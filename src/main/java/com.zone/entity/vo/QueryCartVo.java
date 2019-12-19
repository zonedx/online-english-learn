package com.zone.entity.vo;

/**
 * @ClassName QueryCartVo
 * @Author zone
 * @Date 2019/3/12  15:10
 * @Version 1.0
 * @Description
 */
public class QueryCartVo {
    private Integer id;
    private Integer bookId;
    /**
     * 图书名称
     */
    private String bookName;
    /**
     * 图书单价
     */
    private Float price;
    /**
     * 图书介绍
     */
    private String descs;
    /**
     * 图书图片
     */
    private String bookImage;
    /**
     * 总价
     */
    private Double sumPrice;

    public QueryCartVo() {
    }

    public QueryCartVo(Integer id, Integer bookId, String bookName, Float price, String descs, String bookImage, Double sumPrice) {
        this.id = id;
        this.bookId = bookId;
        this.bookName = bookName;
        this.price = price;
        this.descs = descs;
        this.bookImage = bookImage;
        this.sumPrice = sumPrice;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDescs() {
        return descs;
    }

    public void setDescs(String descs) {
        this.descs = descs;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public Double getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(Double sumPrice) {
        this.sumPrice = sumPrice;
    }
}
