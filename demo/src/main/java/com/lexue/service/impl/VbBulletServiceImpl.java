package com.lexue.service.impl;

import com.lexue.domain.VbBullet;
import com.lexue.repository.VbIndexRepository;
import com.lexue.service.VbBulletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by UI03 on 2017/6/15.
 */
@Service
public class VbBulletServiceImpl implements VbBulletService {

    private final VbIndexRepository vbIndexRepository ;

    @Autowired
    public  VbBulletServiceImpl (VbIndexRepository  vbIndexRepository){
        this.vbIndexRepository =  vbIndexRepository ;
    }

    public List<VbBullet> queryVbBulletsByVideoid(long timestamp , int video_id) {
        return vbIndexRepository.findBulletsByVideo(timestamp,video_id) ;
    }

    public Page<VbBullet> queryVbBulletsPageByVideoid(long timestamp , int video_id , Pageable pageable){
        return  vbIndexRepository.findBulletsPageByVideo(timestamp,video_id,pageable) ;
    }
}
