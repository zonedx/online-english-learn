package com.zone.dao;

import com.zone.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    public List<Cart> queryAllByUserId(@Param("user_id") Integer userId);
    @Query(nativeQuery = true,value = "select count(*) from oel_cart cart where cart.user_id=?1 and cart.book_id=?2")
    public int isExistCart(Integer userId, Integer bookId);
}
