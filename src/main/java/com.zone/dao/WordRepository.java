package com.zone.dao;

import com.zone.entity.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WordRepository extends JpaRepository<Word, Integer> {

    public Page<Word> findByType(@Param("type") Integer type, Pageable var1);

    public Word findByEnglish(@Param("english") String english);

}
