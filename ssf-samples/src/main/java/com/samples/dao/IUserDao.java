/*
 * @date 2017年03月30日 下午8:55
 */
package com.samples.dao;

import com.samples.dto.UserDTO;
import com.samples.pojo.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author june
 */
@Mapper
public interface IUserDao {

    @Select("select * from `ssf_samples_user` where `id` = #{id}")
    User findById(@Param("id") Long id);

    @Select("select * from `ssf_samples_user` where `username` = #{username}")
    User findByUserName(@Param("username") String username);

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

    @Update("update `ssf_samples_user` set " +
            " age=#{age}," +
            " birthDay=#{birthDay}," +
            " money=#{money}," +
            " remark=#{remark}" +
            " where id=#{id}")
    void update(User user);

    @Delete("delete from `ssf_samples_user` where id=#{id}")
    void delete(Long id);


}
