package com.lexue.repositoryscd;

import com.lexue.domainscd.ChatHistoryVideo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by UI03 on 2017/6/23.
 */
public interface ChatVideoRepository extends JpaRepository<ChatHistoryVideo,Long> {
}
