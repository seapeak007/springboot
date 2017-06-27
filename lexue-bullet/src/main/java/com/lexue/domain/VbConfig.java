package com.lexue.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by UI03 on 2017/6/22.
 */
@Data
@Entity
@Table(name = "vbconfig")
public class VbConfig {
    @Id
    @Column(name = "video_id")
    private int videoId ;
    @Column(name="update_status")
    private int updateStatus ;
    @Column(name="update_time")
    private long updateTime ;
    private int version ;
    private String client ;
}
