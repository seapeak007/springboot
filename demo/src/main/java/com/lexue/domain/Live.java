package com.lexue.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by UI03 on 2017/6/22.
 */
@Data
@Entity
@Table(name = "live")
public class Live {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "live_id")
    private int id;
    @Column(name = "live_name")
    private String name;
    @Column(name = "live_cover")
    private String cover;
    @Column(name = "live_status")
    private int liveStatus;
    @Column(name = "orig_price")
    private int origPrice;
    @Column(name = "real_price")
    private int realPrice;
    @Column(name = "price_type")
    private int priceType;
    @Column(name = "live_start")
    private int startTime;
    @Column(name = "live_end")
    private int endTime;
    @Column(name = "left_head")
    private int leftHead;
    @Column(name = "total_head")
    private int totalHead;
    @Column(name = "live_subject_id")
    private int subjectId;
    @Column(name = "live_subject_name")
    private String subjectName;
    @Column(name = "product_id")
    private int productId;
    @Column(name = "live_description")
    private String description;
    @Column(name="teacher_id")
    private int teacherId;
    @Column(name = "live_chat_room")
    private int roomId;
    @Column(name = "has_bought")
    private boolean hasBought;
    @Column(name = "live_time_left")
    private long timeLeft;
    @Column(name = "video_id")
    private int videoId;
    @Column(name = "video_name")
    private String videoName;
    @Column(name = "download_url")
    private String downloadUrl;
}
