package com.zone.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @ClassName Word
 * @Author zone
 * @Date 2018/12/31  14:08
 * @Version 1.0
 * @Description
 */
@Entity
@Table(name = "oel_word")
public class Word implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    @Column(name="english")
    private String english;
    @Column(name = "chinese")
    private String chinese;
    @Column(name="type",columnDefinition = "TINYINT(1)")
    private Integer type;
    @Column(name = "word_photo")
    private String wordPhoto;

    public String getWordPhoto() {
        return wordPhoto;
    }

    public void setWordPhoto(String wordPhoto) {
        this.wordPhoto = wordPhoto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", english='" + english + '\'' +
                ", chinese='" + chinese + '\'' +
                ", type=" + type +
                ", wordPhoto='" + wordPhoto + '\'' +
                '}';
    }
}
