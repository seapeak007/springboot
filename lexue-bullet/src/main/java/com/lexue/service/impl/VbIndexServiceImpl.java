package com.lexue.service.impl;

import com.lexue.domain.VbBullet;
import com.lexue.domain.VbIndex;
import com.lexue.repository.VbIndexRepository;
import com.lexue.service.VbIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by UI03 on 2017/6/9.
 */
@Service
public class VbIndexServiceImpl implements VbIndexService {

    private final VbIndexRepository vbIndexRepository;

    @Autowired
    public VbIndexServiceImpl(VbIndexRepository vbIndexRepository) {
        this.vbIndexRepository = vbIndexRepository;
    }

    public List<VbIndex> queryVbIndexByVideoid(int video_id){

        return  vbIndexRepository.findByVideoId(video_id) ;
    }

    public List<VbBullet> queryVbBulletsByVideoid(long timestamp ,int video_id) {
        return vbIndexRepository.findBulletsByVideo(timestamp,video_id) ;
    }

    public Page<VbBullet> queryVbBulletsPageByVideoid(long timestamp , int video_id , Pageable pageable){
        return  vbIndexRepository.findBulletsPageByVideo(timestamp,video_id,pageable) ;
    }

}
