package com.lexue.service.impl;

import com.lexue.exception.LoginInOtherPlaceException;
import com.lexue.exception.SessionErrorException;
import com.lexue.service.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by danielmiao on 2017/5/11.
 * Version: 1.0.0
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final static Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    @Value("${application.auth.uri}")
    private String uri;

    private final RestTemplate restTemplate;

    @Autowired
    public AuthenticationServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class SessionInfo {
        private String error_info;
        private int status;
        private long uid;
    }

    @Override
    public long checkSession(String sid, String did) throws SessionErrorException, LoginInOtherPlaceException {
        long uid;
        SessionInfo sessionInfo;
        if (did != null) {
            sessionInfo = this.restTemplate.getForObject(uri + "?sid={sid}&did={did}",
                    SessionInfo.class, sid, did);
        } else if (sid == null) {
            logger.error("sid is null.");
            throw new SessionErrorException("sid is null.");
        } else {
            sessionInfo = this.restTemplate.getForObject(uri + "?sid={sid}",
                    SessionInfo.class, sid);
        }

        if (sessionInfo != null && sessionInfo.getStatus() == 0) {
            uid = sessionInfo.getUid();
        } else if (sessionInfo != null && sessionInfo.getStatus() == 29) {
            logger.error("login in other place, sid=[{}], code=[{}],msg=[{}].", sid, sessionInfo.getStatus(), sessionInfo
                    .getError_info());
            throw new LoginInOtherPlaceException(sessionInfo.getError_info());
        } else if (sessionInfo != null) {
            logger.error("session error, sid=[{}], code=[{}],msg=[{}].", sid, sessionInfo.getStatus(), sessionInfo
                    .getError_info());
            throw new SessionErrorException(sessionInfo.getError_info());
        } else {
            logger.error("result is null");
            throw new SessionErrorException("result is null.");
        }
        return uid;
    }
}
