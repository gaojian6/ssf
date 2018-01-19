/*
 * @date 2017?03?30? ??9:12
 */
package com.samples.web;

import com.icourt.common.BeanMapper;
import com.icourt.core.Result;
import com.icourt.core.error.ParamException;
import com.samples.dto.UserDTO;
import com.samples.dto.UserUpdateDto;
import com.samples.pojo.MyUser;
import com.samples.pojo.User;
import com.samples.service.IUserService;
import com.samples.web.api.UserControllerApi;
import io.swagger.models.auth.In;
import jdk.internal.cmm.SystemResourcePressureImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author june
 */
@RestController
@RequestMapping("/user")
public class UserController implements UserControllerApi{

    @Autowired
    private IUserService userService;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Override
    @GetMapping("/{id}")
    public Result<UserDTO> findById(@PathVariable Long id){
        if(id<=0){
            throw new ParamException("id??");
        }
        logger.info("????");
        User user = userService.findById(id);
        UserDTO userDTO = BeanMapper.map(user,UserDTO.class);
        logger.warn("??");
        logger.info("INFO ?? {},{}","aaa","bbb");
        try {
            String str = null;
            str.indexOf(".");
        }catch (Exception e){
            logger.error("???",e);
        }
        logger.debug("debug???");

        return Result.success(userDTO);
    }

    @Override
    @PostMapping("/save")
    public Result<Void> save(@RequestBody @Valid UserDTO userDTO){
        User user = userService.findByUserName(userDTO.getUsername());
        if(user != null){
            return Result.fail("??????");
        }
        userService.save(userDTO);
        return Result.success();
    }

    @Override
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id){
        if(id<=0 || userService.findById(id) == null){
            throw new ParamException("id??");
        }http://localhost:8080/samples/swagger-ui.html#
        userService.delete(id);
        return Result.success();
    }

    @Override
    @PutMapping("/update/{id}")
    public Result<Void> update(@PathVariable Long id,@RequestBody @Valid UserUpdateDto userUpdateDto){
        User user = userService.findById(id);
        if(user == null){
            throw new ParamException("id??");
        }
        BeanMapper.copy(userUpdateDto,user);
        userService.update(user);
        return Result.success();
    }


    @Override
    @GetMapping(value = "/select")
    public Result<List<MyUser>> select() {
        List<MyUser> myuserlist = userService.select();

        System.out.println(myuserlist);

        System.out.println(Result.success(myuserlist));

        return Result.success(myuserlist);

    }


    @Override
    @GetMapping(value = "/selectbydepartment")
    public Result<List<MyUser>> selectByDepartment(String departmentName){
        List<MyUser> myuserlist = userService.selectByDepartment(departmentName);
        System.out.println(userService.selectByDepartment(departmentName));
        return Result.success(myuserlist);
    }

    @Override
    @GetMapping(value = "/insertuserbydepartment")
    public Result<Void> insertUserByDepartment(String departmentName,String username){
        MyUser myuser = userService.findByName(username);
        if(myuser == null) {
            return Result.fail("用户不存在");
        }
        Integer departmentid = userService.findDepartment(departmentName);
        if(departmentid == null){
            return Result.fail("部门不存在");
        }
        userService.setDepartment(departmentid,username);
        return Result.success();
    }

}
