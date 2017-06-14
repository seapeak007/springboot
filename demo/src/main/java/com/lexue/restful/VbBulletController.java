package com.lexue.restful;

import com.lexue.domain.VbIndex;
import com.lexue.domain.VbMeta;
import com.lexue.service.VbIndexService;
import com.lexue.service.VbMetaService;
import com.lexue.service.VbUserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public VbBulletController(VbIndexService vbIndexService, VbMetaService vbMetaService, VbUserService vbUserService){
        this.vbIndexService = vbIndexService ;
        this.vbMetaService = vbMetaService ;
        this.vbUserService = vbUserService ;
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String  hello (){
        System.out.println("hello") ;
        return "hello world " ;
    }

    @RequestMapping(value = "/bullet", method = RequestMethod.GET)
    public String  genBullet(@RequestParam int video_id){

//        List<VbIndex> vblist =  this.vbIndexService.queryVbIndexByVideoid(video_id);
//        return vblist.toString() ;
        List<VbMeta> metalist = this.vbMetaService.getMetas() ;
        return metalist.toString() ;
    }
}
