package com.lexue.repositoryscd;

import com.lexue.domainscd.Livez;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by UI03 on 2017/6/22.
 */
public interface LivezRepository extends JpaRepository<Livez,Integer>{

    @Query(value = "select l from Livez l where l.roomId = ?1")
    Livez queryLivezByRoom(@Param("roomId") int roomId) ;
    @Query(value = "select  l from Livez l where l.videoId= ?1")
    Livez queryLivezByVideo(@Param("videoId") int videoId) ;
}
