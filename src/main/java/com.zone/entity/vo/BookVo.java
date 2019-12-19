package com.zone.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zone.entity.Book;
import com.zone.entity.BookCollection;
import com.zone.entity.BookPicture;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @ClassName BookVo
 * @Author zone
 * @Date 2019/3/2  13:13
 * @Version 1.0
 * @Description
 */

public class BookVo {
    private Integer id;
    private String name;

    private String press;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp publishDate;

    private String introduce;

    private Float price;

    private Integer amount;

    private Integer saleVolume;

    private Integer commentSum;
    /**
     * 书籍相关图片
     */
    private String picturePlace;
    /**
     * 是否收藏书籍
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp collectionTime;

    private Integer isDeleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public Timestamp getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Timestamp publishDate) {
        this.publishDate = publishDate;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getSaleVolume() {
        return saleVolume;
    }

    public void setSaleVolume(Integer saleVolume) {
        this.saleVolume = saleVolume;
    }

    public Integer getCommentSum() {
        return commentSum;
    }

    public void setCommentSum(Integer commentSum) {
        this.commentSum = commentSum;
    }

    public String getPicturePlace() {
        return picturePlace;
    }

    public void setPicturePlace(String picturePlace) {
        this.picturePlace = picturePlace;
    }

    public Timestamp getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(Timestamp collectionTime) {
        this.collectionTime = collectionTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public BookVo() {
        super();
    }

    public BookVo(Integer id, String name, String press, Timestamp publishDate, String introduce, Float price, Integer amount, Integer saleVolume, Integer commentSum, String picturePlace, Timestamp collectionTime, Integer isDeleted) {
        this.id = id;
        this.name = name;
        this.press = press;
        this.publishDate = publishDate;
        this.introduce = introduce;
        this.price = price;
        this.amount = amount;
        this.saleVolume = saleVolume;
        this.commentSum = commentSum;
        this.picturePlace = picturePlace;
        this.collectionTime = collectionTime;
        this.isDeleted = isDeleted;
    }
}
