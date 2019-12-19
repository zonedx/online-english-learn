package com.zone.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @ClassName Game
 * @Author zone
 * @Date 2019/2/21  8:47
 * @Version 1.0
 * @Description
 */
@Entity
@Table(name="oel_game")
public class Game implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    @Column(name = "title")
    private String title;
    @Column(name = "game_place")
    private String gamePlace;
    @Column(name = "image")
    private String image;

    public Game(String title, String gamePlace, String image) {
        this.title = title;
        this.gamePlace = gamePlace;
        this.image = image;
    }

    public Game() {
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

    public String getGamePlace() {
        return gamePlace;
    }

    public void setGamePlace(String gamePlace) {
        this.gamePlace = gamePlace;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
