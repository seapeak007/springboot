package com.lexue.beans;

import com.lexue.utils.BytesUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by UI03 on 2017/6/12.
 */
@Data
public class IndexHeader {

    private int	vid ;
    private int	ver ;
    private int index_record_length  ;
    private long timestamp ;
    private int count ;

    private ArrayList<HashMap> datas ;

    public byte[] bulid(){
        if(this.vid >0){
            byte[] rtn = new byte[4+4+4+8+4+(4+16)*count] ;
            System.arraycopy(BytesUtils.convertIntToBytes(this.vid),0,rtn,0,4);
            System.arraycopy(BytesUtils.convertIntToBytes(this.ver),0,rtn,4,4);
            System.arraycopy(BytesUtils.convertIntToBytes(this.index_record_length),0,rtn,8,4);
            System.arraycopy(BytesUtils.convertLongToBytes(this.timestamp),0,rtn,12,8);
            System.arraycopy(BytesUtils.convertIntToBytes(this.count),0,rtn,20,4);
            for(int i=0;i<datas.size() ;i++){
                HashMap m = datas.get(i) ;
                System.arraycopy(BytesUtils.convertIntToBytes(Integer.valueOf(m.get("offset").toString())),0,rtn,24+20*i,4);
                System.arraycopy((byte[])m.get("mdbyte"),0,rtn,28+20*i,16);
            }
            return rtn ;
        }else{
            return null ;
        }

    }


}
