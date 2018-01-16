/*
 * @date 2017年03月30日 下午8:15
 */
package com.samples.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 修改用户dto
 * @author june
 */
@ApiModel(description = "用户对象")
public class UserUpdateDto {

    @ApiModelProperty(notes = "年龄",example = "20")
    private int age;

    @ApiModelProperty(notes = "生日",example = "1999-11-20")
    private Date birthDay;

    @ApiModelProperty(notes = "存款",example = "100000000.01")
    private Double money;

    @ApiModelProperty(notes = "备注",example = "这是一个很帅的哥")
    private String remark;


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
