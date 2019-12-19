package com.zone.service;

import com.zone.dao.VideoRepository;
import com.zone.entity.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * @ClassName VideoService
 * @Author zone
 * @Date 2019/1/9  14:46
 * @Version 1.0
 * @Description
 */
@Service("videoService")
public class VideoService {
    public static final Logger logger = Logger.getLogger(VideoService.class.getName());
    //日志前缀字符串,方便通过日志定位程序
    private static String logPrefix = null;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private UserService userService;

    /**
     *
     * @param page
     * @return
     */
    public Page<Video> findAllVideo(Integer page){
        Pageable pageable = new PageRequest(page, 3, Sort.Direction.ASC, "id");
        return videoRepository.findAll(pageable);
    }

    /**
     * 通过ID查询视频记录
     * @param id
     * @return
     */
    public Video findOneVideo(Integer id){
        Video video=videoRepository.findFirstById(id);
        return video;
    }

}
