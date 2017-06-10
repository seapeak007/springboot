package com.lexue.jpa.repository;

import com.lexue.jpa.entity.VideoBulletIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by UI03 on 2017/6/9.
 */

public interface IndexRepository extends JpaRepository<VideoBulletIndex,Integer> {

    @Query(value = "select s from VideoBulletIndex s where s.video_id = ?1")
    VideoBulletIndex findByVideoid(@Param("video_id") int videoid);

}
