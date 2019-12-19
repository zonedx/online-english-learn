package com.zone.dao;

import com.zone.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends JpaRepository<Game,Integer> {

    public Game findFirstById(@Param("id") Integer id);
}
