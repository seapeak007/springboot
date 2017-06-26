package com.lexue.domainscd;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by UI03 on 2017/6/23.
 */
@Data
@Entity
@Table(name = "chat_history_video")
public class ChatHistoryVideo {
    @Id
    private long id ;
    private int user_id ;
    private String message ;
    private int chat_time ;
    private int msg_type ;
    private int video_id ;
}
