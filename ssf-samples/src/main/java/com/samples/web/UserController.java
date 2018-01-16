/*
 * @date 2017年03月30日 下午9:12
 */
package com.samples.web;

import com.icourt.common.BeanMapper;
import com.icourt.core.Result;
import com.icourt.core.error.ParamException;
import com.samples.dto.UserDTO;
import com.samples.dto.UserUpdateDto;
import com.samples.pojo.User;
import com.samples.service.IUserService;
import com.samples.web.api.UserControllerApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author june
 */
@RestController
@RequestMapping("/user")
public class UserController implements UserControllerApi{

    @Autowired
    private IUserService userService;

    private Logger logger = LoggerFactory.getLogger(UserController.class);


    @GetMapping("/{id}")
    public Result<UserDTO> findById(@PathVariable Long id){
        if(id<=0){
            throw new ParamException("id有误");
        }
        logger.info("测试日志");
        User user = userService.findById(id);
        UserDTO userDTO = BeanMapper.map(user,UserDTO.class);
        logger.warn("警告");
        logger.info("INFO 输出 {},{}","aaa","bbb");
        try {
            String str = null;
            str.indexOf(".");
        }catch (Exception e){
            logger.error("出错了",e);
        }
        logger.debug("debug可以吗");

        return Result.success(userDTO);
    }


    @PostMapping("/save")
    public Result<Void> save(@RequestBody @Valid UserDTO userDTO){
        User user = userService.findByUserName(userDTO.getUsername());
        if(user != null){
            return Result.fail("用户名已存在");
        }
        userService.save(userDTO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id){
        if(id<=0 || userService.findById(id) == null){
            throw new ParamException("id有误");
        }
        userService.delete(id);
        return Result.success();
    }

    @PutMapping("/update/{id}")
    public Result<Void> update(@PathVariable Long id,@RequestBody @Valid UserUpdateDto userUpdateDto){
        User user = userService.findById(id);
        if(user == null){
            throw new ParamException("id有误");
        }
        BeanMapper.copy(userUpdateDto,user);
        userService.update(user);
        return Result.success();
    }


}
