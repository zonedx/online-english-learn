package com.zone.service;

import com.zone.dao.UserAddressRepository;
import com.zone.dao.UserRepository;
import com.zone.entity.User;
import com.zone.entity.UserAddress;
import com.zone.util.code.CodeUtil;
import com.zone.util.code.FormatUtil;
import com.zone.util.file.FileUtil;
import com.zone.util.mail.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @ClassName UserService
 * @Author zone
 * @Date 2018/12/27  12:29
 * @Version 1.0
 * @Description
 */
@Service("userService")
public class UserService {

    public static final Logger logger = Logger.getLogger(UserService.class.getName());
    //日志前缀字符串,方便通过日志定位程序
    private static String logPrefix = null;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailUtil mailUtil;
    @Autowired
    private UserAddressRepository userAddressRepository;

    /**
     * 用户注册
     * 1.格式判断
     * 返回2--用户名格式不正确
     * 返回3--密码格式不正确
     * 返回4--邮箱格式不正确
     * 2.判断该用户名是否存在
     * 返回5--用户名已存在
     * 3.判断邮箱是否被使用
     * 返回6--邮箱已被注册
     *
     * @return
     * @author zone
     * @params
     */
    public int register(String username, String password, String email,String phone) {
        logPrefix = "[UserService.register]";
        if (FormatUtil.isLegalUsername(username.trim()) != 1) {
            return 2;
        }
        if (FormatUtil.isLegalPassword(password.trim()) != 1) {
            return 3;
        }
        if (FormatUtil.isLegalEmail(email.trim()) != 1) {
            return 4;
        }
        //判断用户名和邮箱是否存在
        if (userRepository.isExistUsername(username) > 0) {
            logger.info(logPrefix + ":username:" + username + " is exist,register failed!");
            return 5;
        }
        if (userRepository.isExistEmali(email) > 0) {
            logger.info(logPrefix + ":email:" + email + " is exist,register failed!");
            return 6;
        }
        //判断手机号是否合法
        if(!FormatUtil.isLegalPhone(phone)){
            return 7;
        }
        //通过限制条件  存储用户初始信息
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhone(phone);
        user.setHeadImage("../img/mrtx.jpg");
        user.setPoints(0);
        user.setAccountState(0);//设置初始默认值0
        String code = CodeUtil.getUUID();
        user.setActivateCode(code);
        logger.info(logPrefix + "registering  user info:" + user.toString());
        userRepository.save(user);//保存到数据库
        mailUtil.setCode(code);
        mailUtil.setReciverEmail(email);
        new Thread(mailUtil).start();//保存成功则通过线程的方式给用户发送一封邮件
        logger.warning(logPrefix + "用户(username:" + user.getUsername() + " email:" + user.getEmail() + ")注册中...激活邮件已发送");
        return 1;
    }

    public int usernameConfirm(String username){
        if (FormatUtil.isLegalUsername(username.trim()) != 1) {
            return 2;
        }
        if (userRepository.isExistUsername(username) > 0) {
            return 5;
        }
        return 1;
    }
    public int emailConfirm(String email){
        if (FormatUtil.isLegalEmail(email.trim()) != 1) {
            return 4;
        }
        if (userRepository.isExistEmali(email) > 0) {
            logger.info(logPrefix + ":email:" + email + " is exist,register failed!");
            return 6;
        }
        return 1;
    }
    public int phoneConfirm(String phone){
        if(!FormatUtil.isLegalPhone(phone)){
            return 2;//不合法
        }
        return 1;//合法
    }
    /**
     * 注册激活
     *
     * @param session
     * @param code
     * @return
     */
    public int registerActivate(HttpSession session, String code) {
        User user = null;
        user = userRepository.findDistinctByActivateCode(code);
        if (user != null) {
            session.setAttribute("register_user_email", user.getEmail());
            //设定用户为激活状态
            user.setAccountState(1);
            user.setActivateCode("");
            if (this.save(user)) {
                logger.info("账号邮箱：" + user.getEmail() + "激活成功！");
                return 1;
            } else {
                String message = "账号邮箱：" + user.getEmail() + "激活成功，但更新用户信息故障！";
                logger.warning(message);
                return 0;
            }
        } else {
            logger.info("不存在该用户或已激活成功！");
            return -1;
        }
    }

