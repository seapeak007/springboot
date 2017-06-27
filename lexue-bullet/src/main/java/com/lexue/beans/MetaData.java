package com.lexue.beans;

import com.lexue.utils.BytesUtils;
import lombok.Data;

/**
 * Created by UI03 on 2017/6/12.
 */
@Data
public class MetaData {
    private long meta_id ;
    /**
     * 元数据文本长度
     */
    private int	meta_length ;
    private byte[]  meta_body ;

    public byte[] bulid(){
        if(this.meta_id >0){
            byte[] rtn = new byte[8+4+this.meta_body.length] ;
            System.arraycopy(BytesUtils.convertLongToBytes(this.meta_id),0,rtn,0,8);
            System.arraycopy(BytesUtils.convertIntToBytes(this.meta_length),0,rtn,8,4);
            System.arraycopy(meta_body,0,rtn,12,meta_body.length);
            return rtn ;
        }else{
            return null ;
        }

    }

}
