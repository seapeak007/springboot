package com.lexue.beans;

import com.lexue.utils.BytesUtils;
import lombok.Data;

/**
 * Created by UI03 on 2017/6/12.
 */
@Data
public class MetaHeader {
    private int vid ;
    private int ver ;
    /**
     * 跟总文件的时间戳一致
     */
    private long timestamp ;
    private long index_offset ;
    private long index_length ;
    private int index_record_length ;
    private long data_offset ;
    private long data_length ;
    private int data_record_length ;

    public byte[] bulid(){
        if(this.vid >0){
            byte[] rtn = new byte[4+4+8+8+8+4+8+8+4] ;
            System.arraycopy(BytesUtils.convertIntToBytes(this.vid),0,rtn,0,4);
            System.arraycopy(BytesUtils.convertIntToBytes(this.ver),0,rtn,4,4);
            System.arraycopy(BytesUtils.convertLongToBytes(this.timestamp),0,rtn,8,8);
            System.arraycopy(BytesUtils.convertLongToBytes(this.index_offset),0,rtn,16,8);
            System.arraycopy(BytesUtils.convertLongToBytes(this.index_length),0,rtn,24,8);
            System.arraycopy(BytesUtils.convertIntToBytes(this.index_record_length),0,rtn,32,4);
            System.arraycopy(BytesUtils.convertLongToBytes(this.data_offset),0,rtn,36,8);
            System.arraycopy(BytesUtils.convertLongToBytes(this.data_length),0,rtn,44,8);
            System.arraycopy(BytesUtils.convertIntToBytes(this.data_record_length),0,rtn,52,4);
            return rtn ;
        }else{
            return null ;
        }

    }
}
