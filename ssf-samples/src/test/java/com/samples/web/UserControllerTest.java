package com.samples.web;

import com.icourt.core.json.IJsonService;
import com.samples.dto.UserDTO;
import com.samples.dto.UserUpdateDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * @author june
 */
public class UserControllerTest extends AbstractWebTest{

    @Autowired
    private IJsonService jsonService;

    @Test
    public void findById() throws Exception {
        restMockMvc.perform(get("/user/{id}",1))
                .andExpect(jsonPath("$.isSuccess").value(is(true)))
                .andExpect(jsonPath("$.data.id").value(is(1)))
                .andExpect(jsonPath("$.data.username").value(is("周勇33")));

        restMockMvc.perform(get("/user/{id}",10000))
                .andExpect(jsonPath("$.isSuccess").value(is(true)))
                .andExpect(jsonPath("$.data").value(isEmptyOrNullString()));
    }

    @Test
    public void save() throws Exception {
        UserDTO userDTO = new UserDTO();
        restMockMvc.perform(post("/user/save")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonService.toJSONString(userDTO)))
                .andExpect(jsonPath("$.isSuccess").value(is(false)));

        userDTO.setUsername("周勇33");
        restMockMvc.perform(post("/user/save")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonService.toJSONString(userDTO)))
                .andExpect(jsonPath("$.isSuccess").value(is(false)))
                .andExpect(jsonPath("$.resultMsg").value(is("用户名已存在")));

        userDTO.setUsername("周勇3399999");
        restMockMvc.perform(post("/user/save")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonService.toJSONString(userDTO)))
                .andExpect(jsonPath("$.isSuccess").value(is(true)));
    }

    @Test
    public void delete() throws Exception {
        restMockMvc.perform(MockMvcRequestBuilders.delete("/user/{id}",1))
                .andExpect(jsonPath("$.isSuccess").value(is(true)));

        restMockMvc.perform(MockMvcRequestBuilders.delete("/user/{id}",199999))
                .andExpect(jsonPath("$.isSuccess").value(is(false)))
                .andExpect(jsonPath("$.resultMsg").value(is("id有误")));
    }

    @Test
    public void update() throws Exception {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setAge(100);
        restMockMvc.perform(put("/user/update/{id}",1)
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonService.toJSONString(userUpdateDto)))
                .andExpect(jsonPath("$.isSuccess").value(is(true)));

        restMockMvc.perform(put("/user/update/{id}",100000)
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonService.toJSONString(userUpdateDto)))
                .andExpect(jsonPath("$.isSuccess").value(is(false)))
                .andExpect(jsonPath("$.resultMsg").value(is("id有误")));
    }

}