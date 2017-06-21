package com.lexue.service;

import com.lexue.domain.VbBullet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by UI03 on 2017/6/15.
 */
public interface VbBulletService {
    List<VbBullet> queryVbBulletsByVideoid(long timestamp , int video_id) ;
    Page<VbBullet> queryVbBulletsPageByVideoid(long timestamp , int video_id , Pageable pageable) ;
    int queryVbBulletsCountByVideoid(long timestamp , int video_id ) ;

    void genLiveBullets() ;
}
