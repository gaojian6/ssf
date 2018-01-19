/*
 * @date 2017年03月30日 下午8:55
 */
package com.samples.dao;

import com.samples.dto.UserDTO;
import com.samples.pojo.MyUser;
import com.samples.pojo.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * @author june
 */
@Mapper
public interface IUserDao {

    /**
     * 按id查找
     *
     * @param id 用户id
     * @return 查找结果
     */
    @Select("select * from `myuser` where `id` = #{id}")
    User findById(@Param("id") Long id);

    /**
     * 按用户名查询
     *
     * @param username 用户名
     * @return 查询结果
     */
    @Select("select * from `ssf_samples_user` where `username` = #{username}")
    User findByUserName(@Param("username") String username);

    /**
     * 插入用户
     *
     * @param user 用户信息
     */
    @Insert("INSERT INTO `ssf_samples_user` " +
            "(username,age,birthDay,money,remark) " +
            "VALUES(" +
            " #{username}," +
            " #{age}," +
            " #{birthDay}," +
            " #{money}," +
            " #{remark}" +
            ")")
    void save(UserDTO user);

    /**
     * 更新用户表
     *
     * @param user 用户信息
     */
    @Update("update `ssf_samples_user` set " +
            " age=#{age}," +
            " birthDay=#{birthDay}," +
            " money=#{money}," +
            " remark=#{remark}" +
            " where id=#{id}")
    void update(User user);

    /**
     * 删除用户信息
     *
     * @param id 用户的id
     */
    @Delete("delete from `ssf_samples_user` where id=#{id}")
    void delete(Long id);


    /**
     * 查询用户
     *
     * @return 返回查询结果
     */
    List<MyUser> select();

    /**
     * 按照部门名称查找用户
     *
     * @param departmentName
     * @return 查询结果
     */
    List<MyUser> selectByDepartment(String departmentName);

    /**
     * 按用户名查找
     *
     * @param username 用户名
     * @return 查询用户信息
     */
    MyUser findByName(String username);

    /**
     * 设置用户的部门名称
     *
     * @param departmentid 部门号
     * @param username     用户名
     */
    void setDepartment(@Param("departmentid") Integer departmentid, @Param("username") String username);

    /**
     * 查找部门名称的部门号
     *
     * @param departmentName
     * @return 部门号
     */
    Integer findDepartment(String departmentName);
}
