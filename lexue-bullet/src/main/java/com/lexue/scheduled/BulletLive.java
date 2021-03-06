package com.lexue.scheduled;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lexue.config.CacheConfig;
import com.lexue.domain.*;
import com.lexue.repository.*;
import com.lexue.scheduled.queue.IndexConsumer;
import com.lexue.scheduled.queue.IndexForQueue;
import com.lexue.scheduled.queue.IndexProducer;
import com.lexue.service.CacheService;
import com.lexue.utils.DateUtils;
import com.lexue.utils.FileUtils;
import com.lexue.utils.SHA1Util;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by UI03 on 2017/6/26.
 */
@Lazy(false)
@Slf4j
@Component
public class BulletLive {

    @Value("${rongyun.app.id}")
    private String appid;
    @Value("${rongyun.app.sec}")
    private String appsec ;
    @Value("${rongyun.msg.url}")
    private String rymsgurl ;
    @Value("${bullet.rongyun.zip.path}")
    private String zippath ;
    @Value("${bullet.index.vacuate.ignore}")
    private boolean vacuateFlag ;
    @Value("${bullet.index.vacuate.time}")
    private int vacuateTime ;
    @Value("${lexue.client}")
    private String client ;


    @Autowired
    private VbIndexRepository vbIndexRepository ;
    @Autowired
    private VbMetaRepository vbMetaRepository ;
    @Autowired
    private VbUserRepository vbUserRepository ;
    @Autowired
    private VbConfigRepository vbConfigRepository ;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CacheService cacheService ;

    private static BlockingQueue<IndexForQueue> metaQueue = new ArrayBlockingQueue<IndexForQueue>(100) ;
    private static BlockingQueue<VbIndex> indexQueue = new ArrayBlockingQueue<VbIndex>(100) ;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class RyMsgHisRtn {
        private int code;
        private String url;
        private String date;
    }

