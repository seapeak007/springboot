package com.lexue.restful;

import com.lexue.domain.VbIndex;
import com.lexue.service.VbIndexService;
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
public class VbIndexController {

    private final VbIndexService vbIndexService;

    @Autowired
    public VbIndexController(VbIndexService vbIndexService){
        this.vbIndexService = vbIndexService ;
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String  hello (){
        System.out.println("hello") ;
        return "hello world " ;
    }

    @RequestMapping(value = "/bullet", method = RequestMethod.GET)
    public String  genBullet(@RequestParam int video_id){

        List<VbIndex> vblist =  this.vbIndexService.queryVbIndexByVideoid(video_id);
        return vblist.toString() ;
    }
}
