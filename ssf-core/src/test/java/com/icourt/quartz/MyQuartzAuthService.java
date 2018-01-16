/*
 * @date 2017年08月12日 下午2:05
 */
package com.icourt.quartz;

/**
 * @author june
 */
//@Service
public class MyQuartzAuthService implements IQuartzAuthService{

    @Override
    public String getCurrUser() {
        return "ssfTest";
    }

    @Override
    public boolean hasRight() {
        return true;
    }
}
