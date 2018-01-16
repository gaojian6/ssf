package com.icourt.quartz;


public interface IQuartzAuthService {
    /**
     * 获取当前用户名或id
     *
     * @return 用户标识
     */
    String getCurrUser();

    /**
     * 是否拥有操作权限
     * @return 是否
     */
    public boolean hasRight();
}
