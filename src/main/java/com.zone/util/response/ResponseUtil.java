package com.zone.util.response;

/**
 * @ClassName ResponseUtil
 * @Author zone
 * @Date 2018/12/27  15:09
 * @Version 1.0
 * @Description
 */
public class ResponseUtil {
    public static Response success(Object object){
        return template(ResponseCode.SUCCESS,"response code",object);
    }
    public static Response success(String message){
        return template(ResponseCode.SUCCESS,message,null);
    }
    public static Response success(String message,Object object){
        return template(ResponseCode.SUCCESS,message,object);
    }
    public static Response success(ResponseCode code,String message){
        return template(code,message,null);
    }
    public static Response error(ResponseCode code,String message){
        return template(code,message,null);
    }
    public static Response template(ResponseCode responseCode,String message,Object object){
        Response response=new Response();
        response.setCode(responseCode);
        response.setMesseage(message);
        response.setData(object);
        return response;
    }
}
