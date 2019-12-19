package com.zone.service;

import com.zone.dao.GameRepository;
import com.zone.entity.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * @ClassName GameService
 * @Author zone
 * @Date 2019/2/21  8:53
 * @Version 1.0
 * @Description
 */
@Service("gameService")
public class GameService {
    public static final Logger logger = Logger.getLogger(GameService.class.getName());
    //日志前缀字符串,方便通过日志定位程序
    private static String logPrefix = null;
    @Autowired
    private UserService userService;
    @Autowired
    private GameRepository gameRepository;

    public Page<Game> findAllGame(Integer page){
        Pageable pageable=new PageRequest(page,3,Sort.Direction.ASC,"id");
        return gameRepository.findAll(pageable);
    }

    public Game findOneGame(Integer id){
        Game game=gameRepository.findFirstById(id);
        return game;
    }

    /**
     * 计算本次游戏所得积分
     * 规则：
     *      由于游戏时间为180S
     *      积分=游戏得分*（当前剩余时间/180S）
     *      if
     *      当前剩余时间<18S
     *      积分=游戏得分*0.1
     * @param score
     * @param time
     * @return
     */
    public Float calScore(Integer score,Integer time){
        float points;
//        while(time!=null&&score!=null){
//            //--------//
//        }
        if(time<=18){
            points=score/10;
        }else {
            points=score*time/180;
        }
        //System.out.println("points+++++"+points);
        return points;
    }
}
