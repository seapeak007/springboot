package com.lexue.service;

import com.lexue.domain.VbBullet;
import com.lexue.domain.VbIndex;

import java.util.List;

/**
 * Created by UI03 on 2017/6/9.
 */
public interface VbIndexService {
    List<VbIndex> queryVbIndexByVideoid(int video_id) ;
    List<VbBullet> queryVbBulletsByVideoid(int video_id) ;

}
