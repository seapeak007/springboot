package com.lexue.service.impl;

import com.lexue.jpa.entity.VideoBulletIndex;
import com.lexue.jpa.repository.IndexRepository;
import com.lexue.service.VideoIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by UI03 on 2017/6/9.
 */
@Service
public class VideoIndexServiceImpl implements VideoIndexService {

    private final IndexRepository indexRepository;

    @Autowired
    public VideoIndexServiceImpl(IndexRepository indexRepository) {
        this.indexRepository = indexRepository;
    }

    public VideoBulletIndex queryVideoIndexByVideo(int video_id){

        return  indexRepository.findByVideoid(video_id) ;
    }

    public VideoBulletIndex queryVideoIndexById(int id){
        return  indexRepository.findOne(id) ;
    }
}
