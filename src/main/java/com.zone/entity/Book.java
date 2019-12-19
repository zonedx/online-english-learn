package com.zone.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName Book
 * @Author zone
 * @Date 2019/2/28  15:07
 * @Version 1.0
 * @Description
 */
@Entity
@Table(name="oel_book")
public class Book implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "author")
    private String author;
    @Column(name = "author_intro")
    private String authorIntro;
    @Column(name = "content")
    private String content;
    @Column(name = "press")
    private String press;
    @Column(name ="publish_date")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp publishDate;
    @Column(name="introduce")
    private String introduce;
    @Column(name="price")
    private Float price;
    @Column(name = "amount")
    private Integer amount;
    @Column(name = "sale_volume")
    private Integer saleVolume;
    @Column(name = "comment_sum")
    private Integer commentSum;
//    @OneToMany(cascade = CascadeType.ALL,mappedBy = "bookId")
//    private Set<BookPicture> bookPictures=new HashSet<BookPicture>();
    @Column(name = "first_image")
    private String firstImage;

    public Book() {
    }

    public Book(String name, String author, String authorIntro, String content, String press, Timestamp publishDate, String introduce, Float price, Integer amount, Integer saleVolume, Integer commentSum, String firstImage) {
        this.name = name;
        this.author = author;
        this.authorIntro = authorIntro;
        this.content = content;
        this.press = press;
        this.publishDate = publishDate;
        this.introduce = introduce;
        this.price = price;
        this.amount = amount;
        this.saleVolume = saleVolume;
        this.commentSum = commentSum;
        this.firstImage = firstImage;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorIntro() {
        return authorIntro;
    }

    public void setAuthorIntro(String authorIntro) {
        this.authorIntro = authorIntro;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFirstImage() {
        return firstImage;
    }

    public void setFirstImage(String firstImage) {
        this.firstImage = firstImage;
    }

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
}
