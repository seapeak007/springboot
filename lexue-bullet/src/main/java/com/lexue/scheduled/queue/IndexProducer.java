package com.lexue.scheduled.queue;

import com.lexue.domain.VbIndex;
import com.lexue.domain.VbMeta;
import com.lexue.repository.VbIndexRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

/**
 * Created by UI03 on 2017/7/12.
 */
@Slf4j
public class IndexProducer implements Runnable {
    private BlockingQueue<IndexForQueue>  metaqueue ;
    private BlockingQueue<VbIndex> indexqueue ;
    private VbIndexRepository vbIndexRepository ;
    private  int chat_time ;
    private int liveStartTime ;
    private int vacuateTime ;
    private int videoId ;
    private long time;
    public IndexProducer(BlockingQueue<IndexForQueue> mq,BlockingQueue<VbIndex> iq,VbIndexRepository vbIndexRepository){
        this.metaqueue=mq;
        this.indexqueue=iq ;
        this.vbIndexRepository =vbIndexRepository ;
    }

    @Override
    public void run(){
        while (true){
            log.info("index producer ,metaqueue size:"+metaqueue.size()+";indexqueue size:"+indexqueue.size());
            try{
                IndexForQueue ifq = metaqueue.take() ;
                VbMeta meta = ifq.getVbMeta() ;
                chat_time = ifq.getChat_time() ;
                liveStartTime = ifq.getLiveStartTime() ;
                vacuateTime = ifq.getVacuateTime() ;
                videoId = ifq.getVideoId() ;
                time = ifq.getTime() ;

                long metaId = meta.getMetaId() ;
                int timestamp = chat_time - liveStartTime ;
                int offset =  timestamp <0 ? 0:timestamp;
                int indextime = (offset/vacuateTime)*vacuateTime;
                VbIndex index = vbIndexRepository.queryIndexByVideoTimeMeta(videoId,Long.valueOf(String.valueOf(indextime)),metaId) ;
//            boolean idxexist = false ;
                if(null==index){
                    index = new VbIndex();
                    index.setCreateTime(time);
                    index.setMetaId(metaId);
                    index.setTimestamp(Long.valueOf(String.valueOf(indextime)));
                    index.setVideoId(videoId);
                    index = vbIndexRepository.save(index) ;
                }

                indexqueue.put(index);
            } catch (InterruptedException e) {
                log.error("indexproducer run error:"+e);
                e.printStackTrace();
            }
        }
    }
}
