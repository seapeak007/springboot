package com.lexue.service.impl;

import com.lexue.domain.UserRole;
import com.lexue.repository.UserRoleRepository;
import com.lexue.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by UI03 on 2017/6/26.
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    private UserRoleRepository userRoleRepository  ;

    @Override
    public UserRole queryUserRole(long user_id) {
        return userRoleRepository.findOne(user_id);
    }

    @Override
    public Set<Long> queryTeaUsers() {
        return userRoleRepository.queryTeaUsers();
    }
}
