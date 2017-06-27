package com.lexue.service.impl;

import com.lexue.domain.VbUser;
import com.lexue.repository.VbUserRepository;
import com.lexue.service.VbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by UI03 on 2017/6/9.
 */
@Service
public class VbUserServiceImpl implements VbUserService {

    private final VbUserRepository vbUserRepository;

    @Autowired
    public VbUserServiceImpl(VbUserRepository vbUserRepository) {
        this.vbUserRepository = vbUserRepository;
    }

    public List<VbUser> queryVbUserByVideoid(int video_id){

        return  vbUserRepository.findByVideoId(video_id) ;
    }

}
