package com.lexue.service.impl;

import com.lexue.domain.VbMeta;
import com.lexue.repository.VbMetaRepository;
import com.lexue.service.VbMetaService;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by UI03 on 2017/6/14.
 */
@Service
public class VbMateServiceImpl implements VbMetaService {

    private VbMetaRepository vbMetaRepository ;
    @Autowired
    public VbMateServiceImpl(VbMetaRepository vbMetaRepository){
        this.vbMetaRepository = vbMetaRepository ;
    }

    @Cacheable(com.lexue.config.CacheConfig.META_CACHE)
    public List<VbMeta> getMetas() {
        List<VbMeta> metalist = new ArrayList<VbMeta>() ;
        System.out.println("go repository");
        metalist = this.vbMetaRepository.findAll() ;
        return metalist ;
    }
}
