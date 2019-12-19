package com.zone.service;

import com.zone.dao.SententialRepository;
import com.zone.entity.Sentential;
import com.zone.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.logging.Logger;

/**
 * @ClassName SententialService
 * @Author zone
 * @Date 2019/1/7  13:12
 * @Version 1.0
 * @Description
 */
@Service("sententialService")
public class SententialService {
    static final Integer primaryLevel=120;//等级限时
    static final Integer middleLevel=160;
    public static final Logger logger = Logger.getLogger(SententialService.class.getName());
    //日志前缀字符串,方便通过日志定位程序
    private static String logPrefix = null;
    @Autowired
    private UserService userService;
    @Autowired
    private SententialRepository sententialRepository;

    /**
     * 根据类别查询所有句型
     *
     * @param page
     * @param type
     * @return
     */
    public Page<Sentential> findAllSentential(Integer page, Integer type) {
        Pageable pageable = new PageRequest(page, 4, Sort.Direction.ASC, "id");
        return sententialRepository.findByType(type, pageable);
    }

    /**
     * 验证填入的单词是否正确
     * 返回1--答案正确，并且增加用户积分
     * 返回2、4--答案不正确
     * 返回3--答案正确，但不增加用户积分
     * 若已看过该题的正确答案，用户积分不增加
     *
     * @param fillWord
     * @param sentenceId
     * @return
     */
    public Integer checkFillWord(HttpServletRequest request, String fillWord, String sentenceId) {
        logPrefix = "[SententialService.checkFillWord]";
        Sentential sentential = null;
        User user=null;
        Map<String,Sentential> sententialMap=null;
        sententialMap = (Map<String, Sentential>) request.getSession().getAttribute("sententialMap");
        user=  userService.findOneByLoginUserMap(request);
        if(sententialMap==null){
            sententialMap=new HashMap<String,Sentential>();
        }
        sentential = sententialMap.get(sentenceId);
        if (sentential == null) {
            sentential = sententialRepository.findBySentenceId(sentenceId);
            String keyWord = sentential.getFillWord().trim();
            if (keyWord.equalsIgnoreCase(fillWord.trim())) {
                user.setPoints(user.getPoints()+1);//用户积分增加1
                userService.save(user);
                logger.info(logPrefix+":答案：" + fillWord + "正确！");
                return 1;//答题正确,并且增加用户积分
            } else {
                logger.info(logPrefix+":答案：" + fillWord + "不正确！");
                return 2;
            }
        }else {
                String keyWord = sentential.getFillWord();
                if (keyWord.equalsIgnoreCase(fillWord.trim())) {
                    logger.info("填入单词正确，但由于已看过答案，故不增加用户积分");
                    return 3;
                }
                return 4;
        }
    }

    /**
     * 初级训练4题 限时4*30s=120s
     * 中级训练8题 限时8*20s=160s
     *
     * @param request
     * @param fillword
     * @param sentenceId
     *   type 训练等级  1--初级  2--中级
     * @return
     */

    //private float grade=0;//成绩
    public Float accountResult(HttpServletRequest request,List<String> fillword,List<String> sentenceId){
        float grade=0;//成绩
        logPrefix = "[SententialService.TimeLimitedTraining]";
        int isRight=0;
        User user=userService.findOneByLoginUserMap(request);
        for(int i=0;i<fillword.size();i++){
            isRight=checkFillWord(request,fillword.get(i),sentenceId.get(i));
            if(isRight==1 ||isRight==3){
                grade++;
            }
        }
        if(fillword.size()>4){//说明是中级
            grade=(grade/8)*100;
        }else {
            grade=(grade/4)*100;
        }
        user.setPoints((int)(grade/25)+user.getPoints());//增加对应积分
        userService.save(user);
        return grade;
    }

    /**
     * 根据类别随机查询题目
     * @param page
     * @param type
     * @return
     */
    private Page<Sentential> sententials=null;
    public Page<Sentential> findSomeSentential(Integer page,Integer type){
        logPrefix = "[SententialService.findSomeSentential]";

        if(type==1){
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("答题超时");
                    logger.info(logPrefix+":答题超时");
                    sententials=null;
                }
            },primaryLevel*1000);
            while (true){
                Pageable pageable = new PageRequest(page, 4, Sort.Direction.ASC, "id");
                sententials=sententialRepository.findSententialsByRandAndType(type,3,pageable);
                return sententials;
            }
        }
        if(type==2){
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("答题超时");
                    logger.info(logPrefix+":答题超时");
                }
            },middleLevel*1000);
            while (true){
                Pageable pageable = new PageRequest(page, 8, Sort.Direction.ASC, "id");
                return sententialRepository.findSententialsByRandAndType(type,7,pageable);
            }
        }
        return null;
    }
    /**
     * 查看该题的正确答案
     *   将答案存在map中，避免经常操作数据库
     * @param request
     * @param sentenceId
     * @return
     */
    public Sentential findCorrectWord(HttpServletRequest request, String sentenceId) {
        logPrefix="[SententialService.findCorrectWord]";
        Sentential sentential = null;
        Map<String,Sentential> sententialMap=null;
        sententialMap = (Map<String, Sentential>) request.getSession().getAttribute("sententialMap");

        if (sententialMap == null) {//还没查看过正确答案
            sententialMap=new HashMap<String,Sentential>();//注意初始化map
            sentential = sententialRepository.findBySentenceId(sentenceId);
            if(sentential!=null){
                sententialMap.put(sentenceId,sentential);
                request.getSession().setAttribute("sententialMap", sententialMap);
            }else
                logger.info(logPrefix+":没有该编号:"+sentenceId+"对应的句型");
            //不过此可能性发生概率很小，因为sentenceId是从数据库中读出来展示到网页上，再从网页上获取的
        }else {
            sentential=sententialMap.get(sentenceId);
            if(sentential==null){//还没看过该题目（sententialId）的正确答案
                sentential = sententialRepository.findBySentenceId(sentenceId);
                sententialMap.put(sentenceId,sentential);
                request.getSession().setAttribute("sententialMap", sententialMap);
            }
        }
        return sentential;
    }
}
