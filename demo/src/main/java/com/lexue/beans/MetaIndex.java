package com.lexue.beans;

import com.lexue.utils.BytesUtils;
import lombok.Data;

import java.util.ArrayList;

/**
 * Created by UI03 on 2017/6/12.
 */
@Data
public class MetaIndex {
    private long timestamp ;
    private long meta_id ;
    private long user_id ;
    private short user_role ;


    public byte[] bulid(){
        if(this.timestamp >0){
            byte[] rtn = new byte[8+8+8+2] ;
            System.arraycopy(BytesUtils.convertLongToBytes(this.timestamp),0,rtn,0,8);
            System.arraycopy(BytesUtils.convertLongToBytes(this.meta_id),0,rtn,8,8);
            System.arraycopy(BytesUtils.convertLongToBytes(this.user_id),0,rtn,16,8);
            System.arraycopy(BytesUtils.convertShortToBytes(this.user_role),0,rtn,24,2);
            return rtn ;
        }else{
            return null ;
        }

    }


}
