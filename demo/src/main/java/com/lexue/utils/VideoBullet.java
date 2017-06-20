package com.lexue.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;

/**
 * Created by UI03 on 2017/6/8.
 */
@Slf4j
public class VideoBullet {
    /**
     * user  220 221 223
     * index 110 111 112
     * meta  你好 helloworld  外面的世界123
     */

    /**
     * 转码采用对象的方式进行模块处理，index  meta  header分别对象处理，便于后续维护
     * 转码的工具类，在新的项目Tools中有转码工具BytesUtils，跟类型识别工具类
     * 每个类中增加一个bulid方法，生成本类的二进制bytes[]
     * @param args
     */

    public static  void main(String args[]) throws Exception{

        HashMap<String,String> index = new HashMap<String,String>() ;

        //读取文件
        byte[] bs =readFromFile("D:/avideos/1/1_7.meta",2048);

        log.info(""+BytesUtils.convertBytesToInt(bs,0));
        log.info(""+BytesUtils.convertBytesToInt(bs,4));
        log.info(""+BytesUtils.convertBytesToLong(bs,8));
        log.info(""+BytesUtils.convertBytesToLong(bs,16));
        log.info(""+BytesUtils.convertBytesToLong(bs,24));
        log.info(""+BytesUtils.convertBytesToInt(bs,32));
        log.info(""+BytesUtils.convertBytesToLong(bs,36));
        log.info(""+BytesUtils.convertBytesToLong(bs,44));
        log.info(""+BytesUtils.convertBytesToInt(bs,52));
        log.info("header read end");
        log.info(""+BytesUtils.convertBytesToLong(bs,56));
        log.info(""+BytesUtils.convertBytesToLong(bs,64));
        log.info(""+BytesUtils.convertBytesToLong(bs,72));
        log.info(""+BytesUtils.convertBytesToShort(bs,80));
        log.info("index one read end");
        log.info(""+BytesUtils.convertBytesToLong(bs,290));
        log.info(""+BytesUtils.convertBytesToInt(bs,298));

        Long temp = Long.MAX_VALUE;
        log.info("max long:"+temp);
        FileUtils.writeToFile(BytesUtils.convertLongToBytes(temp),"d:/long.meta",true) ;

        FileOutputStream fos = null;
        File file = new File("d:/avideos/1/1.index") ;
        byte[] bytes = BytesUtils.convertIntToBytes(9) ;
        int rtn =0 ;
        try {
            fos = new FileOutputStream(file,true);
            FileChannel fc = fos.getChannel();

            ByteBuffer bbf = ByteBuffer.wrap(bytes);
            bbf.put(bytes) ;
            bbf.flip();
            fc.write(bbf,300) ;

            fc.close();
            fos.flush();
            fos.close();
        }catch (IOException e) {
            rtn = 1 ;
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     *
     * @param pathname
     * @param capacity
     * @return
     */
    public static byte[] readFromFile(String pathname,int capacity){
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(new File(pathname));
            FileChannel channel = fin.getChannel();

            ByteBuffer bf = ByteBuffer.allocate(capacity);
            System.out.println("限制是：" + bf.limit() + "容量是：" + bf.capacity()
                    + "位置是：" + bf.position());
            int length = -1;
            channel.read(bf) ;
            byte[] b = bf.array() ;
            log.info("bytes length:"+b.length);

            channel.close();
            fin.close();

            return b ;

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
        return null ;
    }


 }
