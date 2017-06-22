package com.lexue.repository;

import com.lexue.domain.VbMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by UI03 on 2017/6/12.
 */
public interface VbMetaRepository extends JpaRepository<VbMeta,Long> {

    @Query(value = "select  m from VbMeta m where m.content = ?1")
    VbMeta queryByContent(@Param("content") String content) ;
}
