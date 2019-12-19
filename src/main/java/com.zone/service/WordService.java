package com.zone.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zone.dao.ForgotWordRepository;
import com.zone.dao.WordRepository;
import com.zone.entity.ForgotWord;
import com.zone.entity.NewWord;
import com.zone.entity.User;
import com.zone.entity.Word;
import com.zone.util.datetime.DateTimeUtil;
import javax.servlet.http.HttpServletRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.logging.Logger;

/**
 * @ClassName WordService
 * @Author zone
 * @Date 2018/12/31  14:17
 * @Version 1.0
 * @Description
 */
@Service("wordService")
public class WordService {
    public static final Logger logger = Logger.getLogger(WordService.class.getName());
    //日志前缀字符串,方便通过日志定位程序
    private static String logPrefix = null;
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ForgotWordRepository forgotWordRepository;

    /**
     * 显示所有单词
     * @param page
     * @return
     */
    public Page<Word> findAllWords( Integer page,Integer type){
        Pageable pageable=new PageRequest(page,1,Sort.Direction.ASC, "id");
        return wordRepository.findByType(type,pageable);
    }

    /**
     * 查询所有单词翻译|拼写
     * @param page
     * @return
     */
    public Page<Word> findSpelling( Integer page){
        Pageable pageable=new PageRequest(page,8,Sort.Direction.ASC, "id");
        //System.out.println(wordRepository.findAll(pageable));
        return wordRepository.findAll(pageable);
    }
    /**
     * 查询该单词的翻译
     * @param english
     * @return
     */
    public String findTranslation(String english){
        logPrefix="[WordService.findTranslation]";
        Word word=null;
        word=wordRepository.findByEnglish(english);
        String chinese=null;
        if(word==null){
            word=new Word();
            try {
                word.setEnglish(english);
                chinese=this.translate(english);
                if(english.matches("^[\\u4e00-\\u9fa5]+$")){//至少匹配一个中文
                    word.setChinese(english);
                }
                if(chinese.equals(english)){
                    return "error";
                }else {
                    if(english.matches("^[\\u4e00-\\u9fa5]+$")){//至少匹配一个中文,说明输入查询的是中文
                        //输入中文查询必调用三方接口
                        //若存在该单词则不保存数据库
                        if(wordRepository.findByEnglish(chinese)!=null){
                            return chinese;
                        }
                        word.setEnglish(chinese);
                    }else {
                        word.setChinese(chinese);
                    }
                    word.setType(2);//新增单词默认为中级单词
                    wordRepository.save(word);
                    return chinese;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        chinese=word.getChinese();
        if(chinese==null){
            logger.info("该单词:"+english+"暂无翻译，将调用三方翻译接口");
            try {
                chinese=this.translate(english);
                if(chinese.length()>10){
                    return "error";
                }else {
                    word.setChinese(chinese);
                    wordRepository.save(word);//将从三方翻译接口查询到的翻译保存到数据库中，下次查询可用
                    logger.info("保存单词:"+english+"翻译:"+chinese+"  成功~");
                    return chinese;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return chinese;
    }

    /**
     * 查看遗忘词汇详情
     * @param english
     * @return
     */
    public Word findWord(String english){
        Word word=wordRepository.findByEnglish(english);
        return word;
    }
    /**
     * 查询单词的三方翻译
     * @param english
     * @return
     */
    public String translate(String english) throws IOException {
        logPrefix="[WordService.translate]";
        StringBuilder url=new StringBuilder("http://fanyi.youdao.com/translate?&doctype=json&type=AUTO&i=");
        url.append(english);
        URL u=new URL(url.toString());
        Document document=null;
        if(english.matches("^[\\u4e00-\\u9fa5]+$")){//待翻译的字符串是中文，需编码转换
            HttpURLConnection connection= (HttpURLConnection) u.openConnection();
            document=Jsoup.parse(connection.getInputStream(),"GBK",url.toString());
        }else {
            document=Jsoup.connect(url.toString())
            .header("Content-Type", "application/json;charset=UTF-8")
            .header("Connection", "keep-alive")
            .header("Accept", "*/*")
            .header("Accept-Encoding", "gzip, deflate")
            .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
            .header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
            .ignoreContentType(true) //resolve:org.jsoup.UnsupportedMimeTypeException: Unhandled content type. Must be text/*, application/xml, or application/xhtml+xml. Mimetype=application/x-javascript, URL=www.a.com
            .get();
        }
        ObjectMapper mapper=new ObjectMapper();
        NewWord word=null;
        word=mapper.readValue(document.text(),NewWord.class);
        String translate=word.getTranslateResult().get(0).get(0).getTgt();
        if(translate.equals(english)){
            logger.info("该单词:"+english+"有误，请重新查询");
            return english;
        }
        return translate;
    }

    /**
     * 将单词忘记标记
     * 1、从遗忘词汇表查询该单词是否存在
     * 1.1存在则 遗忘次数+1，更新最近记忆时间
     * 1.2不存在，将此单词存入遗忘词汇表
     * @param english
     * @return
     */
    public int forgotTag(HttpServletRequest request,String english){
        Word word=null;
        User user=null;
        ForgotWord forgotWord=new ForgotWord();
        user=userService.findOneByLoginUserMap(request);
        word=wordRepository.findByEnglish(english);
        int wordId=word.getId();
        if(forgotWordRepository.isExistThisWord(wordId)!=1){//不存在此单词
            System.out.println("wordId-----"+wordId);
            forgotWord.setWordId(wordId);
            forgotWord.setEnglish(word.getEnglish());
            forgotWord.setTimes(1);
            forgotWord.setStoredDatetime(DateTimeUtil.calendarToTimestamp(Calendar.getInstance()));
            user.setPoints(user.getPoints()+1);//增加用户积分
        }else {
            forgotWord=forgotWordRepository.findByWordId(wordId);
            forgotWord.setTimes(forgotWord.getTimes()+1);
            user.setPoints(user.getPoints()+1);
        }
        userService.save(user);
        forgotWordRepository.save(forgotWord);
        return 1;
    }

    /**
     * 查询所有高频忘词
     * @param page
     * @return
     */
    public Page<ForgotWord> findAllForgotWord(Integer page){
        Pageable pageable=new PageRequest(page,1,Sort.Direction.DESC, "times");
        return forgotWordRepository.findAll(pageable);
    }
}
