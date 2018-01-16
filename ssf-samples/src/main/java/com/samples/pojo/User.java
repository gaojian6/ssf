/*
 * @date 2017年03月30日 下午8:15
 */
package com.samples.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author june
 */
@Table(name = "ssf_samples_user")
public class User implements Serializable{

    private static final long serialVersionUID = -7676481238043109996L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private int age;

    @Column(name="birthDay")
    private Date birthDay;

    private Double money;

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
}
