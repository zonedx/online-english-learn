package com.zone.service;

import com.zone.dao.ListeningRepository;
import com.zone.entity.Listening;
import com.zone.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.logging.Logger;

/**
 * @ClassName ListeningService
 * @Author zone
 * @Date 2019/1/24  11:34
 * @Version 1.0
 * @Description
 */
@Service("listeningService")
public class ListeningService {
    public static final Logger logger = Logger.getLogger(ListeningService.class.getName());
    //日志前缀字符串,方便通过日志定位程序
    private static String logPrefix = null;
    @Autowired
    private UserService userService;
    @Autowired
    private ListeningRepository listeningRepository;

    /**
     * 根据类别查询所有句型
     *
     * @param page
     * @param type
     * @return
     */
    public Page<Listening> findAllListening(Integer page, Integer type) {
        Pageable pageable = new PageRequest(page, 10, Sort.Direction.ASC, "id");
        return listeningRepository.findByType(type, pageable);
    }

    /**
     * 检查答案
     * @param request
     * @param fillAnswer
     * @param id
     * @return
     */
    public int checkAnswer(HttpServletRequest request,String fillAnswer,Integer id){
        logPrefix="[ListeningService.checkAnswer]";
        Listening listening=null;
        listening=listeningRepository.findFirstById(id);
        String correctAnswer=listening.getCorrectAnswer();
        if(correctAnswer.equalsIgnoreCase(fillAnswer.trim())){
            logger.info(logPrefix+":答案：" + fillAnswer + "正确！");
            return 1;
        }else {
            logger.info(logPrefix+":答案：" + fillAnswer + "错误！");
            return 2;
        }
    }

    /**
     *
     * @param request
     * @param fillAnswer
     * @param id
     * @return
     */
    public Float accountResult(HttpServletRequest request, List<String> fillAnswer,List<Integer> id){
        float grade=0;//成绩
        int isRight=0;
        HttpSession session=request.getSession();
        User user=userService.findOneByLoginUserMap(request);
        for(int i=0;i<fillAnswer.size();i++){
            isRight=checkAnswer(request,fillAnswer.get(i),id.get(i));
            if(isRight==1){
                grade++;
            }
        }
        user.setPoints(user.getPoints()+(int)grade);//增加对应积分
        userService.save(user);
        //System.out.println("now----time---"+System.currentTimeMillis());
        //System.out.println("session----time---"+session.getAttribute("oldTime"));
        grade=grade*10;
        //session.setAttribute("oldTime",System.currentTimeMillis());
        System.out.println("grade-----"+grade);
        return grade;
    }
}
