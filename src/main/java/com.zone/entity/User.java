package com.zone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;

/**
 * @ClassName User
 * @Author zone
 * @Date 2018/12/27  9:23
 * @Version 1.0
 */
@Entity
@Table(name = "oel_user")
public class User implements Serializable {
    @Id
    @GeneratedValue
    @Column(name="id")
    private Integer id;
    @Column(name="username")
    private String username;
    @Column(name="password")
    @JsonIgnore
    private String password;
    @Column(name="phone")
    private String phone;
    @Column(name="email")
    private String email;
    @Column(name="points")
    private Integer points;
    @Column(name="headImage")
    private String headImage;
    @Column(name="introduce")
    private String introduce;
    @Transient //临时字段，映射时忽略
    private String address;
    @Column(name="activateCode")
    private String activateCode;
    @Column(name="accountState",columnDefinition = "TINYINT(1)")
    private Integer accountState;
    @Transient //临时字段，映射时忽略
    private String token;
    @Transient //临时字段，映射时忽略
    private Calendar lastActiveDateTime;
    @Transient //临时字段，映射时忽略
    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Calendar getLastActiveDateTime() {
        return lastActiveDateTime;
    }

    public void setLastActiveDateTime(Calendar lastActiveDateTime) {
        this.lastActiveDateTime = lastActiveDateTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getActivateCode() {
        return activateCode;
    }

    public void setActivateCode(String activateCode) {
        this.activateCode = activateCode;
    }

    public Integer getAccountState() {
        return accountState;
    }

    public void setAccountState(Integer accountState) {
        this.accountState = accountState;
    }

    public User(String username, String password, String phone, String email, Integer points, String headImage, String introduce, String address, String activateCode, Integer accountState) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.points = points;
        this.headImage = headImage;
        this.introduce = introduce;
        this.address = address;
        this.activateCode = activateCode;
        this.accountState = accountState;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", points=" + points +
                ", headImage='" + headImage + '\'' +
                ", introduce='" + introduce + '\'' +
                ", address='" + address + '\'' +
                ", activateCode='" + activateCode + '\'' +
                ", accountState=" + accountState +
                '}';
    }
}
