package com.zone.controller;

import com.zone.entity.Book;
import com.zone.entity.BookPicture;
import com.zone.entity.Order;
import com.zone.entity.UserAddress;
import com.zone.entity.vo.BookVo;
import com.zone.entity.vo.OrderVo;
import com.zone.entity.vo.QueryCartVo;
import com.zone.service.BookService;
import com.zone.service.UserService;
import com.zone.util.response.Response;
import com.zone.util.response.ResponseCode;
import com.zone.util.response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @ClassName BookController
 * @Author zone
 * @Date 2019/3/7  15:34
 * @Version 1.0
 * @Description
 */
@Controller
public class BookController {
    private static final Logger logger = Logger.getLogger(UserService.class.getName());
    //日志前缀字符串,方便通过日志定位程序
    private static String logPrefix = null;
    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;

    @RequestMapping("/findAllBooks/api")
    public String findAllBooks(ModelMap modelMap) {
        List<BookVo> bookVos = bookService.findAllBooks();
        modelMap.addAttribute("datas",bookVos);
        return "shopping";
    }
    @RequestMapping("/findBooksByCondition/api")
    public String findBooksByCondition(ModelMap modelMap,
                                       @RequestParam(value = "page",required = false,defaultValue = "0")Integer page,
                                       @RequestParam(value = "condition",required = false,defaultValue = "5") Integer condition,
                                       @RequestParam(value="name",required = false)String name){
        Page<Book> books=bookService.findAllBooksByCodition(page,condition,name);
        modelMap.addAttribute("datas",books);
        modelMap.addAttribute("condition",condition);
        return "shopping";
    }
    @ResponseBody
    @RequestMapping("/addToCart/api")
    public Response addToCart(HttpServletRequest request, @RequestParam(value = "bookId") Integer bookId,
                              @RequestParam(value = "amount",required = false,defaultValue = "1") Integer amount) {
        if (bookService.addToCart(request, bookId, amount)) {
            return ResponseUtil.success("加入购物车成功");
        } else {
            return ResponseUtil.error(ResponseCode.FAIL, "加入购物车失败，购物车中已存在此图书");
        }
    }
    @ResponseBody
    @RequestMapping("/purchaseBooks/api")
    public Response purchaseBooks(HttpServletRequest request,
                                  @RequestParam(value = "addressId")Integer addressId,
                                  @RequestParam(value = "bookId")Integer bookId,
                                  @RequestParam(value = "amount")Integer amount){
        List<OrderVo> orderVos=bookService.purchaseBooks(request,bookId,amount,addressId);
        if(orderVos==null){
            return ResponseUtil.error(ResponseCode.FAIL,"购买失败");
        }
        return ResponseUtil.success("购买成功",orderVos);
    }
    @ResponseBody
    @RequestMapping("/testOrder/api")
    public Response testOrder(@RequestParam(value = "id")Integer id){
        return ResponseUtil.success(bookService.testOrder(id));
    }

    @ResponseBody
    @RequestMapping("/removeCart/api")
    public Response removeCart(HttpServletRequest request,@RequestParam(value = "id")Integer id){
        bookService.removeCart(id);
        return ResponseUtil.success("删除商品成功");
    }

    @RequestMapping("/findAllCarts/api")
    public String findAllCarts( HttpServletRequest request,ModelMap modelMap){
        List<QueryCartVo> queryCaryVos=bookService.findAllCart(request);
        modelMap.addAttribute("datas",queryCaryVos);
        return "cart";
//        if(queryCaryVos!=null){
//            return ResponseUtil.success("查询成功",queryCaryVos);
//        }
//        return ResponseUtil.error(ResponseCode.FAIL,"当前用户购物车为空");
    }

    @RequestMapping("/findBookDetail/api")
    public String findBookDetail(HttpServletRequest request,ModelMap modelMap,@RequestParam(value = "id")Integer id){
        Book book=bookService.findBookDetail(request,id);
        List<BookPicture> bookPictures=bookService.findBookDetailPic(id);
        modelMap.addAttribute("datas",book);
        modelMap.addAttribute("datas2",bookPictures);
        return "book-details";
    }

    @RequestMapping("/findAllOrders/api")
    public String  findAllOrders(HttpServletRequest request,ModelMap modelMap){
        List<Order> orders=bookService.findAllOrders(request);
        modelMap.addAttribute("datas",orders);
        return "order";
    }

    @ResponseBody
    @RequestMapping("/deleteOrder/api")
    public Response deleteOrder(HttpServletRequest request,@RequestParam(value = "id")Integer id){
        Integer isDeleted=bookService.deleteOrder(request,id);
        if(isDeleted==1){
            return ResponseUtil.success("删除订单成功");
        }else {
            return ResponseUtil.error(ResponseCode.FAIL,"删除订单失败");
        }
    }
    @RequestMapping("/confirmOrder/api")
    public String confirmOrder(HttpServletRequest request, ModelMap modelMap,
                                  @RequestParam(value = "id")Integer id,
                                  @RequestParam(value = "picturePlace")String picturePlace,
                                  @RequestParam(value = "name")String name,
                                  @RequestParam(value ="price")Float price,
                                  @RequestParam(value = "amount")Integer amount){
        List<UserAddress> userAddress=userService.findUserAddress(request);
        OrderVo orderVo=new OrderVo();
        orderVo.setBookImage(picturePlace);
        orderVo.setBookName(name);
        orderVo.setPrice(price);
        orderVo.setSumPrice(price*amount);
        modelMap.addAttribute("id",id);
        modelMap.addAttribute("userAddress",userAddress);
        modelMap.addAttribute("orderVo",orderVo);
        return "confirm-order";
    }
}
