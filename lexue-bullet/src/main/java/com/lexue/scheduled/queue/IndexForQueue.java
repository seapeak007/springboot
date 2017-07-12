package com.lexue.scheduled.queue;

import com.lexue.domain.VbMeta;
import lombok.Data;

/**
 * Created by UI03 on 2017/7/12.
 */
@Data
public class IndexForQueue {
    VbMeta vbMeta ;
    private  int chat_time ;
    private int liveStartTime ;
    private int vacuateTime ;
    private int videoId ;
    private long time;
}
