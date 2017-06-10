package com.lexue.jpa.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by UI03 on 2017/6/9.
 */
@Data
@Entity
@Table(name="video_bullet_index")
public class VideoBulletIndex implements Serializable {

    @Id
    private int video_bullet_index ;
    private int video_id ;
    private String video_bullet_meta ;
    private int timestamp ;
}
