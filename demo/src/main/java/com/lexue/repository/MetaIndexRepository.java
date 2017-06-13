package com.lexue.repository;


import com.lexue.domain.VideoBulletIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by UI03 on 2017/6/9.
 */

public interface MetaIndexRepository extends JpaRepository<VideoBulletIndex,Long> {

    @Query(value = "select s from VideoBulletIndex s where s.video_id = ?1")
    List<VideoBulletIndex> findByVideoid(@Param("video_id") int videoid) ;

}
