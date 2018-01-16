package com.samples.service;

import com.samples.AbstractTest;
import com.samples.dto.UserDTO;
import com.samples.pojo.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author june
 */
public class UserServiceTest extends AbstractTest{

    @Autowired
    private IUserService userService;

    @Test
    public void findByUserName() throws Exception {
        User user = userService.findByUserName("周勇33");
        Assert.assertNotNull(user);
        Assert.assertEquals("周勇33",user.getUsername());

        User user2 = userService.findByUserName("2222周勇33");
        Assert.assertNull(user2);
    }

    @Test
    public void findById() throws Exception {
        User user = userService.findById(1L);
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());
        Assert.assertEquals(1L,user.getId().longValue());

        User user2 = userService.findById(100000000L);
        Assert.assertNull(user2);
    }

    @Test
    public void save() throws Exception {
        String userName = "sssss";
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(userName);
        userDTO.setAge(12);
        userService.save(userDTO);
        User user = userService.findByUserName(userName);
        Assert.assertNotNull(user);
        Assert.assertEquals(userName,user.getUsername());
    }

    @Test
    public void update() throws Exception {
        User user = new User();
        user.setId(2L);
        user.setMoney(9999.99);
        userService.update(user);
        User user2 = userService.findById(2L);
        Assert.assertNotNull(user2);
        Assert.assertNotNull(user2.getMoney());
        Assert.assertEquals(9999.99,user2.getMoney().doubleValue(),2);
    }

    @Test
    public void delete() throws Exception {
        userService.delete(3L);
        User user = userService.findById(3L);
        Assert.assertNull(user);
    }

    @Test
    public void findAll() throws Exception {
        List<User> userList = userService.findAll();
        Assert.assertNotNull(userList);
        Assert.assertEquals(3,userList.size());

        User user = new User();
        user.setAge(20);
        userList = userService.findUserList(user);
        Assert.assertNotNull(userList);
        Assert.assertEquals(1,userList.size());

    }

    @Test
    public void findByIds() throws Exception {
        List<User> userList = userService.findByIds("1,2,3");
        Assert.assertNotNull(userList);
        Assert.assertEquals(3,userList.size());
    }

}