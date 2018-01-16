/**
 * ProjectName:  mall-framework
 * FileName:  BasePagination.java
 * PackageName:  com.cyou.mall.base
 * Copyright (c) 2013, CYOU All Rights Reserved.
 */
package com.icourt.common.page;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName: BasePagination. <br/>
 * Description: 分页基础类 <br/>
 *
 * @author lan
 * @version 1.0
 */
public class BasePagination<T extends Serializable> implements Pagination<T> {

    /**
     * 是否需要分页
     */
    public Byte isNeedPage = 1;

    /**
     * 默认的每页数据量（pageSize）
     */
    public static final int DEFAULT_PAGE_SIZE = 20;
    /**
     * 默认页码（第一页）
     */
    public static final int DEFAULT_PAGE_NUM = 1;
    /**
     * 默认显示页码标签的个数 如： {首页 1 2 3 4 5 ... 16 17 18 末页}
     */
    public static final int DEFAULT_MAX_PAGE_INDEX_NUMBER = 9;
    /**
     * 显示页码标签的个数
     */
    private transient int maxPageIndexNumber = DEFAULT_MAX_PAGE_INDEX_NUMBER;
    /**
     * 页码编号数组
     */
    protected transient int[] pageNumberList = new int[0];
    /**
     * 需分页的数据总量
     */
    protected int total;
    /**
     * 每页数据量
     */
    protected int pageSize = DEFAULT_PAGE_SIZE;
    /**
     * 总页数
     */
    protected transient int totalPage;
    /**
     * 当前页码
     */
    protected transient int currentPage;
    /**
     * 下一页页码
     */
    protected transient int nextPage;
    /**
     * 上一页页码
     */
    protected transient int previousPage;
    /**
     * 是否有下一页
     */
    protected transient boolean hasNext = false;
    /**
     * 是否有前一页
     */
    protected transient boolean hasPrevious = false;
    /**
     * 获取该页的数据列表
     */
    protected List<T> list;

    public BasePagination() {
        super();
    }

    public BasePagination(int total) {
        this(total, total);
    }

    public BasePagination(int total, int currentPage) {
        this(total, DEFAULT_PAGE_SIZE, currentPage);
    }

    public BasePagination(int total, int pageSize, int currentPage) {
        this(total, pageSize, currentPage, DEFAULT_MAX_PAGE_INDEX_NUMBER);
    }

    public BasePagination(int total, int pageSize, int currentPage, int maxPageIndexNumber) {
        this.maxPageIndexNumber = maxPageIndexNumber;
        init(total, pageSize, currentPage);
    }

    /**
     * 计算当前页的取值范围：pageStartRow和pageEndRow
     */
    private void calculatePage() {
        // 计算总页数
        if ((total % pageSize) == 0) {
            totalPage = total / pageSize;
        } else {
            totalPage = total / pageSize + 1;
        }
        // 判断是否还有上一页
        hasPrevious = (currentPage - 1) > 0;

        // 判断是否还有下一页
        hasNext = currentPage < totalPage;
        // 计算上一页
        if (hasPrevious) {
            previousPage = currentPage - 1;
        }
        // 计算下一页
        if (hasNext) {
            nextPage = currentPage + 1;
        }
    }

    public void init(int total, int pageSize, int currentPage) {
        this.total = total;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        calculatePage();
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public int getNextPage() {
        return nextPage;
    }

    public boolean hasPrevious() {
        return hasPrevious;
    }

    public int getPreviousPage() {
        return previousPage;
    }

    /**
     * 获取页码标签列表大小 <br/>
     */
    public int getMaxPageIndexNumber() {
        return maxPageIndexNumber;
    }

    /**
     * 设置页码标签列表大小 <br/>
     */
    public void setMaxPageIndexNumber(int maxPageIndexNumber) {
        this.maxPageIndexNumber = maxPageIndexNumber;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * 获取页码列表 <br/>
     *
     * @return
     * @author xblibo 2013-3-2 下午3:42:38
     */
    public int[] getPageNumberList() {
        if (totalPage > this.maxPageIndexNumber) {
            this.pageNumberList = new int[this.maxPageIndexNumber];
            int offset = (this.maxPageIndexNumber - 4) / 2;
            if (this.currentPage - offset <= (1 + 2)) {
                for (int index = 0; index < maxPageIndexNumber - 2; index++) {
                    pageNumberList[index] = (index + 1);
                }
            } else if (this.currentPage + offset >= (totalPage - 2)) {
                int start = totalPage;
                for (int index = maxPageIndexNumber - 1; index > 1; index--) {
                    pageNumberList[index] = (start--);
                }
            } else {
                int start = currentPage - offset;
                for (int index = 2; index < maxPageIndexNumber - 2; index++) {
                    pageNumberList[index] = (start++);
                }
            }
            pageNumberList[0] = 1;
            pageNumberList[maxPageIndexNumber - 1] = totalPage;
        } else {
            // 总页数小于 设置的页码标签数
            this.pageNumberList = new int[this.totalPage];
            for (int index = 0; index <= totalPage - 1; index++) {
                pageNumberList[index] = (index + 1);
            }
        }
        return pageNumberList;
    }

    public static void main(String[] args) {
        BasePagination<Integer> pagination = new BasePagination<Integer>(200, 10, 5);
        for (int i : pagination.getPageNumberList()) {
            System.out.print(i + "\t");
        }
    }
}
