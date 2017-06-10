package com.lexue.service;

import com.lexue.jpa.entity.VideoBulletIndex;

/**
 * Created by UI03 on 2017/6/9.
 */
public interface VideoIndexService {
    VideoBulletIndex queryVideoIndexByVideo(int video_id) ;
    VideoBulletIndex queryVideoIndexById(int id) ;

}
