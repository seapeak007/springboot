package com.lexue.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by UI03 on 2017/6/9.
 */
@Data
@Entity
@Table(name="vbindex")
public class VbIndex {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "index_id")
    private long indexId ;
    @Column(name = "video_id")
    private int videoId ;
    @Column(name = "meta_id")
    private long metaId ;
    /**
     * 偏移时间，方便同一个内容，多个用户同时发送，做抽稀记录使用
     */
    private long timestamp ;

    @Column(name ="create_time")
    private long createTime ;
}
