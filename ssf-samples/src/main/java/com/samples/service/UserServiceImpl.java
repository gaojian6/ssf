/*
 * @date 2017年03月30日 下午9:06
 */
package com.samples.service;

import com.icourt.core.error.ParamException;
import com.samples.dao.IUserDao;
import com.samples.dao.IUserMapper;
import com.samples.dto.UserDTO;
import com.samples.pojo.MyUser;
import com.samples.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author june
 */
@Service
public class UserServiceImpl implements IUserService{

    @Autowired
    private IUserDao userDao;

    @Autowired
    private IUserMapper userMapper;

    @Override
    public User findByUserName(String username) {
        if(username == null){
            return null;
        }
        return userDao.findByUserName(username);
    }

    @Override
    public User findById(Long id) {
        if(id == null){
            return null;
        }
        return userDao.findById(id);
    }

    @Override
    public void save(UserDTO userDTO) {
        if(userDTO.getId() != null){
            throw new ParamException("user的主键id不为null");
        }
        userDao.save(userDTO);
    }

    @Override
    public void update(User user) {
        if(user.getId() == null){
            throw new ParamException("user的主键id不能为null");
        }
        userDao.update(user);
    }

    @Override
    public void delete(Long id) {
        if(id != null){
            userDao.delete(id);
        }
    }

    @Override
    public List<User> findAll() {
        return userMapper.selectAll();
    }

    @Override
    public List<User> findUserList(User user) {
        return userMapper.select(user);
    }

    @Override
    public List<User> findByIds(String ids) {
        return userMapper.selectByIds(ids);
    }


    @Override
    public List<MyUser> select(){
        return userDao.select();
    }

    @Override
    public List<MyUser> selectByDepartment(String departmentName){
        return userDao.selectByDepartment(departmentName);
    }

    @Override
    public MyUser findByName(String username){
        return userDao.findByName(username);
    }

    @Override
    public Integer findDepartment(String departmentName){
        return userDao.findDepartment(departmentName);
    }


    @Override
    public void setDepartment(Integer departmentid,String username){
        userDao.setDepartment(departmentid,username);
    }

}
