package com.icourt.common.service;

import com.icourt.common.page.BasePagination;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 * A common service interface for all access to data mapping.
 *
 * @author lan
 */
public interface GenericService<T, ID extends Serializable> {

    /**
     * Find by id
     *
     * @param id data id
     * @return data object
     */
    T findById(ID id);

    /**
     * Find by id
     *
     * @param id   data id
     * @param lock lock use
     * @return data object
     */
    T findById(ID id, boolean lock);

    /**
     * find all data
     *
     * @return all data object list
     */
    List<T> findAll();

    /**
     * 保存或更新(如果对象已存在)
     *
     * @param object
     * @return TODO
     */
    T saveOrUpdate(T object);

    /**
     * 删除
     *
     * @param entity
     */
    void delete(T entity);

    /**
     * 批量删除
     *
     * @param objects Collection
     */
    void deleteBatch(Collection<T> objects);

    /**
     * 批量保存或更新
     *
     * @param objects Collection
     */
    void saveOrUpdateAll(Collection<T> objects);

    /**
     * just save
     *
     * @param entity
     */
    ID saveEntity(T entity);

    /**
     * save and ignore null field
     *
     * @param entity
     * @return
     */
    ID saveEntitySelective(T entity);

    /**
     * just update
     *
     * @param entity
     */
    void updateEntity(T entity);

    /**
     * update and ignore null field
     *
     * @param entity
     */
    void updateEntitySelective(T entity);

    /**
     * 保存或更新(如果对象已存在)
     *
     * @param entity
     * @return TODO
     */
    T saveOrUpdateSelective(T entity);

    /**
     * find by bo by selective condition
     */
    T findSelective(T entity);

    /**
     * 获取列表
     */
    List<T> findLists(BasePagination t);

    /**
     * 获取分页列表
     */
    List<T> findPageLists(BasePagination t);
}
