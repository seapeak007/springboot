package com.lexue.service.impl;

import com.lexue.config.CacheConfig;
import com.lexue.domain.Live;
import com.lexue.repository.LiveRepository;
import com.lexue.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by UI03 on 2017/7/12.
 */
@Slf4j
@Service
public class CacheServiceImpl implements CacheService {
    @Autowired
    private LiveRepository liveRepository ;

    /**
     * 查询直播Live
     * @param liveroom
     * @return
     */
    @Cacheable(CacheConfig.LIVE_ROOM)
    public Live queryLiveByRoom(int liveroom){
        log.info("go repository");
        return liveRepository.queryLiveByRoom(liveroom) ;
    }
}
