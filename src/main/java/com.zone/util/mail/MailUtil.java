package com.zone.util.mail;

import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class MailUtil implements Runnable{

    @Autowired
    private MailProperties mailProperties;

    private String reciverEmail=null;//收件人邮箱

    private String code=null;//激活码

    public void setReciverEmail(String reciverEmail) {
        this.reciverEmail = reciverEmail;
    }

    public MailProperties getMailProperties() {
        return mailProperties;
    }

    public String getReciverEmail() {
        return reciverEmail;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public MailUtil() {
    }

    public MailUtil(String reciverEmail, String code) {
        this.reciverEmail = reciverEmail;
        this.code = code;
    }

    @Override
    public void run() {
        String sender=mailProperties.getSenderEmail();//发件人邮箱
        String host=mailProperties.getHost();//指定发送邮件的主机smtp.qq.com(QQ)|smtp.163.com(网易)

        Properties properties = System.getProperties();// 获取系统属性
        properties.setProperty("mail.smtp.host", host);// 设置邮件服务器
        properties.setProperty("mail.smtp.auth", "true");// 打开认证

        //QQ邮箱需要下面这段代码，163邮箱不需要
        try {
            //QQ邮箱需要下面这段代码，163邮箱不需要
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);

            // 1.获取默认session对象
            Session session =
                    Session.getDefaultInstance(properties, new Authenticator() {
                        public PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(
                                    mailProperties.getSenderEmail(),
                                    mailProperties.getAuthCode()); // 发件人邮箱账号、授权码
                        }
                    });

            // 2.创建邮件对象
            Message message = new MimeMessage(session);
            // 2.1设置发件人
            message.setFrom(new InternetAddress(sender));
            // 2.2设置接收人
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(reciverEmail));
            // 2.3设置邮件主题
            String contextPath = mailProperties.getContextPath();
            if(contextPath.startsWith("/")){//判断context-path有无/符去，如果有统一除/
                mailProperties.setContextPath(contextPath.substring(1));
            }

            message.setSubject(mailProperties.getSubject());
            // 2.4设置邮件内容
            String actionPath = mailProperties.getActionPath();
            if(actionPath.startsWith("/")){//判断actionPath有无/符去，如果有统一除/
                mailProperties.setActionPath(actionPath.substring(1));
            }
            String url = mailProperties.getProtocol() + "://"
                    + mailProperties.getServerDomain() + ":"
                    + mailProperties.getPort() + "/"
                    + mailProperties.getContextPath() + "/"
                    + mailProperties.getActionPath()
                    + "?code=" + code;
            String content =
                    "<html><head></head><body><h1>This is a account activate email,please click this link if you like activate your account.</h1><h3>" +
                            "<a href='" + url + "'>" + url + "</href></h3></body></html>";
            message.setContent(content, "text/html;charset=UTF-8");

            System.out.println("链接："+url);
            // 3.发送邮件
            Transport.send(message);
            System.out.println("[MailUtil] 邮件成功发送!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
