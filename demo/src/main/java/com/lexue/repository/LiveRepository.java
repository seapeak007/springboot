package com.lexue.repository;

import com.lexue.domain.Live;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by UI03 on 2017/6/22.
 */
public interface LiveRepository  extends JpaRepository<Live,Integer>{

    @Query(value = "select l from Live l where l.roomId = ?1")
    Live queryLiveByRoom(@Param("roomId") int roomId) ;
    @Query(value = "select  l from Live l where l.videoId= ?1")
    Live queryLiveByVideo(@Param("videoId") int videoId) ;
}