    @Scheduled(cron = "${bullet.live.cron}")
    public void liveBullets(){
        log.info("start liveBullets:"+new Date());
        Long dealtime =System.currentTimeMillis()/1000 -3600*2 ; //处理前2个小时的一个小时内数据
        Date dealDate = new Date(Long.valueOf(String.valueOf(dealtime)+"000")) ;
        String datastr = DateUtils.getDateFormat(dealDate,"yyyyMMddHH") ;
//        datastr="2017071016" ;
        String params = "date="+datastr ;
        Gson gson = new Gson();
        try{
            Random r = new Random(1000000) ;
            String nonce = String.valueOf(r.nextInt() ) ;
            String timestamp = String.valueOf(System.currentTimeMillis()/1000) ;
            String signature = SHA1Util.getSHA(appsec+nonce+timestamp)  ;

            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded");
            headers.setContentType(type);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            headers.add("App-Key",appid);
            headers.add("Nonce",nonce);
            headers.add("Timestamp",timestamp);
            headers.add("Signature",signature);

            HttpEntity<String> formEntity = new HttpEntity<String>(params, headers);
            RyMsgHisRtn rymsg = restTemplate.postForObject(rymsgurl, formEntity, RyMsgHisRtn.class);

            if(null!=rymsg && 200==rymsg.getCode()){
                if(null==rymsg.getUrl() || "".equals(rymsg.getUrl())){
                    log.info("this date have no value,data:"+datastr) ;
                }else{
                    //zip下载以及处理
                    String zipurl =rymsg.getUrl() ;
                    log.info("zipurl:"+zipurl);
                    boolean downFlag=false ;
                    for(int i=0;i<3;i++){
                        if(FileUtils.downloadzip(zipurl,zippath,datastr+".zip") ){
                            downFlag = true ;
                            break ;
                        }
                    }
                    if(downFlag){
                        try{
                            ZipFile zf = new ZipFile(zippath+datastr+".zip");
                            InputStream in = new BufferedInputStream(new FileInputStream(zippath+datastr+".zip"));
                            ZipInputStream zin = new ZipInputStream(in);
                            ZipEntry ze;
                            while ((ze = zin.getNextEntry()) != null) {
                                if (ze.isDirectory()) {
                                } else {
                                    log.info("file:" + ze.getName() + "=="+ ze.getSize() + " bytes");
                                    long size = ze.getSize();
                                    if (size > 0) {
                                        BufferedReader br = new BufferedReader(
                                                new InputStreamReader(zf.getInputStream(ze)));
                                        String line;
                                        while ((line = br.readLine()) != null) {
//                                            log.info("line:"+line);
//                                            String chattime = line.substring(0,19) ;//2017/06/17 22:00:04
                                            Date cdate = DateUtils.formatDate(line.substring(0,19),"yyyy/MM/dd HH:mm:ss") ;
                                            String chatstamp = String.valueOf(cdate.getTime()/1000 );

                                            String value = line.substring(19);
//                                            log.info("value:"+value);
                                            JsonObject jsonObj = gson.fromJson(value,JsonObject.class) ;
                                            if("RC:TxtMsg".equals(jsonObj.get("classname").getAsString())){
//                                                log.info("聊天信息");
                                                String content = jsonObj.get("content").getAsJsonObject().get("content").getAsString() ;
                                                addLiveBullets(jsonObj.get("fromUserId").getAsInt() , jsonObj.get("targetId").getAsInt() , content ,
                                                        Integer.valueOf(chatstamp) ,jsonObj.get("targetType").getAsInt()  ) ;
                                            }
                                        }
                                        br.close();
                                    }
                                }
                            }
                            zin.closeEntry();
                            in.close();
                            zin.close();
                            zf.close();
                        }catch (IOException e){
                            log.error("read from local zip error:"+e);
                        }

                    }else{
                        log.error("down from rongyun fail");
                    }
                }
            }else{
                log.error("genLiveBullets live error:"+rymsg);
            }
        }catch (Exception e ){
            log.error("genLiveBullets live error:"+e);
        }
        log.info("end liveBullets:"+new Date());
    }

    /**
     * 直播弹幕入库 这个单线程的效率太低
     * @param uid
     * @param liveroom
     * @param content
     * @param chat_time
     * @param msg_type
     */
//    public void addLiveBullets(int uid , int liveroom , String content ,int chat_time ,int msg_type ){
//        log.info("addLiveBullets start:"+new Date());
//        if(!("".equals(content) || liveroom <1)){
//            try{
//                Live live = queryLiveByRoom(liveroom) ;
//                if(live !=null){
//                    VbMeta meta = vbMetaRepository.queryByContent(content) ;
//                    long time = System.currentTimeMillis()/1000 ;
//                    if(null==meta){
////                        log.info("new meta content");
//                        meta = new VbMeta() ;
//                        meta.setContent(content);
//                        meta.setContentType(Short.valueOf(String.valueOf(msg_type)));
//                        meta.setCount(Long.valueOf("1"));
//                        meta.setCreateTime(time);
//                        meta.setDisplay(Short.valueOf("0"));
//                    }else{
////                        log.info("meta content exist");
//                        meta.setCount(meta.getCount()+1);
//                        meta.setUpdateTime(time);
//                    }
//                    meta = vbMetaRepository.save(meta) ;
//                    long metaId = meta.getMetaId() ;
//                    int timestamp = chat_time - live.getStartTime() ;
//                    int offset =  timestamp <0 ? 0:timestamp;
//                    int indextime = (offset/vacuateTime)*vacuateTime;
//                    int videoId = live.getVideoId() ;
//                    VbIndex index = vbIndexRepository.queryIndexByVideoTimeMeta(videoId,Long.valueOf(String.valueOf(indextime)),metaId) ;
//                    boolean idxexist = false ;
//                    if(null==index){
////                        log.info("new index");
//                        index = new VbIndex();
//                        index.setCreateTime(time);
//                        index.setMetaId(metaId);
//                        index.setTimestamp(Long.valueOf(String.valueOf(indextime)));
//                        index.setVideoId(videoId);
//                        index = vbIndexRepository.save(index) ;
//                    }else {
////                        log.info("index exist");
//                        idxexist=true ;
//                    }
//
//                    if(vacuateFlag & idxexist){
////                        log.info("index vacuate ignore user message");
//                    }else{
////                        log.info("index vacuate not ignore user message");
//                        VbUser user = new VbUser();
//                        user.setCreateTime(time);
//                        user.setIndexId(index.getIndexId());
//                        user.setTimestamp(timestamp);
//                        user.setUid(uid);
//                        user.setVideoId(videoId);
//                        vbUserRepository.save(user) ;
//                    }
//
//                    VbConfig vc = vbConfigRepository.findOne(videoId) ;
//                    if(vc==null || "0".equals(vc.getUpdateStatus())){
//                        updateConfig(videoId) ;
//                    }
//                }else{
//                    log.info("not query live by liveroom:"+liveroom);
//                }
//            }catch (Exception e){
//                log.error("addLiveBullets error:"+e);
//            }
//
//        }else{
//            log.info("信息不完整，忽略该信息content："+content);
//        }
//        log.info("addLiveBullets end:"+new Date());
//
//    }
    private static Thread indexpro=null ;
    ExecutorService userservice = Executors.newFixedThreadPool(10);

