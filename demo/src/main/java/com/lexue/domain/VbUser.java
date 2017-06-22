package com.lexue.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by UI03 on 2017/6/12.
 */
@Data
@Entity
@Table(name="vbuser")
public class VbUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="user_id")
    private long userId ;
    private long timestamp ;
    private int uid ;
    @Column(name = "index_id")
    private long indexId ;
    @Column(name = "video_id")
    private int videoId ;
    @Column(name ="create_time")
    private long createTime ;

}
