/*
 * @date 2017年04月18日 下午8:47
 */
package com.samples.dao;

import com.samples.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author june
 */
@Mapper
public interface IUserMapper extends ISamplesMapper<User> {

}
