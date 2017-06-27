package com.lexue.scheduled;

import com.lexue.beans.IndexHeader;
import com.lexue.beans.MetaData;
import com.lexue.beans.MetaHeader;
import com.lexue.beans.MetaIndex;
import com.lexue.common.PageUtils;
import com.lexue.config.CacheConfig;
import com.lexue.domain.VbBullet;
import com.lexue.domain.VbConfig;
import com.lexue.repository.VbConfigRepository;
import com.lexue.service.UserRoleService;
import com.lexue.service.VbBulletService;
import com.lexue.service.VbConfigService;
import com.lexue.utils.BytesUtils;
import com.lexue.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by UI03 on 2017/6/26.
 */
@Lazy(false)
@Slf4j
@Component
public class BulletFile {

    @Autowired
    private  VbBulletService vbBulletService ;
    @Autowired
    private VbConfigService vbConfigService ;
    @Autowired
    private UserRoleService userRoleService ;
    @Autowired
    private VbConfigRepository vbConfigRepository ;

    @Value("${lexue.client}")
    private String client ;
    @Value("${bullet.file.bytelen}")
    private int bftlen ;
    @Value("${bullet.file.path}")
    private String filePath ;
    @Value("${bullet.file.ver}")
    private int ver ;
    @Value("${bullet.meta.index.len}")
    private int metaIndexlen ;
    @Value("${bullet.meta.data.len}")
    private int metaDatalen ;
    @Value("${bullet.index.record.len}")
    private int indexRecordlen ;
    @Value("${bullet.query.pagesize}")
    private int pagesize ;

    private Set<Long> teaUsers =null ;
    /**
     * 定时生成所有符合条件的video_id弹幕
     */
    @Scheduled(cron = "${bullet.file.cron}")
    public void genBulletFiles(){
        log.info("start genBulletFiles:"+new Date());
        long time = System.currentTimeMillis() /1000 ;
        List<VbConfig> vclist = vbConfigService.queryUpdateConfig(client,time) ;
        for(VbConfig vc:vclist){
            genBulletFile(vc.getVideoId()) ;
        }
        log.info("end genBulletFiles:"+new Date());
    }

