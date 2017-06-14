package com.lexue.repository;


import com.lexue.domain.VbIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by UI03 on 2017/6/9.
 */

public interface VbIndexRepository extends JpaRepository<VbIndex,Long> {

    @Query(value = " select  f from VbIndex f where f.videoId=?1")
    List<VbIndex> findByVideoId(@Param("video_id") int videoId) ;
}
