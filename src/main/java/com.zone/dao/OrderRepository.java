package com.zone.dao;

import com.zone.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer> {
    public List<Order> findAllByUserIdAndIsDeleted(@Param("user_id") Integer userId,@Param("is_deleted") Integer isDeleted);
    @Query(nativeQuery = true,
            value = "select orders.create_time as createTime,orders.orders_desc as orderDesc,book.name as bookName," +
                    "book.first_image as bookImage,book.price as price,orders.price as sumPrice,orders.is_deleted as isDeleted" +
                    " from oel_book book,oel_orders orders" +
                    " where book.id=orders.book_id and orders.id=?1 and orders.is_deleted=0")
    public Object findOrderVo(Integer ordersId);

    Order findOrderById(@Param("id")Integer id);
}
