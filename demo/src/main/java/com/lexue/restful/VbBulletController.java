package com.lexue.restful;

import com.lexue.beans.IndexHeader;
import com.lexue.beans.MetaData;
import com.lexue.beans.MetaHeader;
import com.lexue.beans.MetaIndex;
import com.lexue.common.PageUtils;
import com.lexue.domain.VbBullet;
import com.lexue.service.VbBulletService;
import com.lexue.service.VbIndexService;
import com.lexue.service.VbMetaService;
import com.lexue.service.VbUserService;
import com.lexue.utils.BytesUtils;
import com.lexue.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by UI03 on 2017/6/9.
 */
@RestController
@RequestMapping(value="/video")
@Slf4j
public class VbBulletController {

    private final VbIndexService vbIndexService;
    private final VbMetaService vbMetaService ;
    private final VbUserService vbUserService ;
    private final VbBulletService  vbBulletService ;

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


    @Autowired
    public VbBulletController(VbIndexService vbIndexService, VbMetaService vbMetaService, VbUserService vbUserService,VbBulletService vbBulletService){
        this.vbIndexService = vbIndexService ;
        this.vbMetaService = vbMetaService ;
        this.vbUserService = vbUserService ;
        this.vbBulletService = vbBulletService ;
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String  hello (){
        System.out.println("hello") ;
        return "hello world " ;
    }

    @RequestMapping(value="/livebullet",method =RequestMethod.GET )
    public String  livebullet(){
        vbBulletService.genLiveBullets();
        return  "hello world" ;
    }

    @RequestMapping(value = "/bullet", method = RequestMethod.GET)
    public String  genBullet(@RequestParam int video_id){

        long filetimes = System.currentTimeMillis() /1000 ;
        log.info("genBullet start:"+new Date()+",filetimes:"+filetimes);
        ArrayList<HashMap> vilist = new ArrayList<HashMap>() ;//整个文件包含几个分片文件
        int ibytelen =0 ;
        int mbytelen = 0;
        Set metaSet = new HashSet() ;
        String headtemp = filePath+video_id+"/"+video_id+"header.temp" ;
        String indextemp = filePath+video_id+"/"+video_id+"index.temp" ;
        String metatemp = filePath+video_id+"/"+video_id+"meta.temp" ;

        FileUtils.deleteFilesByDirectory(filePath+video_id+"/");
        FileUtils.createDirectories(filePath+video_id+"/","rwxr-x---");

        int dataCount = this.vbBulletService.queryVbBulletsCountByVideoid(filetimes,video_id) ;
        int dealCount = dataCount /pagesize +2 ;

        for(int j=1;j<dealCount ;j++){
            PageRequest p = PageUtils.buildPageRequest(j,pagesize,"");
            Page<VbBullet> pl = this.vbBulletService.queryVbBulletsPageByVideoid(filetimes,video_id,p) ;

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

                    FileUtils.writeToFile(mh.bulid(),headtemp,true) ;
                    log.info("合并记录长度ibytelen||mbytelen："+ibytelen+"||"+mbytelen);


                    //超过了一个文件，需要合并文件并返回文件ＭＤ５值，然后重新开始写新的文件
                    ArrayList<String> fileArray = new ArrayList<String>() ;
                    fileArray.add(headtemp) ;
                    fileArray.add(indextemp) ;
                    fileArray.add(metatemp) ;
                    String comfile = filePath +video_id+"/" +video_id+"_"+b.getTimestamp()+".meta" ;

                    HashMap m = new HashMap() ;
                    m.put("offset",b.getTimestamp()) ;
                    m.put("mdbyte", FileUtils.combineFiles(fileArray,comfile)) ;

                    vilist.add(m) ;

                    //删除temp临时文件
                    FileUtils.deleteFiles(fileArray) ;

                    //初始新文件的写
                    ibytelen =0 ;
                    mbytelen = 0;
                    metaSet = new HashSet() ;

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
                index.setUser_role(Short.valueOf("1"));//请求用户接口，然后赋值，待开发

                byte[] indexbyte = index.bulid() ;
                FileUtils.writeToFile(indexbyte,indextemp,true) ;
                ibytelen = ibytelen + indexbyte.length ;

                if(metaSet.contains(data.getMeta_id())){
                    log.info("meta 已存在");
                }else{
                    byte[] metabyte = data.bulid() ;
                    FileUtils.writeToFile(metabyte,metatemp,true) ;
                    mbytelen = mbytelen + metabyte.length ;
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

                    FileUtils.writeToFile(mh.bulid(),headtemp,true) ;
                    log.info(ibytelen+"||"+mbytelen);

                    //超过了一个文件，需要合并文件并返回文件ＭＤ５值，然后重新开始写新的文件
                    ArrayList<String> fileArray = new ArrayList<String>() ;
                    fileArray.add(headtemp) ;
                    fileArray.add(indextemp) ;
                    fileArray.add(metatemp) ;
                    String comfile = filePath +video_id+"/" +video_id+"_"+lastoffset+".meta" ;

                    HashMap m = new HashMap() ;
                    m.put("offset",lastoffset) ;
                    m.put("mdbyte", FileUtils.combineFiles(fileArray,comfile)) ;

                    vilist.add(m) ;

                    //删除temp临时文件
                    FileUtils.deleteFiles(fileArray) ;
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

        log.info("genBullet end:"+new Date());
        return "over" ;


    }



}
