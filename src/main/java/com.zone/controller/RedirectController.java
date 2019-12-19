package com.zone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName Controller
 * @Author zone
 * @Date 2019/1/5  18:37
 * @Version 1.0
 * @Description
 */
@Controller
public class RedirectController {
    @RequestMapping("/sentential/api")
    public String sentential(){
        return "sentential";
    }
    @RequestMapping("/sen-list/api")
    public String sententialList(){
        return "sen-list";
    }
    @RequestMapping("/sen-list3/api")
    public String sententialList3(){
        return "sen-list3";
    }
    @RequestMapping("/sen-list4/api")
    public String sententialList4(){
        return "sen-list4";
    }
    @RequestMapping("/sen-list5/api")
    public String sententialList5(){
        return "sen-list5";
    }
    @RequestMapping("/index")
    public String index(){
        return "index";
    }
    @RequestMapping("/listening")
    public String listening(){return "listening";}
    @RequestMapping("/addNewAddress")
    public String addNewAddress(){return "add-address";}
    @RequestMapping("/chongzhi")
    public String chongzhi(){return "cz";}
    @RequestMapping("/cart-page")
    public String cartPage(){return "cart-page";}
    @RequestMapping("/register")
    public String register(){return "register";}
    @RequestMapping("/resetPassword")
    public String resetPassword(){return "reset-password";}
}
