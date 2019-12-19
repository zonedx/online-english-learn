package com.zone.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @ClassName UserAddress
 * @Author zone
 * @Date 2019/3/15  9:43
 * @Version 1.0
 * @Description
 */
@Entity
@Table(name = "oel_user_address")
public class UserAddress implements Serializable {
    @Id
    @GeneratedValue
    @Column(name="id")
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "recipient")
    private String recipient;
    @Column(name = "recipient_phone")
    private String recipientPhone;
    @Column(name = "address")
    private String address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
