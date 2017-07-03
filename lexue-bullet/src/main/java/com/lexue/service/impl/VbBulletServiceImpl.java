package com.lexue.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lexue.config.CacheConfig;
import com.lexue.domain.*;
import com.lexue.domainscd.Livez;
import com.lexue.http.CommonResponse;
import com.lexue.repository.*;
import com.lexue.repositoryscd.LivezRepository;
import com.lexue.service.VbBulletService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by UI03 on 2017/6/15.
 */

@Slf4j
@Service
public class VbBulletServiceImpl implements VbBulletService {

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

    private final VbIndexRepository vbIndexRepository ;
    @Autowired
    private  LiveRepository liveRepository ;
    @Autowired
    private VbMetaRepository vbMetaRepository ;
    @Autowired
    private VbUserRepository vbUserRepository ;
    @Autowired
    private VbConfigRepository vbConfigRepository ;
    @Autowired
    private LivezRepository livezRepository;
    @Autowired
    private final RestTemplate restTemplate;

    @Autowired
    public  VbBulletServiceImpl (VbIndexRepository  vbIndexRepository ,RestTemplate restTemplate){
        this.vbIndexRepository =  vbIndexRepository ;
        this.restTemplate = restTemplate ;
    }

    public List<VbBullet> queryVbBulletsByVideoid(long timestamp , int video_id) {
        return vbIndexRepository.findBulletsByVideo(timestamp,video_id) ;
    }

    public Page<VbBullet> queryVbBulletsPageByVideoid(long timestamp , int video_id , Pageable pageable){
        return  vbIndexRepository.findBulletsPageByVideo(timestamp,video_id,pageable) ;
    }

    public int queryVbBulletsCountByVideoid(long timestamp , int video_id ){
        return  vbIndexRepository.queryVbBulletsCountByVideoid(timestamp,video_id) ;
    }

    /**
     * 录播弹幕入库
     * @param uid
     * @param video_id
     * @param chat_time 偏移
     * @param content
     * @return
     */
    public CommonResponse genVideoBullet(int uid, int videoId, int chat_time , String content ,int msg_type){
        CommonResponse crp = null ;
        if(!("".equals(content) || videoId <1)){
            try{
                VbMeta meta = vbMetaRepository.queryByContent(content) ;
                long time = System.currentTimeMillis()/1000 ;
                if(null==meta){
                    log.info("new meta content");
                    meta = new VbMeta() ;
                    meta.setContent(content);
                    meta.setContentType(Short.valueOf(String.valueOf(msg_type)));
                    meta.setCount(Long.valueOf("1"));
                    meta.setCreateTime(time);
                    meta.setDisplay(Short.valueOf("0"));
                }else{
                    log.info("meta content exist");
                    meta.setCount(meta.getCount()+1);
                    meta.setUpdateTime(time);
                }
                meta = vbMetaRepository.save(meta) ;
                long metaId = meta.getMetaId() ;
                int offset =  chat_time <0 ? 0:chat_time;
                int indextime = (offset/vacuateTime)*vacuateTime;
                VbIndex index = vbIndexRepository.queryIndexByVideoTimeMeta(videoId,Long.valueOf(String.valueOf(indextime)),metaId) ;
                boolean idxexist = false ;
                if(null==index){
                    log.info("new index");
                    index = new VbIndex();
                    index.setCreateTime(time);
                    index.setMetaId(metaId);
                    index.setTimestamp(Long.valueOf(String.valueOf(indextime)));
                    index.setVideoId(videoId);
                    index = vbIndexRepository.save(index) ;
                }else {
                    log.info("index exist");
                    idxexist=true ;
                }

                if(vacuateFlag & idxexist){
                    log.info("index vacuate ignore user message");
                }else{
                    log.info("index vacuate not ignore user message");
                    VbUser user = new VbUser();
                    user.setCreateTime(time);
                    user.setIndexId(index.getIndexId());
                    user.setTimestamp(chat_time);
                    user.setUid(uid);
                    user.setVideoId(videoId);
                    vbUserRepository.save(user) ;
                }

                updateConfig(videoId)  ;

                crp = new CommonResponse(1000,"success") ;
            }catch (Exception e){
                log.error("addLiveBullets error:"+e);
                crp = new CommonResponse(1003,e.getMessage()) ;
            }
        }else{
            log.info("信息不完整，忽略该信息content："+content);
        }
        return crp ;
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
    }

//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    private static class RyMsgHisRtn {
//        private int code;
//        private String url;
//        private String date;
//    }

