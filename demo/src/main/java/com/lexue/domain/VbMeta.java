package com.lexue.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by UI03 on 2017/6/12.
 */
@Data
@Entity
@Table(name="vbmeta")
public class VbMeta {
    @Id
    @Column(name = "meta_id")
    private long metaId ;
    private String content ;
    @Column(name = "content_type")
    private short contentType ;
    private long count ;
    private short display ;
    @Column(name ="create_time")
    private long createTime ;
    @Column(name ="update_time")
    private long updateTime ;

}
