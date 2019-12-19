package com.zone.controller;

import com.zone.entity.ForgotWord;
import com.zone.entity.Word;
import com.zone.service.UserService;
import com.zone.service.WordService;
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
import java.util.logging.Logger;

/**
 * @ClassName WordController
 * @Author zone
 * @Date 2018/12/31  14:43
 * @Version 1.0
 * @Description
 */
@Controller
public class WordController {
    private static final Logger logger = Logger.getLogger(WordController.class.getName());
    //日志前缀字符串,方便通过日志定位程序
    private static String logPrefix = null;
    @Autowired
    private WordService wordService;

    @RequestMapping("/findAllWords/api")
    public String findWords(ModelMap modelMap, @RequestParam(value = "page",required = false,defaultValue = "0")Integer page,
                            @RequestParam(value = "type",defaultValue = "1")Integer type){
        Page<Word> words=wordService.findAllWords(page,type);

        modelMap.addAttribute("datas",words);

        return "learn-word";
    }
    @RequestMapping("/word")
    public String findForgotWord(ModelMap modelMap, @RequestParam(value = "page",required = false,defaultValue = "0")Integer page){
        Page<ForgotWord> forgotWords=wordService.findAllForgotWord(page);
        modelMap.addAttribute("forgotWords",forgotWords);
        return "word";
    }
    @RequestMapping("/findTranslate/api")
    public String findTranslate(ModelMap modelMap, @RequestParam(value = "page",required = false,defaultValue = "0")Integer page){
        Page<Word> words=wordService.findSpelling(page);
        modelMap.addAttribute("datas",words);
        return "translate-word";
    }
    @RequestMapping("/findSpelling/api")
    public String findSpelling(ModelMap modelMap, @RequestParam(value = "page",required = false,defaultValue = "0")Integer page){
        Page<Word> words=wordService.findSpelling(page);
        modelMap.addAttribute("datas",words);
        return "spelling-word";
    }
    @RequestMapping("/findForgotWordDetails/api")
    public String findForgotWordDetails(ModelMap modelMap,@RequestParam(value = "english")String english){
        Word word=wordService.findWord(english);
        modelMap.addAttribute("datas",word);
        return "forgot-word-details";
    }
    @ResponseBody
    @RequestMapping("/findTranslation/api")
    public Response findTranslation(@RequestParam(value = "english")String english){
        if(english==null||english.length()==0){
            return ResponseUtil.error(ResponseCode.FAIL,"输入为空");
        }else{
            String s=wordService.findTranslation(english);
            if(s.equals("error")){
                return ResponseUtil.error(ResponseCode.FAIL,"该单词错误");
            }
            return ResponseUtil.success("success",s);
        }
    }
    @ResponseBody
    @RequestMapping("/forgotTag/api")
    public Response forgotTag(HttpServletRequest request, @RequestParam(value = "english")String english){
        int i=wordService.forgotTag(request,english);
        if(i==1){
            return ResponseUtil.success("success");
        }
        return ResponseUtil.error(ResponseCode.FAIL,"标记失败");
    }
}
