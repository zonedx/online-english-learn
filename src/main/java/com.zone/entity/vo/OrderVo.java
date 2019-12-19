package com.zone.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @ClassName OrderVo
 * @Author zone
 * @Date 2019/3/12  22:09
 * @Version 1.0
 * @Description
 */
public class OrderVo implements Serializable {
    /**
     * 订单创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp createTime;
    /**
     * 这里desc表示订单编号
     */
    private String orderDesc;
    /**
     * 书名
     */
    private String bookName;
    /**
     * 图书封面图
     */
    private String bookImage;
    /**
     * 单价
     */
    private Float price;
    /**
     * 总价
     */
    private Float sumPrice;
    /**
     * 订单是否删除
     */
    private Integer isDeleted;

    public OrderVo() {
    }

    public OrderVo(Timestamp createTime, String orderDesc, String bookName, String bookImage, Float price, Float sumPrice, Integer isDeleted) {
        this.createTime = createTime;
        this.orderDesc = orderDesc;
        this.bookName = bookName;
        this.bookImage = bookImage;
        this.price = price;
        this.sumPrice = sumPrice;
        this.isDeleted = isDeleted;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(Float sumPrice) {
        this.sumPrice = sumPrice;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
