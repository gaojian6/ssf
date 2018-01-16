/*
 * @date 2017年03月30日 下午10:57
 */
package com.samples.web;

import com.samples.AbstractTest;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.MockMvcConfigurer;
import org.springframework.test.web.servlet.setup.MockMvcConfigurerAdapter;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * @author june
 */
public class AbstractWebTest extends AbstractTest{

    protected static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected MockMvc restMockMvc;

    @Before
    public void setup() {
        this.restMockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .alwaysDo(print())
                .apply(this.getMockMvcConfigurer())
                .alwaysExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .build();
    }

    protected MockMvcConfigurer getMockMvcConfigurer(){
        return new MockMvcConfigurerAdapter(){
            public RequestPostProcessor beforeMockMvcCreated(ConfigurableMockMvcBuilder<?> builder, WebApplicationContext cxt) {
                return mockHttpServletRequest -> {
                    mockHttpServletRequest.addHeader("Accept",MediaType.APPLICATION_JSON_UTF8_VALUE);
                    return mockHttpServletRequest;
                };
            }
        };
    }

}
