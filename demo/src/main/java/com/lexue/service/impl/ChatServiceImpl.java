package com.lexue.service.impl;

import com.lexue.domainscd.ChatHistory;
import com.lexue.domainscd.ChatHistoryVideo;
import com.lexue.repositoryscd.ChatRepository;
import com.lexue.repositoryscd.ChatVideoRepository;
import com.lexue.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by UI03 on 2017/6/23.
 */
@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatRepository chatRepository ;
    @Autowired
    private ChatVideoRepository chatVideoRepository ;


    @Override
    public long queryChatCount() {
        return chatRepository.count() ;
    }

    @Override
    public List<ChatHistory> queryAllChats() {
        return chatRepository.findAll() ;
    }

    @Override
    public long queryChatVideoCount() {
        return chatVideoRepository.count();
    }

    @Override
    public List<ChatHistoryVideo> queryAllChatVideos() {
        return chatVideoRepository.findAll();
    }
    @Override
    public Page<ChatHistory>  queryChatsByPage(Pageable p){
        return  chatRepository.queryByPage(p) ;
    }
}
