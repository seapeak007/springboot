package com.lexue.service;

import com.lexue.domainscd.ChatHistory;
import com.lexue.domainscd.ChatHistoryVideo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by UI03 on 2017/6/23.
 */
public interface ChatService {
    long  queryChatCount() ;
    List<ChatHistory> queryAllChats() ;


    long queryChatVideoCount() ;
    List<ChatHistoryVideo> queryAllChatVideos();

    Page<ChatHistory> queryChatsByPage(Pageable p) ;
}
