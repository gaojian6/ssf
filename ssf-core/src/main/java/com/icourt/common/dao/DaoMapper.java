package com.icourt.common.dao;

import com.icourt.common.page.BasePagination;

import java.io.Serializable;
import java.util.List;

/**
 * base sql mapper interface
 *
 * @param <T>  entity bean class type
 * @param <ID> primary key class type
 * @author lan
 * @since 1.0.0.0
 */
public interface DaoMapper<T, ID extends Serializable> {

    /**
     * delete action by primary key
     *
     * @param id primary key
     * @return delete result. 1 means success
     */
    int deleteByPrimaryKey(ID id);

    /**
     * do insert entity
     *
     * @param record entity bean to insert
     * @return insert result 1 means success
     */
    int insert(T record);

    /**
     * do insert entity ignore null property
     *
     * @param record entity bean to insert
     * @return insert result 1 means success
     */
    int insertSelective(T record);

    /**
     * find entity by primary key
     *
     * @param id primary key
     * @return entity bean
     */
    T selectByPrimaryKey(ID id);

    /**
     * update entity by primary key ignore null property
     *
     * @param record entity bean
     * @return effective count
     */
    int updateByPrimaryKeySelective(T record);

    /**
     * update entity by primary key
     *
     * @param record entity bean
     * @return effective count
     */
    int updateByPrimaryKey(T record);

    /**
     * do select count(*) mapped id
     *
     * @return record count
     */
    long count();

    /**
     * find all records
     *
     * @return all records
     */
    List<T> selectAll();

    /**
     * find by bo by selective condition
     */
    T findSelective(T t);

    /**
     * 获取列表
     */
    List<T> findLists(BasePagination t);

    /**
     * 获取分页列表
     */
    List<T> findPageLists(BasePagination t);
}
