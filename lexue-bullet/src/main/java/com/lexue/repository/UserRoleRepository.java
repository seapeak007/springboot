package com.lexue.repository;

import com.lexue.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

/**
 * Created by UI03 on 2017/6/26.
 */
public interface UserRoleRepository extends JpaRepository<UserRole,Long> {
    @Query("select f.user_id from UserRole f  where f.role in (1,3) ")
    Set<Long> queryTeaUsers();
}
