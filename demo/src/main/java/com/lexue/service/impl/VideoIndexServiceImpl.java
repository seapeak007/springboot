package com.lexue.service.impl;

import com.lexue.domain.VideoBulletIndex;
import com.lexue.repository.MetaIndexRepository;
import com.lexue.service.VideoIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by UI03 on 2017/6/9.
 */
@Service
public class VideoIndexServiceImpl implements VideoIndexService {

    private final MetaIndexRepository metaIndexRepository;

    @Autowired
    public VideoIndexServiceImpl(MetaIndexRepository metaIndexRepository) {
        this.metaIndexRepository = metaIndexRepository;
    }

    public List<VideoBulletIndex> queryVideoIndexByVideoid(int video_id){

        return  metaIndexRepository.findByVideoid(video_id) ;
    }

    public VideoBulletIndex queryVideoIndexById(long id){
        return  metaIndexRepository.findOne(id) ;
    }
}
