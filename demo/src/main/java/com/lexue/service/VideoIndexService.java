package com.lexue.service;

import com.lexue.domain.VideoBulletIndex;

import java.util.List;

/**
 * Created by UI03 on 2017/6/9.
 */
public interface VideoIndexService {
    List<VideoBulletIndex> queryVideoIndexByVideoid(int video_id) ;
    VideoBulletIndex queryVideoIndexById(long id) ;

}
