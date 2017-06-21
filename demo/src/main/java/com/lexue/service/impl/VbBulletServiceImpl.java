package com.lexue.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lexue.domain.VbBullet;
import com.lexue.repository.VbIndexRepository;
import com.lexue.service.VbBulletService;
import com.lexue.utils.DateUtils;
import com.lexue.utils.SHA1Util;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Random;

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
    @Value("${rongyun.bullet.file}")
    private String bulletfile ;
    @Value("${rongyun.msg.url}")
    private String rymsgurl ;

    private final VbIndexRepository vbIndexRepository ;
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


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class RyMsgHisRtn {
        private int code;
        private String url;
        private String date;
    }

    public void genLiveBullets(){

        String datastr = DateUtils.getDateFormat(new Date(),"yyyyMMddHH") ;
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

//            JsonObject jsonObj = gson.fromJson(params,JsonObject.class) ;
            HttpEntity<String> formEntity = new HttpEntity<String>(params, headers);
            RyMsgHisRtn rymsg = restTemplate.postForObject(rymsgurl, formEntity, RyMsgHisRtn.class);
            if(null!=rymsg && "200".equals(rymsg.getCode())){
                if(null==rymsg.getUrl() || "".equals(rymsg.getUrl())){
                    log.info("this date have no value,data:"+datastr) ;
                }else{
                    //zip下载以及处理



                }
                log.info("rymsg:"+rymsg);
            }else{
               log.error("genLiveBullets live error:"+rymsg);
            }
        }catch (Exception e ){
            log.error("genLiveBullets live error:"+e);
        }

    }
}
