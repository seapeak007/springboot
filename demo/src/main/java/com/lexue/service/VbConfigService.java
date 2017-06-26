package com.lexue.service;

import com.lexue.domain.VbConfig;

import java.util.List;

/**
 * Created by UI03 on 2017/6/26.
 */
public interface VbConfigService {
    List<VbConfig> queryUpdateConfig(String client ,long time) ;
}
