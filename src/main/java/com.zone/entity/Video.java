package com.zone.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @ClassName Caption
 * @Author zone
 * @Date 2019/1/9  14:35
 * @Version 1.0
 * @Description
 */
@Entity
@Table(name = "oel_video")
public class Video implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    @Column(name = "title")
    private String title;
    @Column(name = "videoPlace")
    private String videoPlace;
    @Column(name = "updateTime")
    private Timestamp updateTime;
    @Column(name = "editor")
    private String editor;
    @Column(name = "captions")
    private String captions;
    @Column(name="collection",columnDefinition = "TINYINT(1)")
    private Integer collection;
    @Column(name = "image")
    private String image;
    @Column(name = "desc")
    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", videoPlace='" + videoPlace + '\'' +
                ", updateTime=" + updateTime +
                ", editor='" + editor + '\'' +
                ", captions='" + captions + '\'' +
                ", collection=" + collection +
                ", image='" + image + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoPlace() {
        return videoPlace;
    }

    public void setVideoPlace(String videoPlace) {
        this.videoPlace = videoPlace;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getCaptions() {
        return captions;
    }

    public void setCaptions(String captions) {
        this.captions = captions;
    }

    public Integer getCollection() {
        return collection;
    }

    public void setCollection(Integer collection) {
        this.collection = collection;
    }
}
