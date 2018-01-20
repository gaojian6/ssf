/*
 * @date 2017年03月30日 下午8:15
 */
package com.samples.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author june
 */
@ApiModel(description = "用户对象")
public class UserDTO {

    @ApiModelProperty(notes = "id",example = "1",hidden=true)
    private Long id;

    @ApiModelProperty(notes = "姓名",example = "周勇")
    @NotNull
    @Length(min = 1,max = 40,message = "username长度为1-40")
    private String username;

    @ApiModelProperty(notes = "年龄",example = "20")
    private Integer age;

    @ApiModelProperty(notes = "生日",example = "1999-11-20")
    private Date birthDay;

    @ApiModelProperty(notes = "存款",example = "100000000.01")
    private Double money;

    @ApiModelProperty(notes = "备注",example = "这是一个很帅的哥")
    private String remark;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", age=" + age +
                ", birthDay=" + birthDay +
                ", money=" + money +
                ", remark='" + remark + '\'' +
                '}';
    }
}
