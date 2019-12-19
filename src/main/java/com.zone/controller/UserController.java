package com.zone.controller;

import com.zone.entity.User;
import com.zone.entity.UserAddress;
import com.zone.service.UserService;
import com.zone.util.file.QrCodeUtils;
import com.zone.util.response.Response;
import com.zone.util.response.ResponseCode;
import com.zone.util.response.ResponseUtil;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Logger;

/**
 * @ClassName UserController
 * @Author zone
 * @Date 2018/12/27  14:50
 * @Version 1.0
 * @Description
 */
@Controller
public class UserController {
    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    //日志前缀字符串,方便通过日志定位程序
    private static String logPrefix = null;

    @Autowired
    private UserService userService;

    /**
     * 用户名验证 （鼠标移开响应事件） 为了更好的用户体验
     * @param username
     * @return
     */
    @ResponseBody
    @RequestMapping("/usernameConfirm/api")
    public Response usernameConfirm(@RequestParam("username") String username){
        int code=userService.usernameConfirm(username);
        if(code==2){
            return ResponseUtil.error(ResponseCode.FAIL, "用户名格式不合法！");
        }else if(code==5){
            return ResponseUtil.error(ResponseCode.FAIL, "用户名已存在！");
        }else {
            return ResponseUtil.success("该用户名可用");
        }
    }

    /**
     * 邮箱验证
     * @param email
     * @return
     */
    @ResponseBody
    @RequestMapping("/emailConfirm/api")
    public Response emailConfirm(@RequestParam("email") String email){
        int code=userService.emailConfirm(email);
        if(code==4){
            return ResponseUtil.error(ResponseCode.FAIL, "邮箱格式不合法！");
        }else if(code==6){
            return ResponseUtil.error(ResponseCode.FAIL, "邮箱已存在！");
        }else {
            return ResponseUtil.success("该邮箱可用");
        }
    }

    /**
     * 电话验证
     * @param phone
     * @return
     */
    @ResponseBody
    @RequestMapping("/phoneConfirm/api")
    public Response phoneConfirm(@RequestParam("phone") String phone){
        int code=userService.phoneConfirm(phone);
        if(code==2){
            return ResponseUtil.error(ResponseCode.FAIL, "手机号格式不合法！");
        }else {
            return ResponseUtil.success("该手机号可用");
        }
    }
    /**
     * 用户注册
     *
     * @return com.zone.util.response.Response
     * @author zone
     * @date 2018/12/27 15:42
     * @params [username, password, emai]
     */

    @ResponseBody
    @RequestMapping("/register/api")
    public Response register(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam("email") String email,
                             @RequestParam("phone") String phone) {
        int state = userService.register(username, password, email,phone);
        if (state == 1) {
            return ResponseUtil.success("注册成功，请前往邮箱" + email + "激活账号");
        } else if (state == 2) {
            return ResponseUtil.error(ResponseCode.FAIL, "注册失败，用户名格式不合法！");
        } else if (state == 3) {
            return ResponseUtil.error(ResponseCode.FAIL, "注册失败，密码格式不合法！");
        } else if (state == 4) {
            return ResponseUtil.error(ResponseCode.FAIL, "注册失败，邮箱格式不合法！");
        } else if (state == 5) {
            return ResponseUtil.error(ResponseCode.FAIL, "注册失败，用户名已存在！");
        } else if (state == 6) {
            return ResponseUtil.error(ResponseCode.FAIL, "注册失败，邮箱已被注册！");
        }else if(state==7){
            return ResponseUtil.error(ResponseCode.FAIL, "注册失败，手机号格式错误！");
        }
        else {
            return ResponseUtil.error(ResponseCode.INTERNAL_SERVER_ERROR, "注册失败，原因未知，请及时联系管理员。");
        }
    }

