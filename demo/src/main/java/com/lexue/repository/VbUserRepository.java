package com.lexue.repository;

import com.lexue.domain.VbUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by UI03 on 2017/6/12.
 */
public interface VbUserRepository extends JpaRepository<VbUser,Long> {

    @Query(value = " select  f from VbUser f where f.videoId=?1")
    List<VbUser> findByVideoId(@Param("video_id") int videoId) ;

}
