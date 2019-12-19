package com.zone.dao;

import com.zone.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface VideoRepository extends JpaRepository<Video,Integer> {

    public Video findFirstById(@Param("id") Integer id);

}
