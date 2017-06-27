package com.lexue.service;

import com.lexue.domain.VbBullet;
import com.lexue.domain.VbIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by UI03 on 2017/6/9.
 */
public interface VbIndexService {
    List<VbIndex> queryVbIndexByVideoid(int video_id) ;
    List<VbBullet> queryVbBulletsByVideoid(long timestamp ,int video_id) ;
    Page<VbBullet> queryVbBulletsPageByVideoid(long timestamp , int video_id ,Pageable pageable) ;
}
