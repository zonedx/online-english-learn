package com.zone.service;


import com.zone.dao.*;
import com.zone.entity.*;
import com.zone.entity.vo.BookVo;
import com.zone.entity.vo.OrderVo;
import com.zone.entity.vo.QueryCartVo;
import com.zone.exception.BooksAmountNotEnoughException;
import com.zone.exception.UserPointsNotEnoughException;
import com.zone.util.datetime.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

/**
 * @ClassName BookService
 * @Author zone
 * @Date 2019/2/28  15:13
 * @Version 1.0
 * @Description
 */
@Service("bookService")
public class BookService {
    public static final Logger logger = Logger.getLogger(BookService.class.getName());
    //日志前缀字符串,方便通过日志定位程序
    private static String logPrefix = null;
    @Autowired
    private UserService userService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookPictureRepository bookPictureRepository;
    @Autowired
    private BookCollectionRepository bookCollectionRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderRepository orderRepository;

    /**
     * 查询所有图书
     * @return
     */
    public List<BookVo> findAllBooks() {
        List<Object[]> obj = bookRepository.findBookVo();
        List<BookVo> bookVos=null;
        try {
            bookVos=castEntity(obj,BookVo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookVos;
    }

    /**
     * 查询图书详情
     * @param request
     * @param id
     * @return
     */
    public Book findBookDetail(HttpServletRequest request,Integer id){
       // User user=userService.findOneByLoginUserMap(request);

        Book book=bookRepository.findFirstById(id);

        return book;
    }
    public List<BookPicture> findBookDetailPic(Integer id){
        List<BookPicture> bookPictures=bookPictureRepository.findAllByBookId(id);
        return bookPictures;
    }
    /**
     * 条件condition:
     *  1--销量从高到低
     *  2--价格从低到高
     *  3--价格从高到低
     *  4--搜索图书
     *  5--默认
     * @param page
     * @param condition
     * @return
     */
    public Page<Book> findAllBooksByCodition(Integer page,Integer condition,String name){
        Pageable pageable=null;
        Page<Book> books=null;
        if(condition==1){
            pageable=new PageRequest(page,3,Sort.Direction.DESC,"saleVolume");//降序
            books=bookRepository.findAll(pageable);
        }else if (condition==2){
            pageable=new PageRequest(page,3,Sort.Direction.ASC,"price");//升序
            books=bookRepository.findAll(pageable);
        }else if (condition==3){
            pageable=new PageRequest(page,3,Sort.Direction.DESC,"price");
            books=bookRepository.findAll(pageable);
        }else if(condition==4){
            pageable=new PageRequest(page,3,Sort.Direction.ASC,"id");
            books=bookRepository.findByNameLike("%"+name+"%",pageable);
        }else if(condition==5){
            pageable=new PageRequest(page,3,Sort.Direction.ASC,"id");
            books=bookRepository.findAll(pageable);
        }
        return books;
    }
    /**
     * 购买图书
     * @param request
     * @param bookId
     * @param amount
     * @return
     */
    @Transactional
    public List<OrderVo> purchaseBooks(HttpServletRequest request,Integer bookId,Integer amount,Integer addressId){
        logPrefix="[BookService.purchaseBooks()]:";
        Order order=new Order();
        List<OrderVo> orderVos=null;
        User user=null;
        Book book=null;
        user=userService.findOneByLoginUserMap(request);
        //1、根据图书id（bookId）查询图书余量
        book=bookRepository.findFirstById(bookId);
        int remainAmount=book.getAmount();//剩余库存
        float bookPrice=book.getPrice();
        if(amount>remainAmount){
            logger.info(logPrefix+"图书余量不足");
            throw new BooksAmountNotEnoughException("图书余量不足");
        }
        //2、图书余量充足，添加订单
        float amountPrice=bookPrice*amount;//此订单总价
        order.setUserId(user.getId());
        order.setAddressId(addressId);
        order.setBookId(bookId);
        order.setBookName(book.getName());
        order.setBookImage(book.getFirstImage());
        order.setCreateTime(DateTimeUtil.calendarToTimestamp(Calendar.getInstance()));
        order.setAmount(amount);
        order.setPrice(amountPrice);
        order.setOrdersDesc(String.valueOf(Calendar.getInstance().getTimeInMillis()));//当前时间戳作为desc
        order.setIsDeleted(0);
        orderRepository.save(order);
        //3、增加销量，减少库存，扣除用户积分
        if(amountPrice>user.getPoints()){
            logger.info(logPrefix+"用户积分不足");
            throw new UserPointsNotEnoughException("用户积分不足");
        }
        book.setAmount(remainAmount-amount);
        book.setSaleVolume(book.getSaleVolume()+amount);
        int remainPoints= (int) (user.getPoints()-amountPrice);
        user.setPoints(remainPoints);
        bookRepository.save(book);
        userService.save(user);
        //4、更新返回订单视图orderVo
        Object orderVo=orderRepository.findOrderVo(order.getId());

        List<Object[]> objects=new ArrayList<Object[]>();
        objects.add((Object[]) orderVo);
        try {
            orderVos=castEntity(objects,OrderVo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderVos;
    }

    public List<OrderVo> testOrder(Integer id){
        Object obj=orderRepository.findOrderVo(id);
        List<OrderVo> re=null;
        List<Object[]> objects=new ArrayList<Object[]>();
        objects.add((Object[]) obj);
        try {
            re=castEntity(objects,OrderVo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re;
    }
    /**
     * 加入购物车
     * @param request
     * @param bookId
     * @param amount
     * @return
     */
    public Boolean addToCart(HttpServletRequest request,Integer bookId,Integer amount){
        User user=null;
        Cart cart=new Cart();
        user=userService.findOneByLoginUserMap(request);
        int userId=user.getId();
        int isExistCart=cartRepository.isExistCart(userId,bookId);
        if(isExistCart>=1){
            logger.info("加入购物车失败，购物车中已存在此图书！");
            return false;
        }else {
            cart.setUserId(userId);
            cart.setBookId(bookId);
            cart.setAmount(amount);
            if(this.save(cart)){
                return true;
            }else {
                logger.info("加入购物车失败，原因未知！");
                return false;
            }
        }
    }

    /**
     * 移除出购物车
     * @param id
     * @return
     */
    public Boolean removeCart(Integer id){
        cartRepository.deleteById(id);
        return true;
    }

    /**
     * 查询购物车
     * @param request
     * @return
     */
    public List<QueryCartVo> findAllCart(HttpServletRequest request){
        int userId=userService.findOneByLoginUserMap(request).getId();//根据用户Id查询购物车
        List<QueryCartVo> queryCartVos=new ArrayList<QueryCartVo>();
        List<Cart> carts=cartRepository.queryAllByUserId(userId);//当前用户的所有购物车情况
        if(carts==null){
            logger.info("用户购物车为空");
            queryCartVos=null;
            return queryCartVos;
        }else {
            List<Object[]> objects=new ArrayList<Object[]>();
            Object book=null;
            for (Cart cart:carts){
                int bookId =cart.getBookId();//根据购物车中的bookId查询此book的详细信息
                book=bookRepository.findQueryCartVo(bookId);
                objects.add((Object[]) book);
            }
            try {
                queryCartVos=castEntity(objects,QueryCartVo.class);//结果集objects转换为自定义实体
            } catch (Exception e) {
                logger.info("购物车查询异常");
                e.printStackTrace();
            }
        }
        return queryCartVos;
    }

    /**
     * 查询个人订单
     * @param request
     * @return
     */
    public List<Order> findAllOrders(HttpServletRequest request){
        User user=null;
        user=userService.findOneByLoginUserMap(request);
        List<Order> orders=orderRepository.findAllByUserIdAndIsDeleted(user.getId(),0);
        if(orders==null){
            logger.info("用户订单为空");
            return null;
        }
        return orders;
    }

    /**
     * 删除订单
     * @param request
     * @param id
     * @return
     */
    public Integer deleteOrder(HttpServletRequest request,Integer id){
        User user=null;
        user=userService.findOneByLoginUserMap(request);
        Order order=new Order();
        order=orderRepository.findOrderById(id);
        order.setIsDeleted(1);
        order=orderRepository.save(order);
        if(order==null){
            return 0;
        }
        return 1;
    }
    /**
     * 数据库多表查询 返回的结果集Object  转换为自定义实体
     * @param list
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> castEntity(List<Object[]> list, Class<T> clazz) throws Exception {
        List<T> returnList=new ArrayList<T>();
        Object[] co=list.get(0);
        Class[] c2=new Class[co.length];
        //确定构造方法
        for (int i = 0; i < co.length; i++) {
            c2[i] = co[i].getClass();
        }

        for (Object[] o : list) {
            Constructor<T> constructor = clazz.getConstructor(c2);
            returnList.add(constructor.newInstance(o));
        }
        return returnList;

    }

    /**
     * 创建/更新 购物车
     * @param cart
     * @return
     */
    public boolean save(Cart cart){
        Cart newCart=null;
        newCart=cartRepository.save(cart);
        return newCart == null ? false : true;
    }
}
