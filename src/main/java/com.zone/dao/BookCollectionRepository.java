package com.zone.dao;

import com.zone.entity.BookCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface BookCollectionRepository extends JpaRepository<BookCollection,Integer> {
    public BookCollection findByUserIdAndBookId(@Param("user_id") Integer userId,
                                                @Param("book_id") Integer bookId);
}
