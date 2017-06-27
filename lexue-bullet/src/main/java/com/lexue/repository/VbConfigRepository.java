package com.lexue.repository;

import com.lexue.domain.VbConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by UI03 on 2017/6/22.
 */
public interface VbConfigRepository extends JpaRepository<VbConfig,Integer> {

    @Query("select f from VbConfig f where f.updateStatus=1 and f.client=?1 and f.updateTime+3600 >?2")
    List<VbConfig> queryUpdateConfig(@Param("client") String client ,@Param("time") long time) ;

}
