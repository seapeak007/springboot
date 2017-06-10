package com.lexue.restful;

import com.lexue.jpa.entity.VideoBulletIndex;
import com.lexue.service.VideoIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by UI03 on 2017/6/9.
 */
@RestController
@RequestMapping(value="/video")
public class VideoIndexController {

    private final VideoIndexService videoIndexService;

    @Autowired
    public VideoIndexController(VideoIndexService videoIndexService){
        this.videoIndexService = videoIndexService ;
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String  hello (){
        System.out.println("hello") ;
        return "hello world " ;
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String  queryVideoIndex(@RequestParam int video_id){

        VideoBulletIndex vb =  this.videoIndexService.queryVideoIndexByVideo(video_id);
        return vb.toString() ;
    }
}
