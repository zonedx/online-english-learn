package com.zone.util.code;

import com.zone.entity.User;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName FormatUtil
 * @Author zone
 * @Date 2018/12/27  9:17
 * @Version 1.0
 * @Description
 */
public class FormatUtil {
    private static final Logger logger = Logger.getLogger(User.class.getName());
    //日志前缀字符串,方便通过日志定位程序
    private static String logPrefix = null;

    /**
     * 判断用户名是否合法
     * 1.长度3-18位
     * 2.只包括数字、字母、中文
     */
    public static int isLegalUsername(String username) {
        logPrefix = "[FormatUtil.isLegalUsername]";
        if (username == null) {
            logger.info(logPrefix + ":" + username + " is null,illegal！");
            return 2;
        } else if (username.length() < 3 || username.length() > 18) {
            logger.info(logPrefix + ":" + username + "'s length is not in [3,18],illegal! ");
            return 3;
        } else if (!isUsernameChar(username)) {
            logger.info(logPrefix + ":" + username + " is contain other character,illegal! ");
            return 4;
        }
        return 1;//合法
    }

    /**
     * 判断密码是否合法
     * 1.长度6-18
     * 2.只包含数字、字母、下划线、星号
     *
     * @param password
     * @return
     */
    public static int isLegalPassword(String password) {
        logPrefix = "[FormatUtil.isLegalPassword]";
        if (password == null) {
            logger.info(logPrefix + ":" + password + " is null,illegal! ");
            return 2;
        } else if (password.length() < 6 || password.length() > 18) {
            logger.info(logPrefix + ":" + password + "'s length is not in [6,18],illegal! ");
            return 3;
        } else if (!isPasswordChar(password)) {
            logger.info(logPrefix + ":" + password + " is contain other character,illegal! ");
            return 4;
        }
        return 1;
    }

    /**
     * 是否是中国的合法手机号
     *  @author yuongxi
     *  @reference https://blog.csdn.net/m18860232520/article/details/79396889
     *      中国电信号段 133、149、153、173、177、180、181、189、199
     *      中国联通号段 130、131、132、145、155、156、166、175、176、185、186
     *      中国移动号段 134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188、198
     *      其他号段
     *      14号段以前为上网卡专属号段，如中国联通的是145，中国移动的是147等等。
     *      虚拟运营商
     *          电信：1700、1701、1702
     *          移动：1703、1705、1706
     *          联通：1704、1707、1708、1709、171
     *          卫星通信：1349
     * @param phone
     */
    public static boolean isLegalPhone(String phone){
        logPrefix= "[FormatUtil.isLegalPhone]";
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if(phone==null){
            logger.info(logPrefix + ":" + phone + " is null,illegal! ");
            return false;
        }
        if (phone.length() != 11) {
            logger.info(logPrefix + phone+"'s length is not 11,illegal! ");
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            if (!isMatch) {//格式错误
                logger.info(logPrefix + "In China, this phone number's format is error!");
            }
            return isMatch;
        }
    }
    /**
     * 判断邮箱是否合法
     * 1.长度7-50位
     * 2.邮箱格式
     *
     * @param emali
     * @return
     */
    public static int isLegalEmail(String emali) {
        logPrefix = "[FormatUtil.isLegalEmail]";
        if (emali == null) {
            logger.info(logPrefix + ":" + emali + " is null,illegal! ");
            return 2;
        } else if (emali.length()<7||emali.length()>50){
            logger.info(logPrefix + ":" + emali + "'s length is not in [7,50],illegal! ");
            return 3;
        }else if (!isEmaliChar(emali)){
            logger.info(logPrefix + ":" + emali + " is not mail format,illegal! ");
            return 4;
        }
        return 1;
    }

    /**
     * 判断字符串中是否仅包含字母数字和汉字
     * 汉字：[0x4e00,0x9fa5]（或十进制[19968,40869]）
     * 数字：[0x30,0x39]（或十进制[48, 57]）
     * 小写字母：[0x61,0x7a]（或十进制[97, 122]）
     * 大写字母：[0x41,0x5a]（或十进制[65, 90]）
     *
     * @param str
     */
    public static boolean isUsernameChar(String str) {
        return str.matches("^[a-z0-9A-Z\\u4e00-\\u9fa5]+$");
    }

    public static boolean isPasswordChar(String str) {
        return str.matches("^[a-zA-Z0-9_*]+$");
    }

    //参考正则：^\w+@(\w+\.)+\w+$
    public static boolean isEmaliChar(String str) {
        return str.matches("^\\w+@(\\w+\\.)+\\w+$");
    }
}
