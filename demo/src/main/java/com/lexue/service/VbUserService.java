package com.lexue.service;

import com.lexue.domain.VbUser;

import java.util.List;

/**
 * Created by UI03 on 2017/6/14.
 */
public interface VbUserService {
    List<VbUser> queryVbUserByVideoid(int video_id) ;

}