    /**
     * 注册激活
     *
     * @param session
     * @param code
     * @return
     */
    @RequestMapping("/register-activate/api")
    public void registerActivate(HttpServletRequest request,
            HttpSession session,HttpServletResponse response,
            @RequestParam(value = "code", required = true) String code) {
        //获取当前请求的路径
        String basePath = request.getScheme() + "://" + request.getServerName() + ":"  + request.getServerPort()+request.getContextPath();
        int state = userService.registerActivate(session, code);
        if (state == 1) {
            try {
                response.sendRedirect("/onlineEnglishLearning/success-activate.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //return ResponseUtil.success("激活成功");
        } else {
            try {
                response.sendRedirect("/login.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //return ResponseUtil.error(ResponseCode.FAIL, "激活失败，不存在该用户或已激活");
        }
    }

    /**
     * 登陆
     *
     *  1.初次登陆[数据库查询 username|email + password]
     *  2.非初次登陆，带token登陆
     *  3.非初次登陆，带username|email登陆
     *  4.账户状态是否为1
     *
     *  登陆成功：返回 用户基本信息
     *  登陆失败：返回 空
     * @return
     */
    @ResponseBody
    @RequestMapping("/login/api")
    public Response login(HttpServletRequest request,
                          @RequestParam(value = "token", required = false) String token,
                          @RequestParam(value = "username", required = false) String username,
                          @RequestParam(value = "password") String password,
                          @RequestParam(value = "email", required = false) String email) {
        logPrefix = "[UserController.login] ";
        String sessionId = request.getSession().getId();
        if(userService.loginCheck(request)==1){//已登录过,并刷新活跃时间
            User user=null;
            user=userService.findOneByLoginUserMap(request);
            if(user!=null){
                user.setSessionId(sessionId);
                return ResponseUtil.success("已登陆",user);
            }
        }
        User user=null;
        user=userService.login(request.getSession(),username,password,email);
        if(user!=null){
            if(user.getAccountState()!=1){
                logger.info(logPrefix+"该帐户未被激活");
                return ResponseUtil.error(ResponseCode.FAIL,"该帐户未被激活");
            }
            user.setSessionId(sessionId);
            return  ResponseUtil.success("登陆成功!", user);
        }else {
            logger.info(logPrefix+"用户名或密码错误");
            return ResponseUtil.error(ResponseCode.USERNAME_ERROR_OR_PASSWORD_ERROR,"用户名或密码错误");
        }
    }

    /**
     * 更新用户头像
     * @param request
     * <@param> token
     *          登陆校验过滤器校验登陆时，由于上传文件的form-data形式无法读取token；
     *          过滤器会从header中获取token；
     *          也即提醒：本方法的特殊处在于，获取token需要从header中获取
     * @param logoFile
     * */
    @ResponseBody
    @PostMapping(value = "/updateUserLogo/api")
    public Response updateUserLogo(HttpServletRequest request,
                                 @RequestParam(value = "logo") MultipartFile logoFile
    ){
        String filePath= "D:/ssmCode/online_english_learning/logo/";
//        try {
//            filePath = ResourceUtils.getURL("classpath:").getPath().toString();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        System.out.println("filePath======="+filePath);
        int code = userService.updateUserLogoUrl(request,logoFile, filePath);
        switch (code){
            case 1:
                return ResponseUtil.success("上传头像成功！");
            case -1:
                return ResponseUtil.error(ResponseCode.FAIL,"文件类型为空，请重新选择");
            case -2:
                return ResponseUtil.error(ResponseCode.FAIL,"文件类型不符合，请重新选择");
            default:
                return ResponseUtil.error(ResponseCode.FAIL,"上传失败，原因未知");
        }
        //return ResultUtil.error(ResultCode.FAIL, "[UserController.updateUserLogo] 接口暂未开发");
    }

    /**
     * 更新个人信息（用户名|个性签名）
     * @param request
     * @param username
     * @param introduce
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateUserInfo/api")
    public Response updateUserInfo(HttpServletRequest request,
                                   @RequestParam(value = "username")String username,
                                   @RequestParam(value = "introduce")String introduce){
        int code=userService.updateUserInfo(request,username,introduce);
        logPrefix = "[UserController.updateUserInfo] ";
        if(code==0){
            logger.info("用户名和个性签名为空");
            return ResponseUtil.error(ResponseCode.FAIL,"用户名和个性签名为空");
        }else if(code==1){
            logger.info("修改个性签名成功");
            return ResponseUtil.error(ResponseCode.FAIL,"用户名不能为空");
        }else if(code==2){
            logger.info("用户名已存在，修改失败");
            return ResponseUtil.error(ResponseCode.FAIL,"用户名已存在，修改失败");
        }else if(code==3){
            logger.info("修改签名成功");
            return ResponseUtil.success("修改签名成功");
        }else if(code==4){
            logger.info("修改用户名成功");
            return ResponseUtil.success("修改用户名成功");
        }else if(code==5){
            logger.info("修改用户名或签名成功");
            return ResponseUtil.success("保存成功");
        }
        else {
            logger.info("程序发现未知异常");
            return ResponseUtil.error(ResponseCode.FAIL,"程序发现未知异常");
        }
    }
    @ResponseBody
    @RequestMapping("/addNewAddress/api")
    public Response addNewAddress(HttpServletRequest request,
                                  @RequestParam(value = "recipient")String recipient,
                                  @RequestParam(value = "recipientPhone")String recipientphone,
                                  @RequestParam(value = "address")String address){
        int code=userService.addNewAddress(request,recipient,recipientphone,address);
        if(code==1){
            return ResponseUtil.success("新增地址成功");
        }else if(code==2){
            return ResponseUtil.error(ResponseCode.FAIL,"收货信息（收货人、收货电话、收货地址）不能为空");
        }else {
            return ResponseUtil.error(ResponseCode.FAIL,"数据库异常");
        }
    }
    @RequestMapping("/chongzhi/api")
    public void chongzhi(HttpServletRequest request,HttpServletResponse response){
        String qrcodeUrl = request.getParameter("qrcodeUrl");
        String content="您已成功充值"+qrcodeUrl+"元";
        qrcodeUrl="http://5088618.nat123.cc/onlineEnglishLearning/cz-confirm.html?content="+content+"&po="+qrcodeUrl;
        OutputStream oStream = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //生成二维码
            QrCodeUtils.generate(qrcodeUrl, baos);
            byte[] bytes = baos.toByteArray();
            oStream = response.getOutputStream();
            oStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //当创建对象成功时候，在执行close()方法。
            if(oStream!=null){
                try {
                    oStream.close();
                } catch (IOException e) {
                    try {
                        oStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
        }
    }
    @ResponseBody
    @RequestMapping("/chongzhiConfirm/api")
    public Response chongzhiConfirm(HttpServletRequest request,
                                    @RequestParam(value = "points")String points){
        int code=userService.chongzhi(request,points);
        System.out.println("points---===="+points);
        if(code==1){
            return ResponseUtil.success("充值成功");
        }else if (code==2){
            return ResponseUtil.error(ResponseCode.FAIL,"充值失败（原因：充值不能为0）");
        }else {
            return ResponseUtil.error(ResponseCode.FAIL,"充值失败（原因：充值金额不能为负数）");
        }
    }

    /* 退出登陆 */
    @RequestMapping(value = "/exitLogin/api")
    @ResponseBody
    public Response exitLogin(HttpServletRequest request,
                            @RequestParam(value = "token") String token){
        int handle = userService.exitLogin(request,token);
        if(handle == 1){
            return ResponseUtil.success("退出成功!");
        } else {
            return ResponseUtil.error(ResponseCode.FAIL, "未曾登陆，退出失败.");
        }
    }
    /**
     * 重置密码
     * @param request
     * @param token
     * @param oldPswd
     * @param newPswd
     * @return
     */
    @ResponseBody
    @RequestMapping("/resetPassword/api")
    public Response resetPassword(HttpServletRequest request,
                                  @RequestParam(value = "token")String token,
                                  @RequestParam(value = "oldPswd")String oldPswd,
                                  @RequestParam(value = "newPswd")String newPswd){
        int result = userService.resetPassword(request,token,oldPswd,newPswd);
        switch (result){
            case 1:
                return ResponseUtil.success("重置密码成功！");
            case 2:
                return ResponseUtil.error(ResponseCode.NOT_LOGIN_NO_ACCESS,"重置密码失败,原因：未登录,无权限");
            case 3:
                return ResponseUtil.error(ResponseCode.FAIL,"密码长度(6至18位数)不符合要求，重置失败。");
            case 4:
                return ResponseUtil.error(ResponseCode.FAIL,"原密码输入错误，重置失败");
            case 5:
                return ResponseUtil.error(ResponseCode.FAIL,"新密码与原密码一致，重置失败");
                default:
                    return ResponseUtil.error(ResponseCode.FAIL,"重置失败，原因未知");
        }
    }
    @RequestMapping("/findSelfInfo/api")
    public String findSelfInfo(ModelMap modelMap,HttpServletRequest request){
        User user=userService.findOneByLoginUserMap(request);
        List<UserAddress> userAddress=userService.findUserAddress(request);
        if(user!=null){
            modelMap.addAttribute("user",user);
            modelMap.addAttribute("userAddress",userAddress);
        }
        return "self-info";
    }
}
