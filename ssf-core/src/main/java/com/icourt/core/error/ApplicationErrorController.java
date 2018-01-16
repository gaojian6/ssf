/*
 * @date 2016年11月23日 14:01
 */
package com.icourt.core.error;

import com.icourt.core.ApplicationProperties;
import com.icourt.core.ApplicationVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProvider;
import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProviders;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author june
 */
@RequestMapping("${ssf.error-path:/ssfError}")
public class ApplicationErrorController implements ErrorController {

    private final ApplicationProperties applicationProperties;
    private final ErrorAttributes errorAttributes;
    private static final Map<HttpStatus.Series, String> SERIES_VIEWS;
    private ApplicationContext applicationContext;

    private final ResourceProperties resourceProperties;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final TemplateAvailabilityProviders templateAvailabilityProviders;

    static {
        Map<HttpStatus.Series, String> views = new HashMap<HttpStatus.Series, String>();
        views.put(HttpStatus.Series.CLIENT_ERROR, "4xx");
        views.put(HttpStatus.Series.SERVER_ERROR, "5xx");
        SERIES_VIEWS = Collections.unmodifiableMap(views);
    }

    public ApplicationErrorController(ErrorAttributes errorAttributes,
                                      ApplicationProperties applicationProperties, ApplicationContext applicationContext,
                                      ResourceProperties resourceProperties) {
        Assert.notNull(applicationContext, "ApplicationContext must not be null");
        Assert.notNull(resourceProperties, "ResourceProperties must not be null");
        this.applicationProperties = applicationProperties;
        this.errorAttributes = errorAttributes;
        this.applicationContext = applicationContext;
        this.resourceProperties = resourceProperties;
        this.templateAvailabilityProviders = new TemplateAvailabilityProviders(
                applicationContext);
    }

    ApplicationErrorController(ErrorAttributes errorAttributes,
                               ApplicationProperties applicationProperties, ApplicationContext applicationContext,
                               ResourceProperties resourceProperties,
                               TemplateAvailabilityProviders templateAvailabilityProviders) {
        Assert.notNull(applicationContext, "ApplicationContext must not be null");
        Assert.notNull(resourceProperties, "ResourceProperties must not be null");
        this.applicationProperties = applicationProperties;
        this.errorAttributes = errorAttributes;
        this.applicationContext = applicationContext;
        this.resourceProperties = resourceProperties;
        this.templateAvailabilityProviders = templateAvailabilityProviders;
    }

    @Override
    public String getErrorPath() {
        return this.applicationProperties.getErrorPath();
    }

    @RequestMapping(produces = "text/html")
    public ModelAndView errorHtml(HttpServletRequest request,
                                  HttpServletResponse response) {
        HttpStatus status = getStatus(request);
        Map<String, Object> model = getErrorAttributes(
                request, isIncludeStackTrace(request, MediaType.TEXT_HTML));
        response.setStatus(status.value());
        String applicationVersion = ApplicationVersion.getVersion();
        if(applicationVersion == null){
            applicationVersion = "1.0-SNAPSHOT";
        }
        model.put("applicationVersion", applicationVersion);
        ModelAndView modelAndView = resolveErrorView(request,status, model);
        if(modelAndView == null){
            modelAndView = new ModelAndView("error", model);
        }
        return modelAndView;
    }

    @RequestMapping
    @ResponseBody
    public ResponseEntity<ErrorVM> error(HttpServletRequest request) throws Throwable {
//        Map<String, Object> body = getErrorAttributes(request,
//                isIncludeStackTrace(request, MediaType.ALL));
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        HttpStatus status = getStatus(request);
        String errorMsg = status.name();
        if(errorAttributes != null){
            Throwable t = errorAttributes.getError(requestAttributes);
            if(t!=null){
                t.printStackTrace();
                throw t;
            }
        }
        ErrorVM errorVM = new ErrorVM(ErrorConstants.ERR_INTERNAL_SERVER_ERROR,errorMsg);
        return new ResponseEntity<>(errorVM, status);
    }

    /**
     * Determine if the stacktrace attribute should be included.
     * @param request the source request
     * @param produces the media type produced (or {@code MediaType.ALL})
     * @return if the stacktrace attribute should be included
     */
    private boolean isIncludeStackTrace(HttpServletRequest request,
                                          MediaType produces) {
        ApplicationProperties.IncludeStacktrace include = this.applicationProperties.getIncludeStacktrace();
        if (include == ApplicationProperties.IncludeStacktrace.ALWAYS) {
            return true;
        }
        if (include == ApplicationProperties.IncludeStacktrace.ON_TRACE_PARAM) {
            return getTraceParameter(request);
        }
        return false;
    }

    private ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status,
                                          Map<String, Object> model) {
        ModelAndView modelAndView = resolve(String.valueOf(status), model);
        if (modelAndView == null && SERIES_VIEWS.containsKey(status.series())) {
            modelAndView = resolve(SERIES_VIEWS.get(status.series()), model);
        }
        return modelAndView;
    }

    private ModelAndView resolve(String viewName, Map<String, Object> model) {
        String errorViewName = "error/" + viewName;
        TemplateAvailabilityProvider provider = this.templateAvailabilityProviders
                .getProvider(errorViewName, this.applicationContext);
        if (provider != null) {
            return new ModelAndView(errorViewName, model);
        }
        return resolveResource(errorViewName, model);
    }

    private ModelAndView resolveResource(String viewName, Map<String, Object> model) {
        for (String location : this.resourceProperties.getStaticLocations()) {
            try {
                Resource resource = this.applicationContext.getResource(location);
                resource = resource.createRelative(viewName + ".html");
                if (resource.exists()) {
                    return new ModelAndView(new HtmlResourceView(resource), model);
                }
            }
            catch (Exception ignored) {
            }
        }
        return null;
    }
    /**
     * {@link View} backed by an HTML resource.
     */
    private static class HtmlResourceView implements View {

        private Resource resource;

        HtmlResourceView(Resource resource) {
            this.resource = resource;
        }

        @Override
        public String getContentType() {
            return MediaType.TEXT_HTML_VALUE;
        }

        @Override
        public void render(Map<String, ?> model, HttpServletRequest request,
                           HttpServletResponse response) throws Exception {
            response.setContentType(getContentType());
            FileCopyUtils.copy(this.resource.getInputStream(),
                    response.getOutputStream());
        }

    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request,
                                                     boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return this.errorAttributes.getErrorAttributes(requestAttributes,
                includeStackTrace);
    }

    private boolean getTraceParameter(HttpServletRequest request) {
        String parameter = request.getParameter("trace");
        return parameter != null && !"false".equals(parameter.toLowerCase());
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request
                .getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        }
        catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

}
