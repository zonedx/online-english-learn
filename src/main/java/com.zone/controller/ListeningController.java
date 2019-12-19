package com.zone.controller;

import com.zone.entity.Listening;
import com.zone.service.ListeningService;
import com.zone.service.UserService;
import com.zone.util.response.Response;
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
 * @ClassName ListeningController
 * @Author zone
 * @Date 2019/1/25  16:29
 * @Version 1.0
 * @Description
 */
@Controller
public class ListeningController {
    private static final Logger logger = Logger.getLogger(ListeningController.class.getName());
    //日志前缀字符串,方便通过日志定位程序
    private static String logPrefix = null;
    @Autowired
    private ListeningService listeningService;
    @RequestMapping("/findAllListening/api")
    public String findAllListening(ModelMap modelMap, @RequestParam(value = "page",required = false,defaultValue = "0")Integer page,
                                   @RequestParam(value = "type",defaultValue = "1")Integer type){
        Page<Listening> datas=listeningService.findAllListening(page,type);
        String listening=datas.getContent().get(0).getListeningPlace();
        String material=listening.replace(".mp3",".txt");
        System.out.println(material);
        modelMap.addAttribute("datas",datas);
        modelMap.addAttribute("listening",listening);
        modelMap.addAttribute("material",material);
        return "listening-details";
    }

    @ResponseBody
    @RequestMapping("/getGrade/api")
    public Response getGrade(HttpServletRequest request){
        Map<String,String[]> keyMap=new HashMap<String, String[]>();
        keyMap=request.getParameterMap();
        List<String> fillAnswer=new ArrayList<String>();
        List<Integer> id=new ArrayList<Integer>();
        Float grade=0f;
        if(keyMap!=null){
            Iterator<Map.Entry<String,String[]>> it = keyMap.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry<String,String[]> entry=it.next();
                String key=entry.getKey();  //题目id
                id.add(Integer.valueOf(key));
                String values[]=entry.getValue();
                fillAnswer.add(values[0]);
                System.out.println(key+"--------------"+entry.getValue().toString());
            }
        }
        grade=listeningService.accountResult(request,fillAnswer,id);
        DecimalFormat decimalFormat=new DecimalFormat(".0");
        return ResponseUtil.success("成绩为："+grade,decimalFormat.format(grade));
    }
}
