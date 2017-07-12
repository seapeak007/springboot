package com.lexue.scheduled.queue;

import com.lexue.domain.VbConfig;
import com.lexue.domain.VbIndex;
import com.lexue.domain.VbUser;
import com.lexue.repository.VbConfigRepository;
import com.lexue.repository.VbUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.concurrent.BlockingQueue;

/**
 * Created by UI03 on 2017/7/12.
 */
@Slf4j
public class IndexConsumer implements Runnable {
    private BlockingQueue<VbIndex> indexQueue ;
    private VbUserRepository vbUserRepository ;
    private VbConfigRepository vbConfigRepository ;
    private long time ;
    private  int chat_time ;
    private int liveStartTime ;
    private int uid ;
    private int videoId ;
    @Value("${lexue.client}")
    private String client ;

    public IndexConsumer(BlockingQueue<VbIndex> iq,VbUserRepository vbUserRepository,VbConfigRepository vbConfigRepository,long time,int chat_time,int liveStartTime ,int uid,int videoId){
        this.indexQueue =iq ;
        this.vbUserRepository = vbUserRepository ;
        this.vbConfigRepository= vbConfigRepository ;
        this.time=time ;
        this.chat_time =chat_time ;
        this.liveStartTime = liveStartTime ;
        this.uid =uid ;
        this.videoId = videoId ;
    }

    @Override
    public void run(){
        log.info("indexConsumer run,indexQueue size:"+indexQueue.size());
        try{
            int timestamp = (chat_time - liveStartTime) <0?0:(chat_time - liveStartTime) ;
            VbIndex index = indexQueue.take() ;
            VbUser user = new VbUser();
            user.setCreateTime(time);
            user.setIndexId(index.getIndexId());
            user.setTimestamp(timestamp);
            user.setUid(uid);
            user.setVideoId(videoId);
            vbUserRepository.save(user) ;

            VbConfig vc = vbConfigRepository.findOne(videoId) ;
            if(vc==null || "0".equals(vc.getUpdateStatus())){
                updateConfig(videoId) ;
            }
        } catch (InterruptedException e) {
            log.error("indexConsumer error:"+e);
            e.printStackTrace();
        }
    }

    /**
     * 修改弹幕配置表
     * @param videoId
     */
    private void updateConfig(int videoId){
        VbConfig vc = vbConfigRepository.findOne(videoId) ;
        long time = System.currentTimeMillis()/1000 ;
        if(null==vc){
            log.info("new video config");
            vc = new VbConfig() ;
            vc.setVideoId(videoId);
            vc.setUpdateStatus(1);
            vc.setUpdateTime(time);
            vc.setVersion(1);
            vc.setClient(client);
        }else{
            log.info("update video config");
            vc.setUpdateStatus(1);
            vc.setUpdateTime(time);
        }
        vbConfigRepository.save(vc) ;
        log.info("updateConfig end:"+new Date());
    }
}
