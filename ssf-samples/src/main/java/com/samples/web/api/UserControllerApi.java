/*
 * @date 2017年03月30日 下午9:13
 */
package com.samples.web.api;

import com.icourt.core.Result;
import com.samples.dto.UserDTO;
import com.samples.dto.UserUpdateDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * swagger文档
 * @author june
 */
@Api(tags = "user",description = "用户管理")
public interface UserControllerApi {

    @ApiOperation(value="根据用户id获取用户")
    Result<UserDTO> findById(@PathVariable Long id);

    @ApiOperation(value="新增用户")
    Result<Void> save(@RequestBody @Valid UserDTO userDTO);

    @ApiOperation(value="删除用户")
    Result<Void> delete(@PathVariable Long id);

    @ApiOperation(value="修改用户")
    Result<Void> update(@PathVariable Long id,@RequestBody @Valid UserUpdateDto userUpdateDto);

}
