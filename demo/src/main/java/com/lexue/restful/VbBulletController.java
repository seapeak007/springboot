package com.lexue.restful;

import com.lexue.common.PageUtils;
import com.lexue.domain.VbBullet;
import com.lexue.domain.VbIndex;
import com.lexue.domain.VbMeta;
import com.lexue.service.VbBulletService;
import com.lexue.service.VbIndexService;
import com.lexue.service.VbMetaService;
import com.lexue.service.VbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by UI03 on 2017/6/9.
 */
@RestController
@RequestMapping(value="/video")
public class VbBulletController {

    private final VbIndexService vbIndexService;
    private final VbMetaService vbMetaService ;
    private final VbUserService vbUserService ;
    private final VbBulletService  vbBulletService ;

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

    @RequestMapping(value = "/bullet", method = RequestMethod.GET)
    public String  genBullet(@RequestParam int video_id ,@RequestParam int pageNum){

//        List<VbIndex> vblist =  this.vbIndexService.queryVbIndexByVideoid(video_id);
//        return vblist.toString() ;
//        List<VbMeta> metalist = this.vbMetaService.getMetas() ;
//        return metalist.toString() ;
        long timestamp =200 ;
//        List<VbBullet> bulletlist = this.vbIndexService.queryVbBulletsByVideoid(timestamp ,video_id) ;
//        return bulletlist.toString() ;
        PageRequest p = PageUtils.buildPageRequest(pageNum,3,"");
        Page<VbBullet> pl = this.vbBulletService.queryVbBulletsPageByVideoid(timestamp,video_id,p) ;

        return pl.getContent().toString() ;


    }
}
