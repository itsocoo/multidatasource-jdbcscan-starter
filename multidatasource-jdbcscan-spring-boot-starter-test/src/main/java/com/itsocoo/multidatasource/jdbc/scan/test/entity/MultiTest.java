package com.itsocoo.multidatasource.jdbc.scan.test.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wanghaibo
 * @version V1.0
 * @Description 多数据源的测试用表
 * @date 2018/4/9 17:17
 */
public class MultiTest implements Serializable {
    private static final long serialVersionUID = -6063997369155406598L;

    private Long id;
    private Integer trigger = 1;
    private Integer age;
    private String brand;
    private String username;
    private String password;
    private String lastName;
    private String endName;
    private String content;
    private Integer status = 0;
    private Date createTime = new Date();
    private Date lastUpdateTime;
    private BigDecimal amt;
    private BigDecimal price;

    public MultiTest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTrigger() {
        return trigger;
    }

    public void setTrigger(Integer trigger) {
        this.trigger = trigger;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEndName() {
        return endName;
    }

    public void setEndName(String endName) {
        this.endName = endName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "MultiTest{" +
                "id=" + id +
                ", trigger=" + trigger +
                ", age=" + age +
                ", brand='" + brand + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", lastName='" + lastName + '\'' +
                ", endName='" + endName + '\'' +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                ", amt=" + amt +
                ", price=" + price +
                '}';
    }
}