    /**
     * 生成video_id的弹幕文件
     * @param video_id
     */
    private void genBulletFile(int video_id){
        log.info("gen video bullet start:"+video_id+"="+new Date());
        long filetimes = System.currentTimeMillis() /1000 ;
        ArrayList<HashMap> vilist = new ArrayList<HashMap>() ;//整个文件包含几个分片文件
        int ibytelen =0 ;
        int mbytelen = 0;
        Set metaSet = new HashSet() ;
//        String headtemp = filePath+video_id+"/"+filetimes+"/"+video_id+"header.temp" ;
//        String indextemp = filePath+video_id+"/"+filetimes+"/"+video_id+"index.temp" ;
//        String metatemp = filePath+video_id+"/"+filetimes+"/"+video_id+"meta.temp" ;
        List<byte[]> headlist = new ArrayList<byte[]>() ;
        List<byte[]> indexlist = new ArrayList<byte[]>() ;
        List<byte[]> metalist = new ArrayList<byte[]>() ;


//        FileUtils.deleteFilesByDirectory(filePath+video_id+"/");
        FileUtils.createDirectories(filePath+video_id+"/"+filetimes+"/","rwxr-x---");

        int dataCount = this.vbBulletService.queryVbBulletsCountByVideoid(filetimes,video_id) ;
        int dealCount = dataCount /pagesize +2 ;
        if(pagesize*(dataCount /pagesize) ==dataCount){
            log.info("页数整除的情况");
            dealCount = dealCount -1 ;
        }

        for(int j=1;j<dealCount ;j++){
            PageRequest p = PageUtils.buildPageRequest(j,pagesize,"");
            log.info("query db start:"+new Date());
            Page<VbBullet> pl = this.vbBulletService.queryVbBulletsPageByVideoid(filetimes,video_id,p) ;
            log.info("query db end:"+new Date());
            for(VbBullet b:pl.getContent()){
                if(ibytelen+mbytelen > bftlen){

                    //写入MetaHeader文件
                    MetaHeader mh = new MetaHeader() ;
                    mh.setVid(video_id);
                    mh.setVer(ver);
                    mh.setTimestamp(filetimes);
                    mh.setIndex_offset(Long.valueOf("56"));//header的长度
                    mh.setIndex_length(Long.valueOf(String.valueOf(ibytelen)));
                    mh.setIndex_record_length(metaIndexlen);
                    mh.setData_offset(Long.valueOf(String.valueOf(56+ibytelen)));
                    mh.setData_length(Long.valueOf(String.valueOf(mbytelen)));
                    mh.setData_record_length(metaDatalen);

//                    FileUtils.writeToFile(mh.bulid(),headtemp,true) ;
                    headlist.add(mh.bulid()) ;
                    log.info("合并记录长度ibytelen||mbytelen："+ibytelen+"||"+mbytelen);


                    //超过了一个文件，需要合并文件并返回文件ＭＤ５值，然后重新开始写新的文件
//                    ArrayList<String> fileArray = new ArrayList<String>() ;
//                    fileArray.add(headtemp) ;
//                    fileArray.add(indextemp) ;
//                    fileArray.add(metatemp) ;
                    ArrayList<List<byte[]>> bytesArray = new ArrayList<List<byte[]>>() ;
                    bytesArray.add(headlist) ;
                    bytesArray.add(indexlist) ;
                    bytesArray.add(metalist) ;
                    String comfile = filePath +video_id+"/"+filetimes+"/" +video_id+"_"+b.getTimestamp()+".meta" ;

                    HashMap m = new HashMap() ;
                    m.put("offset",b.getTimestamp()) ;
//                    m.put("mdbyte", FileUtils.combineFiles(fileArray,comfile)) ;
                    log.info("comfile start:"+new Date());
                    m.put("mdbyte", FileUtils.combineBytesFiles(bytesArray,comfile)) ;
                    log.info("comfile end:"+new Date());
                    vilist.add(m) ;

                    //删除temp临时文件
//                    FileUtils.deleteFiles(fileArray) ;

                    //初始新文件的写
                    ibytelen =0 ;
                    mbytelen = 0;
                    metaSet = new HashSet() ;
                    headlist.clear();
                    indexlist.clear();
                    metalist.clear();

                }

                MetaData data = new MetaData() ;
                data.setMeta_id(b.getMetaId());
                String content = b.getContent() ;
                data.setMeta_body(BytesUtils.convertStringToBytes(content));
                data.setMeta_length(BytesUtils.convertStringToBytes(content).length);

                MetaIndex index = new MetaIndex() ;
                index.setMeta_id(b.getMetaId());
                index.setTimestamp(b.getTimestamp());
                index.setUser_id(b.getUserId());
                index.setUser_role(queryUserRole(b.getUserId()));//请求用户接口

                byte[] indexbyte = index.bulid() ;
//                FileUtils.writeToFile(indexbyte,indextemp,true) ;
                indexlist.add(indexbyte) ;
                ibytelen = ibytelen + indexbyte.length ;

                if(metaSet.contains(data.getMeta_id())){
//                    log.info("meta 已存在");
                }else{
                    byte[] metabyte = data.bulid() ;
//                    FileUtils.writeToFile(metabyte,metatemp,true) ;
                    metalist.add(metabyte) ;
                    mbytelen = mbytelen + metabyte.length ;
                    metaSet.add(data.getMeta_id()) ;
                }

            }

            if(j==dealCount-1 & pl.getContent().size() >0){ //最后一次查询，剩余数据写入meta文件，写Index总索引文件
                int size = pl.getContent().size() ;
                long lastoffset = pl.getContent().get(size-1).getTimestamp() ;
                if(ibytelen >0){
                    //写入MetaHeader文件
                    MetaHeader mh = new MetaHeader() ;
                    mh.setVid(video_id);
                    mh.setVer(ver);
                    mh.setTimestamp(filetimes);
                    mh.setIndex_offset(Long.valueOf("56"));//header的长度
                    mh.setIndex_length(Long.valueOf(String.valueOf(ibytelen)));
                    mh.setIndex_record_length(metaIndexlen);
                    mh.setData_offset(Long.valueOf(String.valueOf(56+ibytelen)));
                    mh.setData_length(Long.valueOf(String.valueOf(mbytelen)));
                    mh.setData_record_length(metaDatalen);

//                    FileUtils.writeToFile(mh.bulid(),headtemp,true) ;
                    headlist.add(mh.bulid()) ;
                    log.info("合并记录长度ibytelen||mbytelen："+ibytelen+"||"+mbytelen);

                    ArrayList<List<byte[]>> bytesArray = new ArrayList<List<byte[]>>() ;
                    bytesArray.add(headlist) ;
                    bytesArray.add(indexlist) ;
                    bytesArray.add(metalist) ;

                    String comfile = filePath +video_id+"/"+filetimes+"/" +video_id+"_"+lastoffset+".meta" ;

                    HashMap m = new HashMap() ;
                    m.put("offset",lastoffset) ;
                    m.put("mdbyte", FileUtils.combineBytesFiles(bytesArray,comfile)) ;

                    vilist.add(m) ;

                    //删除temp临时文件
//                    FileUtils.deleteFiles(fileArray) ;

                    //清空一下
                    headlist.clear();
                    indexlist.clear();
                    metalist.clear();
                }

            }
        }

        //写总index文件
        IndexHeader ih = new IndexHeader() ;
        ih.setVid(video_id);
        ih.setVer(ver);
        ih.setIndex_record_length(indexRecordlen);
        ih.setTimestamp(filetimes);
        ih.setCount(vilist.size());
        ih.setDatas(vilist);
        String indexfile = filePath +video_id+"/" +video_id+".index" ;
        FileUtils.writeToFile(ih.bulid(),indexfile,true) ;

        //删除上一次产生的meta文件，即子文件夹
        FileUtils.delFilesExcludeParam(filePath+video_id+"/",String.valueOf(filetimes));
        updateConfig(video_id) ;
        log.info("gen video bullet end:"+video_id+"="+new Date());
    }

    /**
     * 查询用户role
     * @param user_id
     * @return
     */
    @Cacheable(CacheConfig.USER_ROLE)
    private void queryUserMap(){
        this.teaUsers = userRoleService.queryTeaUsers() ;
    }

    private short  queryUserRole(long user_id){
        if(this.teaUsers==null){
            queryUserMap();
        }
        if(this.teaUsers.contains(user_id)){
            return Short.valueOf("1") ;
        }else{
            return Short.valueOf("0") ;
        }
    }

    /**
     * 修改配置表
     * @param videoId
     */
    private void updateConfig(int videoId){
        VbConfig vc = vbConfigRepository.findOne(videoId) ;
        long time = System.currentTimeMillis()/1000 ;
        if(null==vc){
            log.info("error config data");
        }else{
//            log.info("update video config");
            vc.setUpdateStatus(0);
            vc.setVersion(vc.getVersion()+1);
            vc.setUpdateTime(time);
        }
        vbConfigRepository.save(vc) ;
//        log.info("updateConfig end:"+new Date());
    }
}
