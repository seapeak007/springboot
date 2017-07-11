import com.lexue.utils.BytesUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by UI03 on 2017/6/30.
 */
@Slf4j
public class MetaTest {
    public static void main(String args[]){
        String fileName ="D:/avideos/2135_356.meta" ;
        File infile = new File(fileName) ;
        FileInputStream fin = null;
        int capacity =200000 ;
        try {
            fin = new FileInputStream(infile);
            FileChannel channel = fin.getChannel();
            ByteBuffer bf = ByteBuffer.allocate(capacity);
//                    System.out.println("限制是：" + bf.limit() + "容量是：" + bf.capacity()+ "位置是：" + bf.position());

            //读一次就全部读取出文件来
            channel.read(bf) ;
            bf.flip() ;

            byte[] ibytes = bf.array() ;
            log.info("vid:"+BytesUtils.convertBytesToInt(ibytes,0)) ;
            log.info("index_offset:"+BytesUtils.convertBytesToLong(ibytes,16)) ;
            log.info("index_length:"+BytesUtils.convertBytesToLong(ibytes,24)) ;
            log.info("data_offset:"+BytesUtils.convertBytesToLong(ibytes,36)) ;
            log.info("data_length:"+BytesUtils.convertBytesToLong(ibytes,44)) ;
            int index_offset = Integer.valueOf(String.valueOf(BytesUtils.convertBytesToLong(ibytes,16))) ;
            log.info("index-timestamp:"+BytesUtils.convertBytesToLong(ibytes,index_offset)) ;
            log.info("index-meta_id:"+BytesUtils.convertBytesToLong(ibytes,index_offset+8)) ;
            log.info("index-user_id:"+BytesUtils.convertBytesToLong(ibytes,index_offset+16)) ;
            int data_offset = Integer.valueOf(String.valueOf(BytesUtils.convertBytesToLong(ibytes,36))) ;
            log.info("data-meta_id:"+BytesUtils.convertBytesToLong(ibytes,data_offset)) ;
            log.info("data-meta_length:"+BytesUtils.convertBytesToInt(ibytes,data_offset+8)) ;
            log.info("data-meta_body:"+BytesUtils.convertBytesToString(ibytes,data_offset+12,BytesUtils.convertBytesToInt(ibytes,data_offset+8))) ;


            channel.close();
            fin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


    }
}
