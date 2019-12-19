package com.zone.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @ClassName Sentential
 * @Author zone
 * @Date 2019/1/7  13:05
 * @Version 1.0
 * @Description
 */
@Entity
@Table(name = "oel_sentential")
public class Sentential implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    @Column(name = "content")
    private String content;
    @Column(name = "fill_word")
    private String fillWord;
    @Column(name = "type", columnDefinition = "TINYINT(1)")
    private Integer type;
    @Column(name = "sentence_id")
    private String sentenceId;
    @Transient //临时字段，映射时忽略
    private Integer size;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    @Column(name = "translate")

    private String translate;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFillWord() {
        return fillWord;
    }

    public void setFillWord(String fillWord) {
        this.fillWord = fillWord;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(String sentenceId) {
        this.sentenceId = sentenceId;
    }

    @Override
    public String toString() {
        return "Sentential{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", fillWord='" + fillWord + '\'' +
                ", type=" + type +
                ", sentenceId='" + sentenceId + '\'' +
                ", translate='" + translate + '\'' +
                '}';
    }
}
