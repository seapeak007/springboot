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
public class IndexTest {
    public static void main(String args[]){
        String fileName ="D:/avideos/2135.index" ;
        File infile = new File(fileName) ;
        FileInputStream fin = null;
        int capacity =1000 ;
        try {
            fin = new FileInputStream(infile);
            FileChannel channel = fin.getChannel();
            ByteBuffer bf = ByteBuffer.allocate(capacity);
//                    System.out.println("限制是：" + bf.limit() + "容量是：" + bf.capacity()+ "位置是：" + bf.position());

            //读一次就全部读取出文件来
            channel.read(bf) ;
            bf.flip() ;

            byte[] ibytes = bf.array() ;
            log.info(""+BytesUtils.convertBytesToInt(ibytes,0)) ;
            log.info(""+BytesUtils.convertBytesToLong(ibytes,12)) ;


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