    /**
     * 定时生成直播弹幕
      */
//    public void genLiveBullets(){

//        Long dealtime =System.currentTimeMillis()/1000 -3600*2 ; //处理前2个小时的一个小时内数据
//        Date dealDate = new Date(Long.valueOf(String.valueOf(dealtime)+"000")) ;
//        String datastr = DateUtils.getDateFormat(dealDate,"yyyyMMddHH") ;
//        String params = "date="+datastr ;
//        Gson gson = new Gson();
//        try{
//            Random r = new Random(1000000) ;
//            String nonce = String.valueOf(r.nextInt() ) ;
//            String timestamp = String.valueOf(System.currentTimeMillis()/1000) ;
//            String signature = SHA1Util.getSHA(appsec+nonce+timestamp)  ;
//
//            HttpHeaders headers = new HttpHeaders();
//            MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded");
//            headers.setContentType(type);
//            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
//            headers.add("App-Key",appid);
//            headers.add("Nonce",nonce);
//            headers.add("Timestamp",timestamp);
//            headers.add("Signature",signature);
//
//            HttpEntity<String> formEntity = new HttpEntity<String>(params, headers);
//            RyMsgHisRtn rymsg = restTemplate.postForObject(rymsgurl, formEntity, RyMsgHisRtn.class);
//
//            if(null!=rymsg && 200==rymsg.getCode()){
//                if(null==rymsg.getUrl() || "".equals(rymsg.getUrl())){
//                    log.info("this date have no value,data:"+datastr) ;
//                }else{
//                    //zip下载以及处理
//                    String zipurl =rymsg.getUrl() ;
//                    log.info("zipurl:"+zipurl);
//                    boolean downFlag=false ;
//                    for(int i=0;i<3;i++){
//                        if(FileUtils.downloadzip(zipurl,zippath,datastr+".zip") ){
//                            downFlag = true ;
//                            break ;
//                        }
//                    }
//                    if(downFlag){
//                        try{
//                            ZipFile zf = new ZipFile(zippath+datastr+".zip");
//                            InputStream in = new BufferedInputStream(new FileInputStream(zippath+datastr+".zip"));
//                            ZipInputStream zin = new ZipInputStream(in);
//                            ZipEntry ze;
//                            while ((ze = zin.getNextEntry()) != null) {
//                                if (ze.isDirectory()) {
//                                } else {
//                                    log.info("file - " + ze.getName() + " : "+ ze.getSize() + " bytes");
//                                    long size = ze.getSize();
//                                    if (size > 0) {
//                                        BufferedReader br = new BufferedReader(
//                                                new InputStreamReader(zf.getInputStream(ze)));
//                                        String line;
//                                        while ((line = br.readLine()) != null) {
////                                            log.info("line:"+line);
////                                            String chattime = line.substring(0,19) ;//2017/06/17 22:00:04
//                                            Date cdate = DateUtils.formatDate(line.substring(0,19),"yyyy/MM/dd HH:mm:ss") ;
//                                            String chatstamp = String.valueOf(cdate.getTime()/1000 );
//
//                                            String value = line.substring(19);
//                                            log.info("value:"+value);
//                                            JsonObject jsonObj = gson.fromJson(value,JsonObject.class) ;
//                                            if("RC:TxtMsg".equals(jsonObj.get("classname").getAsString())){
//                                                log.info("聊天信息");
//                                                String content = jsonObj.get("content").getAsJsonObject().get("content").getAsString() ;
//                                                addLiveBullets(jsonObj.get("fromUserId").getAsInt() , jsonObj.get("targetId").getAsInt() , content ,
//                                                                    Integer.valueOf(chatstamp) ,jsonObj.get("targetType").getAsInt()  ) ;
//                                            }
//                                        }
//                                        br.close();
//                                    }
//                                }
//                            }
//                            zin.closeEntry();
//                            in.close();
//                            zin.close();
//                            zf.close();
//                        }catch (IOException e){
//                            log.error("read from local zip error:"+e);
//                        }
//
//                    }else{
//                        log.error("down from rongyun fail");
//                    }
//                }
//            }else{
//               log.error("genLiveBullets live error:"+rymsg);
//            }
//        }catch (Exception e ){
//            log.error("genLiveBullets live error:"+e);
//        }

//    }

