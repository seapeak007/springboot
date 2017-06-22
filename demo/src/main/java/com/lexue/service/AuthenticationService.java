package com.lexue.service;

import com.lexue.exception.LoginInOtherPlaceException;
import com.lexue.exception.SessionErrorException;

/**
 * Created by danielmiao on 2017/5/11.
 * Version: 1.0.0
 */
public interface AuthenticationService {
    long checkSession(String sid, String did) throws SessionErrorException, LoginInOtherPlaceException;
}