    /**
     * 定时监控线程是否挂掉
     */
    @Scheduled(cron="0/30 * *  * * ? " )
    private void checkThread(){
        if(indexpro ==null || !indexpro.isAlive()){
            indexpro = new Thread(new IndexProducer(metaQueue,indexQueue,vbIndexRepository)) ;
            indexpro.start();
            log.warn("indexThread new start");
        }
    }
    /**
     * 直播弹幕入库
     * @param uid
     * @param liveroom
     * @param content
     * @param chat_time
     * @param msg_type
     */
    public void addLiveBullets(int uid , int liveroom , String content ,int chat_time ,int msg_type ){
        log.info("addLiveBullets start:"+new Date());
        if(!("".equals(content) || liveroom <1)){
            try{
                Live live = cacheService.queryLiveByRoom(liveroom) ;
                if(live !=null){
                    VbMeta meta = vbMetaRepository.queryByContent(content) ;
                    long time = System.currentTimeMillis()/1000 ;
                    if(null==meta){
                        meta = new VbMeta() ;
                        meta.setContent(content);
                        meta.setContentType(Short.valueOf(String.valueOf(msg_type)));
                        meta.setCount(Long.valueOf("1"));
                        meta.setCreateTime(time);
                        meta.setDisplay(Short.valueOf("0"));
                    }else{
                        meta.setCount(meta.getCount()+1);
                        meta.setUpdateTime(time);
                    }
                    meta = vbMetaRepository.save(meta) ;

                    IndexForQueue ifq = new IndexForQueue() ;
                    ifq.setVbMeta(meta);
                    ifq.setVideoId(live.getVideoId());
                    ifq.setChat_time(chat_time);
                    ifq.setLiveStartTime(live.getStartTime());
                    ifq.setTime(time);
                    ifq.setVacuateTime(vacuateTime);
                    metaQueue.put(ifq);
                    log.info("meta deal end:"+new Date());

                    Thread indexcons = new Thread(new IndexConsumer(indexQueue,vbUserRepository,vbConfigRepository,time,chat_time,live.getStartTime(),uid,live.getVideoId())) ;
                    userservice.execute(indexcons);

                }else{
                    log.info("not query live by liveroom:"+liveroom);
                }
            }catch (Exception e){
                log.error("addLiveBullets error:"+e);
            }

        }else{
            log.info("信息不完整，忽略该信息content："+content);
        }
        log.info("addLiveBullets end:"+new Date());

    }

}
