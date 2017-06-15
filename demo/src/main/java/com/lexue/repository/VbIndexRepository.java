package com.lexue.repository;


import com.lexue.domain.VbBullet;
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

    @Query(value = "select new com.lexue.domain.VbBullet(m.metaId,m.content,i.indexId ,i.timestamp ,u.userId) from VbIndex i ,VbUser u ,VbMeta m where i.indexId=u.indexId and i.metaId= m.metaId and m.display=0 and i.videoId=?1 ORDER BY i.timestamp ASC ")
    List<VbBullet> findBulletsByVideo(@Param("video_id") int videoId) ;
}
