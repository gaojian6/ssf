package com.samples.pojo;

import javax.persistence.Table;
import java.util.Date;

/**
 * @author gaojian
 */

@Table(name = "department")
public class Department {
    private int id;
    private String departmentName;
    private Date createTime;
    private Date updateTime;

    public Department() {
    }

    public Department(int id, String departmentName, Date createTime, Date updateTime) {
        this.id = id;
        this.departmentName = departmentName;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartment() {
        return departmentName;
    }

    public void setDepartment(String departmentName) {
        this.departmentName = departmentName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
