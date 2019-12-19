package com.zone.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @ClassName FotgotWord
 * @Author zone
 * @Date 2019/1/12  17:10
 * @Version 1.0
 * @Description
 */
@Entity
@Table(name = "oel_forgotword")
public class ForgotWord implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "word_id")
    private Integer wordId;
    @Column(name = "english")
    private String english;
    @Column(name = "times")
    private Integer times;//遗忘次数
    @Column(name = "storedDatetime")
    private Timestamp storedDatetime;//最近记忆时间

    @Override
    public String toString() {
        return "ForgotWord{" +
                "id=" + id +
                ", wordId=" + wordId +
                ", english='" + english + '\'' +
                ", times=" + times +
                ", storedDatetime=" + storedDatetime +
                '}';
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWordId() {
        return wordId;
    }

    public void setWordId(Integer wordId) {
        this.wordId = wordId;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Timestamp getStoredDatetime() {
        return storedDatetime;
    }

    public void setStoredDatetime(Timestamp storedDatetime) {
        this.storedDatetime = storedDatetime;
    }
}
