package com.samples.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author gaojian
 */

@Data
public class MyUser implements Serializable{


    private long id;
    private String name;
    private int departmentId;
    private String phone;
    private String address;
    private int age;


}
