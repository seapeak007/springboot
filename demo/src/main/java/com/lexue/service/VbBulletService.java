package com.lexue.service;

import com.lexue.domain.VbBullet;
import com.lexue.http.CommonResponse;
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

    void addLiveBullets(int uid , int liveroom , String content ,int chat_time ,int msg_type ) ;

    CommonResponse genVideoBullet(int uid,int video_id,int chat_time ,String content ,int msg_type) ;
    void addLocalZip(String filename) ;
}
