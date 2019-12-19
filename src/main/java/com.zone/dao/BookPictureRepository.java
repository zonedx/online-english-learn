package com.zone.dao;

import com.zone.entity.BookPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @ClassName BookPictureRepository
 * @Author zone
 * @Date 2019/3/7  17:49
 * @Version 1.0
 * @Description
 */
public interface BookPictureRepository extends JpaRepository<BookPicture,Integer> {
    public List<BookPicture> findAllByBookId(@Param("book_id") Integer bookId);
}
