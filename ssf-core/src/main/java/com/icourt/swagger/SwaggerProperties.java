/*
 * @date 2016年11月20日 22:03
 */
package com.icourt.swagger;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author june
 */
@ConfigurationProperties(prefix = "ssf.swagger", ignoreUnknownFields = false)
public class SwaggerProperties {

    private String title = "API";

    private String description = "API documentation";

    private String version = "0.0.1";

    private String termsOfServiceUrl = "http://www.icourt.cc";

    private String contactName = "iCourt";

    private String contactUrl = "http://www.icourt.cc";

    private String contactEmail = "zhouyong@icourt.cc";

    private String license;

    private String licenseUrl;

    private String includePattern;

    //api基本包路径
    private String basePackage;

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTermsOfServiceUrl() {
        return termsOfServiceUrl;
    }

    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
        this.termsOfServiceUrl = termsOfServiceUrl;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactUrl() {
        return contactUrl;
    }

    public void setContactUrl(String contactUrl) {
        this.contactUrl = contactUrl;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public String getIncludePattern() {
        return includePattern;
    }

    public void setIncludePattern(String includePattern) {
        this.includePattern = includePattern;
    }
}