    /**
     * 用户登录
     *
     * @param session
     * @param username
     * @param password
     * @param email
     * @return
     */
    public User login(HttpSession session, String username, String password, String email) {
        logPrefix = "[UserService.login]";
        User user = null;
        user = userRepository.findByUsernameAndPassword(username, password);
        if (user == null) {
            user = userRepository.findByEmailAndPassword(email, password);
        }
        System.out.println("username-----"+username+"password---"+password+"email---"+email);
        if (user != null) {
            if (user.getAccountState() != 1) {
                logger.info("该帐户：" + user.getUsername() + "||" + user.getEmail() + "未激活！");
                //return null;
            }
            Map<String, User> map = null;
            map = (Map<String, User>) session.getAttribute("loginUserMap");
            if (map == null) {
                map = new HashMap<String, User>();
            }
            //以username(可能为null)+email(可能为null)和当前的登录时间生成token
            Calendar loginDatetime = Calendar.getInstance();
            user.setLastActiveDateTime(loginDatetime);
            String str = user.getUsername() + user.getEmail();
            String token = CodeUtil.MD5(str + Long.toString(loginDatetime.getTimeInMillis()));
            user.setToken(token);
            logger.info(logPrefix + "user:" + user.toString());
            map.put(token, user);
            session.setAttribute("loginUserMap", map);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("token", user.getToken());
            logger.info(logPrefix + "用户(username:" + user.getUsername() + " email:" + user.getEmail() + ")登陆成功！");
            return user;
        }
        logger.info(logPrefix + "用户登陆失败（用户名或密码错误）！");
        return null;
    }

    /**
     * 登陆检查
     * [注意：仅通过请求参数token判断用户是否登陆]
     * [默认：登陆有效核验时间为30分钟]
     * 返回1--登陆成功且有效
     * 1.loginUsersMap不存在,登陆会话用户不存在，返回2
     * 2.请求参数token不存在，返回3
     * 3.该会话有登陆用户，但token无效（过时），返回4
     * 4.该帐户尚未激活，返回5
     *
     * @param request
     * @return
     */
    public int loginCheck(HttpServletRequest request) {
        logPrefix = "[UserService.loginCheck]";
        HttpSession session = request.getSession();
        User user = null;
        Map<String, User> userMap = null;
        userMap = (Map<String, User>) session.getAttribute("loginUserMap");
        if (userMap == null) {
            logger.info(logPrefix + "登陆失败，loginUserMap不存在，说明未曾登陆");
            return 2;
        }
        //从Parameter||Header中获取token
        String requestToken = request.getParameter("token");
        if (requestToken == null) {
            requestToken = request.getHeader("token");
        }
        if (requestToken == null) {
            logger.info("登陆失败，请求参数不全[token]");
            return 3;
        }
        //以token为key，在loginUserMap中查找用户user
        user = userMap.get(requestToken);
        if (user == null) {
            logger.info("登陆失败，该会话有登陆用户，但当前token(" + requestToken + ")无效。");
            return 4;
        }
        //查看该用户状态是否被激活
        if (user.getAccountState() != 1) {
            logger.info("登陆失败，该会话有登陆用户，token(" + requestToken + ")有效，但账户未激活或者被锁定。");
            return 5;
        }

        //计算+校验用户活跃时间是否有效
        Calendar lastActivateDateTime = user.getLastActiveDateTime();
        Long timeDifference = (Calendar.getInstance().getTimeInMillis() - lastActivateDateTime.getTimeInMillis()) / (60 * 1000);
        int validSeconds = 30;//登陆的有效分钟数
        if (timeDifference > validSeconds) {
            logger.info(logPrefix + "登陆失败，用户" + user.getUsername() + "登陆Token(" + requestToken + ")有效,但时间超时失效。");
            return 6;
        }

        //用户登陆完全有效：刷新用户的最近活跃时间戳+重新存入loginUserMap
        user = flushLastActiveDateTime(user);
        userMap.put(requestToken, user);
        session.setAttribute("token", user.getToken());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("loginUserMap", userMap);
        logger.info(logPrefix + "用户" + user.getUsername() + "(token:" + requestToken + ")登陆成功且时间有效!");
        return 1;

    }

    /**
     * 更新用户头像
     *
     * @param request
     * @param file
     * @param filePath
     */
    public int updateUserLogoUrl(HttpServletRequest request, MultipartFile file, String filePath) {
        logPrefix = "[UserService.updateUserLogoUrl] ";
        User user = null;
        user = this.findOneByLoginUserMap(request);
        String fileName = file.getOriginalFilename();
        String realFileName = null;
        String type = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        if (type != null) {
            if ("GIF".equals(type.toUpperCase()) || "PNG".equals(type.toUpperCase()) ||
                    "JPG".equals(type.toUpperCase())) {
                realFileName = String.valueOf(System.currentTimeMillis()) + fileName;
                try {
                    FileUtil.uploadFile(file.getBytes(), filePath, realFileName);
                    user.setHeadImage("../static/img/" + realFileName);
                    logger.info(logPrefix + " user's logo real path:" + filePath + realFileName);
                    userRepository.save(user);
                    return 1;
                } catch (Exception e) {
                    System.out.println("文件上传错误");
                    e.printStackTrace();
                }
                return 1;//图片上传成功
            } else {
                return -2;//文件类型不符合
            }
        } else {
            return -1;//文件类型为空
        }
    }

