/*
 * @date 2017年03月30日 下午9:04
 */
package com.samples.service;

import com.samples.dto.UserDTO;
import com.samples.pojo.User;

import java.util.List;

/**
 * @author june
 */
public interface IUserService {

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return user
     */
    User findByUserName(String username);

    /**
     * 根据id查找用户
     * @param id 用户id
     * @return user
     */
    User findById(Long id);

    /**
     * 新增用户
     * @param userDTO 用户
     */
    void save(UserDTO userDTO);

    /**
     * 修改用户
     * @param user 用户
     */
    void update(User user);

    /**
     * 根据用户id删除用户
     * @param id 用户id
     */
    void delete(Long id);

    List<User> findAll();

    List<User> findUserList(User user);

    List<User> findByIds(String ids);


}
