package com.lexue.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by UI03 on 2017/6/12.
 */
@Data
@Entity
@Table(name="video_bullet_user")
public class VideoBulletUser implements Serializable {
    @Id
    private long video_bullet_user ;
    private long timestamp ;
    private int uid ;
    private short user_role ;
    private long   video_bullet_index ;

}