    /**
     * 更新个人信息
     * 仅可以修改 （用户名|个性签名）
     *
     * @param request
     * @param username
     * @param introduce
     * @return
     */
    public int updateUserInfo(HttpServletRequest request, String username, String introduce) {
        User user = null;
        if (username == "" && introduce == ""){//都不修改
            return 0;//fail
        }
        if(username==null||username==""){
            return 1;
//            if(introduce!=null){
//                user=this.findOneByLoginUserMap(request);
//                user.setIntroduce(introduce);
//                userRepository.save(user);
//                return 1;//修改个性签名
//            }
        }else {//username!=null
            user=this.findOneByLoginUserMap(request);
            if(userRepository.isExistUsername(username)>0){//已经存在该username
                if(!username.equals(user.getUsername())){
                    return 2;//fail
                }
                if(!introduce.equals(user.getIntroduce())){
                    user.setIntroduce(introduce);
                    userRepository.save(user);
                    return 3;//只修改签名
                }
                return 5;
            }
            user.setUsername(username);
            if(introduce.equals(user.getIntroduce())){
                userRepository.save(user);
                return 4;//仅修改用户名
            }
            user.setIntroduce(introduce);
            userRepository.save(user);
            return 5;//都修改
        }
        //return -1;//未知异常
    }

    /**
     * 积分充值
     * @param request
     * @param points
     * @return
     */
    public int chongzhi(HttpServletRequest request,String points){
        User user=this.findOneByLoginUserMap(request);
        if(points==""){
            return 2;//充值失败（原因：充值不能为0）
        }
        int po=Integer.valueOf(points);
        if(po<0){
            return 3;//充值失败（原因：充值金额不能为负数）
        }
        user.setPoints(user.getPoints()+po);
        userRepository.save(user);
        return 1;
    }
    /**
     * 重置密码
     * 1.判断新旧密码是否一致
     * 2.判断原密码是否正确
     * 3.判断新密码长度是否正确
     *
     * @param request
     * @param token
     * @param oldPswd
     * @param newPswd
     * @return
     */
    public int resetPassword(HttpServletRequest request, String token, String oldPswd, String newPswd) {
        logPrefix = "[UserService.resetPassword]";
        if (!oldPswd.equals(newPswd)) {//新旧密码不一致时，才可更改
            User user = null;
            user = this.findOneByLoginUserMap(request);
            if (oldPswd.equals(user.getPassword())) {//原密码输入正确方可继续
                if (newPswd.length() <= 18 && newPswd.length() > 5) {//新密码长度[6,18]
                    user.setPassword(newPswd);
                    int result = this.saveToSession(request.getSession(), user, token);//更新Session登陆用户信息

                    if (result == 1) {
                        this.save(user); // 保存到数据库中
                        logger.info(logPrefix + "用户" + user.getUsername() + "重置密码成功！");
                        return 1;//重置密码成功！
                    } else {
                        logger.info(logPrefix + "用户" + user.getUsername() + "重置密码失败，不存在loginUserMap！");
                        return 2;
                    }
                } else {
                    logger.info(logPrefix + "密码长度(6至18位数)不符合要求，重置失败。");
                    return 3;
                }
            } else {
                logger.info(logPrefix + "原密码输入错误，重置失败。");
                return 4;
            }
        } else {
            logger.info(logPrefix + "新密码与原密码一致，重置失败。");
            return 5;
        }
    }
    /**
     * 退出登陆
     * @param request
     * @param loginToken
     */
    public int exitLogin( HttpServletRequest request, String loginToken){
        logPrefix = "[UserService.exitLogin()] ";
        HttpSession session = null;
        session = request.getSession();
        Map<String, User> users = null;
        users = (Map<String, User>) session.getAttribute("loginUserMap");
        if(users == null){
            logger.warning(logPrefix + "未曾登陆，退出失败。(loginUserMap不存在)");
            return -1;
        }
        User user = null;
        user = users.remove(loginToken);//移除当前会话中登陆的该用户[注：可能还存在其他登陆用户]
        if(user == null){
            logger.warning(logPrefix + "未曾登陆，退出失败。(loginUserMap不存在该用户)");
            return -2;
        }
        session.setAttribute("username","登陆");
        session.setAttribute("loginUserMap", users);
        System.out.println("用户(username:" + user.getUsername() + " email:"+user.getEmail());
        logger.warning(logPrefix + "用户(username:" + user.getUsername() + " email:"+user.getEmail() + ")退出成功！");
        return 1;
    }
    /**
     * 新增收货地址
     *
     * @param request
     * @param recipient
     * @param recipientPhone
     * @param address
     * @return
     */
    public Integer addNewAddress(HttpServletRequest request, String recipient, String recipientPhone, String address) {
        logPrefix = "[UserService.addNewAddress]";
        User user = this.findOneByLoginUserMap(request);
        UserAddress userAddress = new UserAddress();
        Integer userId = user.getId();
        if (recipient != "" && recipientPhone != "" && address != "") {
            userAddress.setUserId(userId);
            userAddress.setRecipient(recipient);
            userAddress.setRecipientPhone(recipientPhone);
            userAddress.setAddress(address);
            UserAddress u = userAddressRepository.save(userAddress);
            if (u == null) {
                logger.info(logPrefix + "插入地址失败，数据库错误");
                return 3;
            }
            return 1;//新增收货地址成功
        } else {
            logger.info(logPrefix + "收货信息（收货人、收货电话、收货地址）不能为空");
            return 2;
        }
    }

