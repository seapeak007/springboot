package com.lexue.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by UI03 on 2017/6/12.
 */
@Data
@Entity
@Table(name="video_bullet_meta")
public class VideoBulletMeta {
    @Id
    private long video_bullet_meta ;
    private String video_bullet_content ;
    private short content_type ;
    private long count ;
    private short display ;

}
