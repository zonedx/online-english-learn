package com.zone.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @ClassName BookPicture
 * @Author zone
 * @Date 2019/3/2  13:16
 * @Version 1.0
 * @Description
 */
@Entity
@Table(name = "oel_book_picture")
public class BookPicture implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "pid")
    private Integer pid;

    //private Integer BookId;
    @Column(name = "picture_name")
    private String pname;
    @Column(name = "picture_place")
    private String picturePlace;

    @Column(name = "book_id")//外键
    private Integer bookId;
    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPicturePlace() {
        return picturePlace;
    }

    public void setPicturePlace(String picturePlace) {
        this.picturePlace = picturePlace;
    }
}
