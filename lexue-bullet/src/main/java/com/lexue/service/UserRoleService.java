package com.lexue.service;

import com.lexue.domain.UserRole;

import java.util.Set;

/**
 * Created by UI03 on 2017/6/26.
 */
public interface UserRoleService {
    UserRole queryUserRole(long user_id) ;
    Set<Long> queryTeaUsers() ;
}
