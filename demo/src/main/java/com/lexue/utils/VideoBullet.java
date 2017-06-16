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


        int im1 = "你好".hashCode()  ;
        int im2 = "helloworld".hashCode() ;
        int im3 = "外面的世界123".hashCode() ;

        HashMap<String,String> meta1 = new HashMap<String,String>() ;
        meta1.put("hashcode",String.valueOf(im1)) ;
        meta1.put("content","你好") ;
        meta1.put("count","1") ;
        HashMap<String,String> meta2 = new HashMap<String,String>() ;
        meta2.put("hashcode",String.valueOf(im2)) ;
        meta2.put("content","helloworld") ;
        meta2.put("count","2") ;
        HashMap<String,String> meta3 = new HashMap<String,String>() ;
        meta3.put("hashcode",String.valueOf(im3)) ;
        meta3.put("content","外面的世界123") ;
        meta3.put("count","3") ;

        int ii1=111;
        int ii2=112;
        int ii3=113;
        HashMap<String,Integer> user1 = new HashMap<String,Integer>() ;
        user1.put("index",ii1);
        user1.put("video",11);
        user1.put("hashcode",im1);
        user1.put("time",111111) ;
        HashMap<String,Integer> user2 = new HashMap<String,Integer>() ;
        user2.put("index",ii2);
        user2.put("video",11);
        user2.put("hashcode",im2);
        user2.put("time",111112) ;
        HashMap<String,Integer> user3 = new HashMap<String,Integer>() ;
        user3.put("index",ii3);
        user3.put("video",11);
        user3.put("hashcode",im3);
        user3.put("time",111113) ;

        long indexCount =2L ;
        int version =1;
        byte[] strbyte = ByteUtil.getBytes("你好世界，我是中国人") ;
        int strbtlen = strbyte.length ;
        byte[] indexbytes = new byte[4+4+8+8+4*(int)indexCount+strbyte.length] ;
        byte[] vbyte =ByteUtil.getBytes(111111) ;
        System.arraycopy(vbyte,0,indexbytes,0,vbyte.length);
        System.arraycopy(ByteUtil.getBytes(version) ,0,indexbytes,4,ByteUtil.getBytes(version).length);
        System.arraycopy(ByteUtil.getBytes(1111000L) ,0,indexbytes,8,ByteUtil.getBytes(1111000L).length);
        System.arraycopy(ByteUtil.getBytes(indexCount) ,0,indexbytes,16,ByteUtil.getBytes(indexCount).length);
        System.arraycopy(ByteUtil.getBytes(10) ,0,indexbytes,24,ByteUtil.getBytes(10).length);
        System.arraycopy(ByteUtil.getBytes(30) ,0,indexbytes,28,ByteUtil.getBytes(30).length);
        System.arraycopy(strbyte ,0,indexbytes,32,strbyte.length);

        System.out.println("aaa:"+indexbytes.length);
//        MessageDigest digest = MessageDigest.getInstance("MD5");
//        digest.update(indexbytes);
//        byte[] bmd = digest.digest() ;
        byte[] bmd = DigestUtils.MD5Digest(indexbytes) ;
        System.out.println(bmd.length);

        System.out.println("strbyte1:"+BytesUtils.convertStringToBytes("沙发客垃圾发").length);

        System.out.println("strbyte2:"+BytesUtils.convertStringToBytes("就个人而言，推荐使用目前最新的Camden版本与Spring Boot 1.4.x的组合。首先，不光光是Spring Boot版本提升带来的一些新功能，另外也由于Spring Cloud的组件版本也有提升，比如Brixton版本中的Spring Cloud Netflix采用了1.1.x，而Camden中采用了1.2.x，这两个版本之间还有不少区别的，在1.2.x中提供了更多实用功能，比如：之前在《为Spring Cloud Ribbon配置请求重试（Camden.SR2+）》一文中提到的RestTemplate的请求重试、关于Zuul的一些头信息优化等。").length);

        System.out.println("hashcode1:"+"aksfd;jaklfa的克拉克".hashCode());
        System.out.println("hashcode2:"+"就个人而言，推荐使用目前最新的Camden版本与Spring Boot 1.4.x的组合".hashCode());

        //逆向转化出来
        outBytes(indexbytes,strbtlen) ;

        //写入到文件
        String filename = "d://videoout.index";
        FileUtils.writeToFile(indexbytes,filename) ;

        System.out.println("写入到文件 ok") ;

        //读取文件
        readFromFile(strbtlen);

        System.out.println("读取文件 ok") ;
    }

    public static void outBytes(byte[] indexbytes,int strlength){
        byte[]  ibyte = new byte[4] ;
        byte[]  lbyte = new byte[8] ;
        byte[]  sbyte = new byte[strlength] ;
        for(int i=0;i<7;i++){
            switch (i) {
                case 0:
                    System.arraycopy(indexbytes,0,ibyte,0,4) ;
                    System.out.println("i="+i+";value="+ByteUtil.getInt(ibyte));
                    break ;
                case 1:
                    System.arraycopy(indexbytes,4,ibyte,0,4) ;
                    System.out.println("i="+i+";value="+ByteUtil.getInt(ibyte));
                    break ;
                case 2:
                    System.arraycopy(indexbytes,8,lbyte,0,8) ;
                    System.out.println("i="+i+";value="+ByteUtil.getLong(lbyte));
                    break ;
                case 3:
                    System.arraycopy(indexbytes,16,lbyte,0,8) ;
                    System.out.println("i="+i+";value="+ByteUtil.getLong(lbyte));
                    break ;
                case 4:
                    System.arraycopy(indexbytes,24,ibyte,0,4);
                    System.out.println("i="+i+";value="+ByteUtil.getInt(ibyte));
                    break ;
                case 5:
                    System.arraycopy(indexbytes,28,ibyte,0,4);
                    System.out.println("i="+i+";value="+ByteUtil.getInt(ibyte));
                    break ;
                case 6:
                    System.arraycopy(indexbytes,32,sbyte,0,strlength);
                    System.out.println("i="+i+";value="+ByteUtil.getString(sbyte));
                    break ;
            }

        }
    }


    public static void readFromFile(int strbtlen){
        String pathname = "d://videoout.index";
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(new File(pathname));
            FileChannel channel = fin.getChannel();

            int capacity = 1024;// 字节
            ByteBuffer bf = ByteBuffer.allocate(capacity);
            System.out.println("限制是：" + bf.limit() + "容量是：" + bf.capacity()
                    + "位置是：" + bf.position());
            int length = -1;

            while ((length = channel.read(bf)) != -1) {

                /*
                 * 注意，读取后，将位置置为0，将limit置为容量, 以备下次读入到字节缓冲中，从0开始存储
                 */
                bf.clear();
                byte[] bytes  = bf.array();
//                System.out.write(bytes , 0, length);
                System.out.println("-------------------");

                System.out.println("限制是：" + bf.limit() + "容量是：" + bf.capacity()
                        + "位置是：" + bf.position());

                outBytes(bytes,strbtlen) ;

            }

            channel.close();

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
