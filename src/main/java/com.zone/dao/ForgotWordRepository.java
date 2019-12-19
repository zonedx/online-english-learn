package com.zone.dao;

import com.zone.entity.ForgotWord;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ForgotWordRepository extends JpaRepository<ForgotWord,Integer> {
    @Query(value = "select count(*) from oel_forgotword where oel_forgotword.word_id=:wordId",nativeQuery = true)
    public int isExistThisWord(@Param("wordId") Integer wordId);

    ForgotWord findByWordId(Integer wordId);

}
