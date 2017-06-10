package com.lexue.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;

/**
 * Created by UI03 on 2017/6/8.
 */
public class VideoBullet {
    /**
     * user  220 221 223
     * index 110 111 112
     * meta  你好 helloworld  外面的世界123
     */


    public static  void main(String args[]){

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

        //逆向转化出来
        outBytes(indexbytes,strbtlen) ;

        //写入到文件
        writeToFile(indexbytes) ;

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
    public static void writeToFile(byte[] indexbytes){
        String filename = "d://videoout.index";
        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(new File(filename));
            FileChannel fc = fos.getChannel();
            ByteBuffer bbf = ByteBuffer.allocate(1024);
            bbf.put(indexbytes) ;
            bbf.flip();
            fc.write(bbf) ;

            fc.close();
            fos.close();
        }catch (IOException e) {
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
