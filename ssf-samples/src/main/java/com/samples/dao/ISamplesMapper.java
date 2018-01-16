/*
 * @date 2017年04月18日 下午8:54
 */
package com.samples.dao;

import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author june
 */
public interface ISamplesMapper<T> extends Mapper<T>, MySqlMapper<T>,IdsMapper<T> {

}
