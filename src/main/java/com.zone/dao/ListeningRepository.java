package com.zone.dao;

import com.zone.entity.Listening;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * @ClassName ListeningRepository
 * @Author zone
 * @Date 2019/1/24  11:31
 * @Version 1.0
 * @Description
 */
public interface ListeningRepository extends JpaRepository<Listening,Integer> {

    public Page<Listening> findByType(@Param("type") Integer type, Pageable var1);

    public Listening findFirstById(@Param("id") Integer id);
}
