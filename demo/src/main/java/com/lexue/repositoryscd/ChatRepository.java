package com.lexue.repositoryscd;

import com.lexue.domainscd.ChatHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by UI03 on 2017/6/23.
 */
public interface ChatRepository extends JpaRepository<ChatHistory,Long> {

        @Query("select c from ChatHistory c ")
        Page<ChatHistory>  queryByPage(Pageable p) ;
}
