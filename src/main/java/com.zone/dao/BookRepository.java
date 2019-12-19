package com.zone.dao;

import com.zone.entity.Book;
import com.zone.entity.vo.BookVo;
import com.zone.entity.vo.QueryCartVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Integer> {
    /**
     * 此处加上group by book.id的目的是为了只显示第一张图片
     * @return
     */
    @Query(nativeQuery = true,value = "SELECT book.id,book.name,book.press,book.publish_date as publishDate,book.introduce,book.price,book.amount,book.sale_volume as saleVolume," +
            "book.comment_sum as commentSum,picture.picture_place as picturePlace,co.collection_time as colletionTime,co.is_deleted as isDeleted " +
            "from oel_book book join oel_book_picture picture join oel_book_collection co " +
            "where " +
            " book.id=picture.book_id"+
            " group by book.id")
    public List<Object[]> findBookVo();
    @Query(nativeQuery = true,
           value = "select cart.id as id,book.id as bookId,book.name,book.price,book.introduce as descs,book.first_image as bookImage,cart.amount*book.price as sumPrice" +
                   " from oel_book book  join oel_cart cart" +
                   " where book.id=cart.book_id and book.id=?1 group by book.id")
    public Object findQueryCartVo(Integer bookId);

    public Book findFirstById(@Param("id") Integer id);

    public Page<Book> findAll(Pageable var);

    public Page<Book> findByNameLike(@Param("name") String name, Pageable var);
}