    /**
     * 查询直播Live
     * @param liveroom
     * @return
     */
//    @Cacheable(CacheConfig.LIVE_ROOM)
//    private Live queryLiveByRoom(int liveroom){
//        log.info("go repository");
//        return liveRepository.queryLiveByRoom(liveroom) ;
//    }

    /**
     * repositoryscd数据库，导数据时使用
     * @param liveroom
     * @return
     */
//    @Cacheable(CacheConfig.LIVE_ROOM)
//    private Live queryLiveByRoom(int liveroom){
//        log.info("go repository");
//        Livez lz = livezRepository.queryLivezByRoom(liveroom) ;
//        Live l = new Live() ;
//        l.setVideoId(lz.getVideoId());
//        l.setRoomId(lz.getRoomId());
//        l.setStartTime(lz.getStartTime());
//        return l ;
//    }

    /**
     * 直播弹幕入库
     * @param uid
     * @param liveroom
     * @param content
     * @param chat_time
     * @param msg_type
     */
//    public void addLiveBullets(int uid , int liveroom , String content ,int chat_time ,int msg_type ){
//        if(!("".equals(content) || liveroom <1)){
//            try{
//                Live live = queryLiveByRoom(liveroom) ;
//                if(live !=null){
//                    VbMeta meta = vbMetaRepository.queryByContent(content) ;
//                    long time = System.currentTimeMillis()/1000 ;
//                    if(null==meta){
//                        log.info("new meta content");
//                        meta = new VbMeta() ;
//                        meta.setContent(content);
//                        meta.setContentType(Short.valueOf(String.valueOf(msg_type)));
//                        meta.setCount(Long.valueOf("1"));
//                        meta.setCreateTime(time);
//                        meta.setDisplay(Short.valueOf("0"));
//                    }else{
//                        log.info("meta content exist");
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
//                        log.info("new index");
//                        index = new VbIndex();
//                        index.setCreateTime(time);
//                        index.setMetaId(metaId);
//                        index.setTimestamp(Long.valueOf(String.valueOf(indextime)));
//                        index.setVideoId(videoId);
//                        index = vbIndexRepository.save(index) ;
//                    }else {
//                        log.info("index exist");
//                        idxexist=true ;
//                    }
//
//                    if(vacuateFlag & idxexist){
//                        log.info("index vacuate ignore user message");
//                    }else{
//                        log.info("index vacuate not ignore user message");
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

//    }


//    public void addLocalZip(String filename){
//        Gson gson = new Gson() ;
//        try{
//            ZipFile zf = new ZipFile(zippath+filename+".zip");
//            InputStream in = new BufferedInputStream(new FileInputStream(zippath+filename+".zip"));
//            ZipInputStream zin = new ZipInputStream(in);
//            ZipEntry ze;
//            while ((ze = zin.getNextEntry()) != null) {
//                if (ze.isDirectory()) {
//                } else {
//                    log.info("file - " + ze.getName() + " : "+ ze.getSize() + " bytes");
//                    long size = ze.getSize();
//                    if (size > 0) {
//                        BufferedReader br = new BufferedReader(
//                                new InputStreamReader(zf.getInputStream(ze)));
//                        String line;
//                        while ((line = br.readLine()) != null) {
////                                            log.info("line:"+line);
////                                            String chattime = line.substring(0,19) ;//2017/06/17 22:00:04
//                            Date cdate = DateUtils.formatDate(line.substring(0,19),"yyyy/MM/dd HH:mm:ss") ;
//                            String chatstamp = String.valueOf(cdate.getTime()/1000 );
//
//                            String value = line.substring(19);
//                            log.info("value:"+value);
//                            JsonObject jsonObj = gson.fromJson(value,JsonObject.class) ;
//                            if("RC:TxtMsg".equals(jsonObj.get("classname").getAsString())){
//                                log.info("聊天信息");
//                                String content = jsonObj.get("content").getAsJsonObject().get("content").getAsString() ;
//                                addLiveBullets(jsonObj.get("fromUserId").getAsInt() , jsonObj.get("targetId").getAsInt() , content ,
//                                        Integer.valueOf(chatstamp) ,jsonObj.get("targetType").getAsInt()  ) ;
//                            }
//                        }
//                        br.close();
//                    }
//                }
//            }
//            zin.closeEntry();
//            in.close();
//            zin.close();
//            zf.close();
//        }catch (IOException e){
//            log.error("read from local zip error:"+e);
//        }
//        log.info("over");
//    }
}
