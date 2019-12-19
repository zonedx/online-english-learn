package com.zone.controller;

import com.zone.entity.Sentential;
import com.zone.service.SententialService;
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
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * @ClassName SententialController
 * @Author zone
 * @Date 2019/1/7  13:15
 * @Version 1.0
 * @Description
 */
@Controller
public class SententialController {
    private static final Logger logger = Logger.getLogger(SententialController.class.getName());
    //日志前缀字符串,方便通过日志定位程序
    private static String logPrefix = null;
    @Autowired
    private SententialService sententialService;

    @RequestMapping("/findAllSentences/api")
    public String findAllSentential(ModelMap modelMap, @RequestParam(value = "page",required = false,defaultValue = "0")Integer page,
                                    @RequestParam(value = "type",defaultValue = "1")Integer type){
        Page<Sentential> sententials=sententialService.findAllSentential(page,type);
        modelMap.addAttribute("datas",sententials);
        return "sentential";
    }
    @ResponseBody
    @RequestMapping("/findSomeSententials/api")
    public Response findSomeSentential(ModelMap modelMap, @RequestParam(value = "page",required = false,defaultValue = "0")Integer page,
                                    @RequestParam(value = "type",defaultValue = "1")Integer type){
        Page<Sentential> sententials=sententialService.findSomeSentential(page,type);
        //modelMap.addAttribute("datas",sententials);
        return ResponseUtil.success("选题成功",sententials);
    }

    @ResponseBody
    @RequestMapping("/findCorrectWord/api")
    public Response findCorrectWord(HttpServletRequest request,
                                    @RequestParam(value = "sentenceId")String sentenceId){
        Sentential sentential=null;
        sentential=sententialService.findCorrectWord(request,sentenceId);
        if(sentential!=null){
            return ResponseUtil.success("查看答案成功",sentential.getFillWord());
        }
        return ResponseUtil.error(ResponseCode.FAIL,"查看答案失败");
    }
    @ResponseBody
    @RequestMapping("/checkFillWord/api")
    public Response checkFillWord(HttpServletRequest request,
                                  @RequestParam(value = "fillword")String fillword,
                                  @RequestParam(value = "sentenceId")String sentenceId){
        int result=sententialService.checkFillWord(request,fillword,sentenceId);
        switch (result){
            case 1:
                return ResponseUtil.success("答题正确，并增加用户积分");
            case 2:
                return ResponseUtil.error(ResponseCode.FAIL,"答题错误，请重新作答");
            case 3:
                return ResponseUtil.success(ResponseCode.SUCCESS_NO_POINTS,"答题正确，但由于已看过答案，故不增加用户积分");
            case 4:
                return ResponseUtil.error(ResponseCode.FAIL,"答题错误，请重新作答");
                default:
                    return ResponseUtil.error(ResponseCode.FAIL,"系统出错");
        }
    }
    public Response TimeLimitedTraining(HttpServletRequest request,
                                        @RequestParam(value = "fillword")String fillword,
                                        @RequestParam(value = "sentenceId")String sentenceId){

        return ResponseUtil.error(ResponseCode.FAIL,"");
    }
    @ResponseBody
    @RequestMapping("/submitSentential/api")
    public Response submitSentential(HttpServletRequest request){
        Map<String,String[]> keyMap=new HashMap<String, String[]>();
        keyMap=request.getParameterMap();
        List<String> fillWord=new ArrayList<String>();
        List<String> sentenceId=new ArrayList<String>();
        Float score=0f;
        if(keyMap!=null){
            Iterator<Map.Entry<String,String[]>> it2 = keyMap.entrySet().iterator();
            Iterator it=keyMap.entrySet().iterator();
            while (it2.hasNext()){
                Map.Entry<String,String[]> entry= it2.next();
                String key=entry.getKey();
                sentenceId.add(key);
                String values[]=entry.getValue();
                String value=values[0];
                fillWord.add(value);
                System.out.println(key+"="+value);

            }
        }
        score=sententialService.accountResult(request,fillWord,sentenceId);
        DecimalFormat decimalFormat=new DecimalFormat(".0");
        return ResponseUtil.success("成绩为："+score,decimalFormat.format(score));
    }
}
