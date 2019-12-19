package com.zone.controller;

import com.zone.entity.Video;
import com.zone.service.UserService;
import com.zone.service.VideoService;
import com.zone.util.file.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URL;
import java.util.logging.Logger;

/**
 * @ClassName VideoController
 * @Author zone
 * @Date 2019/1/9  14:48
 * @Version 1.0
 * @Description
 */
@Controller
public class VideoController {
    private static final Logger logger = Logger.getLogger(VideoController.class.getName());
    //日志前缀字符串,方便通过日志定位程序
    private static String logPrefix = null;
    @Autowired
    private VideoService videoService;

    @RequestMapping("/findAllVideos/api")
    public String findAllVideo(ModelMap modelMap, @RequestParam(value = "page",required = false,defaultValue = "0")Integer page) {
        Page<Video> videos=videoService.findAllVideo(page);
        modelMap.addAttribute("datas",videos);
        return "video";
    }
    @RequestMapping("/findOneVideo/api")
    public String findOneVideo(ModelMap modelMap,@RequestParam(value = "id")Integer id){
        Video video=videoService.findOneVideo(id);
        String path=video.getCaptions();
        URL url = this.getClass().getClassLoader().getResource(path);
        String str=FileUtil.readFile(url.getPath());
        str = str.replaceAll("\\\\r|\\\\n", "<br/>");
        video.setCaptions(str);
        modelMap.addAttribute("datas",video);
        return "video-details";
    }
}
