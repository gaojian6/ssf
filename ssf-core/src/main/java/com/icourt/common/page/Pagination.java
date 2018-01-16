/**
 * ProjectName:  mall-framework
 * FileName:  Pagination.java
 * PackageName:  com.cyou.mall.base.pagination
 * Copyright (c) 2013, CYOU All Rights Reserved.
 */
package com.icourt.common.page;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName: Pagination. <br/>
 * Description: 分页接口 <br/>
 *
 * @author lan
 * @version 1.0
 */
public interface Pagination<T extends Serializable> extends Serializable {

    /**
     * 当pagination做为参数放入map传到mybatis时，该值为取pagination的key
     */
    String MAP_PAGE_FIELD = "MAP_PAGE_FIELD";
    /**
     * 默认pagesize
     */
    int DEFAULT_PAGE_SIZE = 50;

    /**
     * 获取需分页的数据总量 <br/>
     *
     * @return
     * @author xblibo 2013-3-2 下午12:15:52
     */
    public int getTotal();

    /**
     * 获取每页数据容量<br/>
     *
     * @return
     * @author xblibo 2013-3-2 下午12:13:45
     */
    int getPageSize();

    /**
     * 获取总页数 <br/>
     *
     * @return
     * @author xblibo 2013-3-2 下午12:18:32
     */
    public int getTotalPage();

    /**
     * 获取当前页码数 <br/>
     *
     * @return
     * @author xblibo 2013-3-2 下午12:08:40
     */
    int getCurrentPage();

    /**
     * 是否还有下一页 <br/>
     *
     * @return
     * @author xblibo 2013-3-2 下午12:11:41
     */
    public boolean hasNext();

    /**
     * 获取下一页页码 <br/>
     *
     * @return
     * @author xblibo 2013-3-2 下午12:33:33
     */
    public int getNextPage();

    /**
     * 是否还有上一页 <br/>
     *
     * @return
     * @author xblibo 2013-3-2 下午12:12:02
     */
    public boolean hasPrevious();

    /**
     * 获取上一页页码 <br/>
     *
     * @return
     * @author xblibo 2013-3-2 下午12:33:58
     */
    public int getPreviousPage();

    /**
     * 获取该页的数据列表 <br/>
     *
     * @return
     * @author xblibo 2013-3-2 下午12:35:11
     */
    public List<T> getList();

    /**
     * 设置该页数据 <br/>
     *
     * @param list
     * @author xblibo 2013-3-2 下午5:20:28
     */
    public void setList(List<T> list);

    /**
     * 初始化page各项参数 <br/>
     *
     * @param total
     * @param pageSize
     * @param currentPage
     * @author xblibo 2013-3-2 下午2:05:25
     */
    public void init(int total, int pageSize, int currentPage);

    /**
     * 获取页码标签列表大小 <br/>
     *
     * @return
     * @author xblibo 2013-3-2 下午3:37:37
     */
    public int getMaxPageIndexNumber();

    /**
     * 设置页码标签列表大小 <br/>
     *
     * @param maxPageIndexNumber
     * @author xblibo 2013-3-2 下午3:38:43
     */
    public void setMaxPageIndexNumber(int maxPageIndexNumber);

    /**
     * 获取页码列表 <br/>
     *
     * @return
     * @author xblibo 2013-3-2 下午3:42:38
     */
    public int[] getPageNumberList();

    /**
     * 设置总页数 <br/>
     *
     * @param total
     * @author xblibo 2013-3-2 下午5:18:08
     */
    public void setTotal(int total);

    /**
     * 设置每页大小 <br/>
     *
     * @param pageSize
     * @author xblibo 2013-3-2 下午5:18:22
     */
    public void setPageSize(int pageSize);

    /**
     * 设置当前页 <br/>
     *
     * @param currentPage
     * @author xblibo 2013-3-2 下午5:19:28
     */
    public void setCurrentPage(int currentPage);

}
