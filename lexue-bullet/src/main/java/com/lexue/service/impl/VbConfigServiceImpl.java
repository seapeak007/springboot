package com.lexue.service.impl;

import com.lexue.domain.VbConfig;
import com.lexue.repository.VbConfigRepository;
import com.lexue.service.VbConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by UI03 on 2017/6/26.
 */
@Service
public class VbConfigServiceImpl implements VbConfigService{
    @Autowired
    private VbConfigRepository vbConfigRepository ;

    @Override
    public List<VbConfig> queryUpdateConfig(String client ,long time) {
        return vbConfigRepository.queryUpdateConfig(client , time);
    }
}
