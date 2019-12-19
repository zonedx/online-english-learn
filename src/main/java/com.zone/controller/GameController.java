package com.zone.controller;

import com.zone.entity.Game;
import com.zone.service.GameService;
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

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * @ClassName GameController
 * @Author zone
 * @Date 2019/2/21  8:54
 * @Version 1.0
 * @Description
 */
@Controller
public class GameController {
    private static final Logger logger = Logger.getLogger(GameController.class.getName());
    //日志前缀字符串,方便通过日志定位程序
    private static String logPrefix = null;
    @Autowired
    private GameService gameService;

    @RequestMapping("/findAllGames/api")
    public String findAllGame(ModelMap modelMap,@RequestParam(value = "page",required = false,defaultValue = "0")Integer page){
        Page<Game> games=gameService.findAllGame(page);
        modelMap.addAttribute("datas",games);
        return "game";
    }
    @RequestMapping("/findOneGame/api")
    public String findOneGame(ModelMap modelMap,@RequestParam(value = "id",required = false,defaultValue = "1")Integer id){
        Game game=gameService.findOneGame(id);
        //String path=game.getGamePlace();
        modelMap.addAttribute("datas",game);
        return "game";
    }
    private Integer getScore;
    private Integer getTime;
    @ResponseBody
    @RequestMapping("/getScore/api")
    public void getScore(@RequestParam(value = "score")Integer score,
                             @RequestParam(value = "time")Integer time){
        if(score==null){
            //return ResponseUtil.error(ResponseCode.FAIL,"失败");
        }
        getScore=score;
        getTime=time;
        System.out.println("score:"+score+"time"+time);
    }
    @ResponseBody
    @RequestMapping("/calScore/api")
    public Response calScore(){
        float points=gameService.calScore(getScore,getTime);
        return ResponseUtil.success(points);
    }
}
