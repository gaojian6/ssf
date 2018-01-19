/*
 * @date 2017年03月30日 下午9:04
 */
package com.samples.service;

import com.samples.dto.UserDTO;
import com.samples.pojo.MyUser;
import com.samples.pojo.User;

import java.util.ArrayList;
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

    /**
     * 查询所有用户
     * @return 查询结果
     */
    List<User> findAll();

    /**
     * 查找用户
     * @param user 用户信息
     * @return 查询结果
     */
    List<User> findUserList(User user);

    /**
     * 按用户id查找用户
     * @param ids 用户id
     * @return 查询用户信息
     */
    List<User> findByIds(String ids);




    /**
     * 查询用户信息
     * @return 返回查询结果
     */
    List<MyUser> select();

    /**
     * 按部门名称查找用户
     * @param departmentName 部门名称
     * @return 查询的用户信息
     */
    List<MyUser> selectByDepartment(String departmentName);

    /**
     * 按用户名查找
     * @param username 用户名
     * @return 返回查询用户的信息
     */
    MyUser findByName(String username);

    /**
     * 设置用户的部门
     * @param departmentid 部门号
     * @param username 用户名
     */
    void setDepartment(Integer departmentid,String username);

    /**
     * 按部门名称查找部门号
     * @param departmentName
     * @return 部门号
     */
    Integer findDepartment(String departmentName);
}
