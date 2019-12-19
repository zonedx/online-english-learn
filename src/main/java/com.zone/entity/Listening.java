package com.zone.entity;

import org.springframework.stereotype.Controller;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @ClassName Listening
 * @Author zone
 * @Date 2019/1/24  11:20
 * @Version 1.0
 * @Description
 */
@Entity
@Table(name="oel_listening")
public class Listening implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    @Column(name="listening_place")
    private String listeningPlace;
    @Column(name = "content")
    private String content;
    @Column(name = "answer_a")
    private String answerA;
    @Column(name = "answer_b")
    private String answerB;
    @Column(name = "answer_c")
    private String answerC;
    @Column(name = "answer_d")
    private String answerD;
    @Column(name = "correct_answer")
    private String correctAnswer;
    @Column(name = "type", columnDefinition = "TINYINT(1)")
    private Integer type;
    @Column(name = "level", columnDefinition = "TINYINT(1)")
    private Integer level;

    public String getListeningPlace() {
        return listeningPlace;
    }

    public void setListeningPlace(String listeningPlace) {
        this.listeningPlace = listeningPlace;
    }

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

    public String getAnswerA() {
        return answerA;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerC() {
        return answerC;
    }

    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }

    public String getAnswerD() {
        return answerD;
    }

    public void setAnswerD(String answerD) {
        this.answerD = answerD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
