/*
 * @date 2017年03月30日 下午9:13
 */
package com.samples.web.api;

import com.icourt.core.Result;
import com.samples.dto.UserDTO;
import com.samples.dto.UserUpdateDto;
import com.samples.pojo.MyUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * swagger文档
 * @author june
 */
@Api(tags = "user",description = "用户管理")
public interface UserControllerApi {

    /**
     * 根据用户id获取用户
     * @param id 用户的id
     * @return 用户信息
     */
    @ApiOperation(value="根据用户id获取用户")
    Result<UserDTO> findById(@PathVariable Long id);

    /**
     * 新增用户
     * @param userDTO 用户信息
     * @return 是否成功
     */
    @ApiOperation(value="新增用户")
    Result<Void> save(@RequestBody @Valid UserDTO userDTO);

    /**
     * 删除用户
     * @param id 用户的id
     * @return 是否成功
     */
    @ApiOperation(value="删除用户")
    Result<Void> delete(@PathVariable Long id);

    /**
     * 修改用户
     * @param id 用户的id
     * @param userUpdateDto 用户更新后的信息
     * @return 收否成功
     */
    @ApiOperation(value="修改用户")
    Result<Void> update(@PathVariable Long id,@RequestBody @Valid UserUpdateDto userUpdateDto);

    /**
     * 查询所有用户
     * @return 返回查询结果
     */
    @ApiOperation(value="查询所有--用户")
    Result<List<MyUser>> select();

    /**
     * 为一个部门添加制定用户
     * @param deparementName 部门名称
     * @param username 用户名
     * @return 是否成功
     */
    @ApiOperation(value = "为一个部门添加指定用户")
    Result<Void> insertUserByDepartment(String deparementName,String username);

    /**
     * 根据部门查询用户
     * @param departmentName 用户名称
     * @return 返回查询结果
     */
    @ApiOperation(value = "根据部门查询用户")
    Result<List<MyUser>> selectByDepartment(String departmentName);
}
