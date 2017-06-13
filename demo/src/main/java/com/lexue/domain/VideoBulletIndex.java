package com.lexue.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by UI03 on 2017/6/9.
 */
@Data
@Entity
@Table(name="video_bullet_index")
public class VideoBulletIndex implements Serializable {

    @Id
    private long video_bullet_index ;
    private int video_id ;
    private long video_bullet_meta ;
    /**
     * 偏移时间，方便同一个内容，多个用户同时发送，做抽稀记录使用
     */
    private long timestamp ;

}
