package com.icourt.core;

/**
 * 版本信息
 * @author june
 */
public final class ApplicationVersion {

    private ApplicationVersion(){}

    /**
     * @see Package#getImplementationVersion()
     * @return
     */
    public static String getVersion() {
        Package pkg = ApplicationAutoConfiguration.class.getPackage();
        return (pkg != null ? pkg.getImplementationVersion() : null);
    }
}
