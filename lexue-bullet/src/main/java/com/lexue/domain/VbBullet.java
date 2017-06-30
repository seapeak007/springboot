package com.lexue.domain;

import lombok.Data;

import javax.persistence.Id;

/**
 * Created by UI03 on 2017/6/15.
 */
@Data
public class VbBullet {
    private long metaId ;
    private String content ;
    private long indexId ;
    private long timestamp ;
    private long userId ;

    public VbBullet(long metaId,String content, long indexId ,long timestamp ,long userId){
        this.metaId =metaId ;
        this.content = content ;
        this.indexId = indexId ;
        this.timestamp = timestamp ;
        this.userId = userId ;
    }
    public VbBullet(long metaId,String content, long indexId ,long timestamp ,int uid){
        this.metaId =metaId ;
        this.content = content ;
        this.indexId = indexId ;
        this.timestamp = timestamp ;
        this.userId = Long.valueOf(String.valueOf(uid)) ;
    }
}