    /**
     * 获取用户收货地址
     *
     * @param request
     * @return
     */
    public List<UserAddress> findUserAddress(HttpServletRequest request) {
        User user = this.findOneByLoginUserMap(request);
        List<UserAddress> userAddresses = userAddressRepository.findAllByUserId(user.getId());
        return userAddresses;
    }

    /**
     * 通过session.loginUsersMap[username|email|token] + request.[username|email] 获取用户信息
     * 注：不再负责校验是否登陆过
     * <p>
     * 返回null或者user
     *
     * @param request
     **/
    public User findOneByLoginUserMap(HttpServletRequest request) {
        logPrefix = "[UserService.findOneByLoginUsersMap] ";
        Map<String, User> userMap = null;
        userMap = (Map<String, User>) request.getSession().getAttribute("loginUserMap");
        if (userMap != null) {
            User user = null;
            String token = null;
            token = request.getParameter("token");
            if (token == null) {
                token = request.getHeader("token");
                logger.info(logPrefix + "token:" + token + " <from HttpServletRequest.Header>");
            }
            if (token != null) {
                user = userMap.get(token);
                if (user != null) {
                    return user;
                }
            }
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            if (username != null) {
                for (User item : userMap.values()) {
                    if (item.getUsername().equals(username)) {
                        return item;
                    }
                }
            }
            if (email != null) {
                for (User item : userMap.values()) {
                    if (item.getEmail().equals(email)) {
                        return item;
                    }
                }
            }
        }
        return null; //username|email|token 均无user
    }

    /**
     * 刷新登陆用户的最新活跃时间
     * 保障用户能够维持有效登陆
     *
     * @param user
     */
    public User flushLastActiveDateTime(User user) {
        logPrefix = "[UserService.flushLastActiveDateTime] ";
        user.setLastActiveDateTime(Calendar.getInstance());//将活跃时间刷新为当前时刻
        logger.info(logPrefix + "已刷新用户(" + user.getUsername() + ")的活跃时间戳!");
        return user;
    }

    /**
     * 更新或者保存登陆用户信息到session.loginUsersMap 中
     *
     * @param session
     * @param user
     * @param loginToken
     * @return
     */
    public int saveToSession(HttpSession session, User user, String loginToken) {
        logPrefix = "[UserService.saveToSession]";
        Map<String, User> userMap = null;
        userMap = (Map<String, User>) session.getAttribute("loginUserMap");
        if (userMap != null) {//存在loginUserMap对象
            User oldUser = null;
            oldUser = userMap.remove(loginToken);
            //更新登陆用户信息
            userMap.put(loginToken, user);
            session.setAttribute("loginUserMap", userMap);

            if (oldUser != null) {//exists this logined user in session
                logger.info(logPrefix + "更新登陆用户信息成功，session中已存在该用户" + user.getUsername());
            } else {
                logger.info(logPrefix + "新增登陆用户信息成功，session中不存在该用户" + user.getUsername());
            }
            return 1;
        } else {
            logger.info(logPrefix + "session中不存在 loginUsersMap 对象，说明根本未登录，故无权限保存登录用户！");
            return -1; //session中不存在 loginUsersMap对象
        }
    }

    /**
     * 创建新用户或者更新用户
     *
     * @param user
     **/
    public boolean save(User user) {
        User newUser = null;
        newUser = userRepository.save(user);
        return newUser == null ? false : true;
    }
}
