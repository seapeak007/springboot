package com.lexue.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Set;

import static org.apache.catalina.startup.ExpandWar.deleteDir;

/**
 * Created by UI03 on 2017/6/16.
 */
@Slf4j
public class FileUtils {

    /**
     *该方法仅支持最终文件大小小于512KB的文件写入
     * @param bytes
     * @param filename
     * @param appendable  是否追加写入文件
     * @return
     */
    public static int writeToFile(byte[] bytes,String filename,boolean appendable){
        File file = new File(filename) ;
            if(!file.exists()){
                createFile(filename,"rwxr-x---") ;
            }
            FileOutputStream fos = null;
            int rtn =0 ;
            try {

                fos = new FileOutputStream(file,appendable);
                FileChannel fc = fos.getChannel();

                ByteBuffer bbf = ByteBuffer.wrap(bytes);
                bbf.put(bytes) ;
                bbf.flip();
                fc.write(bbf) ;

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

        return rtn ;
    }

    /**
     * 合并list中的小文件，到目标文件
     * @param fileArray 被合并文件list
     * @param combinefile 合并目标文件
     * @return 返回目标文件的MD5 byte[]
     */
    public static byte[] combineFiles(ArrayList<String> fileArray,String combinefile){
        FileOutputStream fos =null ;
        MessageDigest md =null ;
        File combfile = new File(combinefile) ;
        if(!combfile.exists()){
            createFile(combinefile,"rwxr-x---") ;
        }
        try{
            md = MessageDigest.getInstance("MD5");
            fos = new FileOutputStream(combfile);
            FileChannel outchannel = fos.getChannel();
            int capacity = 1024;// 字节

            for(String infile : fileArray){
                FileInputStream fin = null;
                try {
                    fin = new FileInputStream(new File(infile));
                    FileChannel channel = fin.getChannel();
                    ByteBuffer bf = ByteBuffer.allocate(capacity);
//                    System.out.println("限制是：" + bf.limit() + "容量是：" + bf.capacity()+ "位置是：" + bf.position());
                    int length = -1;
                    while ((length = channel.read(bf)) != -1) {

                        //将当前位置置为limit，然后设置当前位置为0，也就是从0到limit这块，都写入到同道中
                        bf.flip();

                        md.update(bf.array(),0,length);

                        int outlength =0;
                        while((outlength=outchannel.write(bf)) != 0){
                            log.info("读，"+length+"写,"+outlength);
                        }

                        //将当前位置置为0，然后设置limit为容量，也就是从0到limit（容量）这块，
                        //都可以利用，通道读取的数据存储到
                        //0到limit这块
                        bf.clear();

                    }
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
            outchannel.close();
            fos.flush();
            fos.close();
        }catch (Exception e) {
            log.error("combineFiles error:"+e);
            e.printStackTrace();
        }finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        byte[] b = md.digest() ;
        log.info("==md5:"+ MD5Util.bytesToHex(b)); //二进制转16进制
        return  b ;
    }

//    public static byte[] combineFiles(ArrayList<String> fileArray,String combinefile){
//        MessageDigest md =null ;
//        File combfile = new File(combinefile) ;
//        if(!combfile.exists()){
//            createFile(combinefile,"rwxr-x---") ;
//        }
//
//        try{
//            md = MessageDigest.getInstance("MD5");
//
//            FileChannel mFileChannel = new FileOutputStream(combfile).getChannel();
//            FileChannel inFileChannel;
//
//            for(String infile : fileArray){
//                File fin = new File(infile) ;
//                inFileChannel = new FileInputStream(fin).getChannel();
//                inFileChannel.transferTo(0, inFileChannel.size(),
//                        mFileChannel);
//
//                inFileChannel.close();
//
//            }
//            mFileChannel.close();
//        }catch (IOException  e) {
//            log.error("combineFiles error:"+e);
//            e.printStackTrace();
//        }catch(NoSuchAlgorithmException e){
//            log.error("combineFiles md5 error:"+e);
//            e.printStackTrace();
//        }
//
//        byte[] b = md.digest() ;
//        log.info("==md5:"+ MD5Util.bytesToHex(b)); //二进制转16进制
//        return  b ;
//    }



    /**
     * 删除list中绝对路径的file文件
      * @param fileArray
     */
    public static void deleteFiles(ArrayList<String> fileArray){
        for(String infile : fileArray){
            File f = new File(infile) ;
            if(f.exists()){
                f.delete() ;
            }
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteFilesByDirectory(String directory) {
        File dir = new File(directory) ;
        if (dir.isDirectory()) {
            String[] children = dir.list();
//            递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }else{
            log.info("not a dir");
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
    /**
     * 创建文件目录
     * @param path
     * @param perm 文件目录权限
     */
    public static void createDirectory(String path,String perm){
        // 创建新目录
//        Path newdir = FileSystems.getDefault().getPath(path);
//        Set<PosixFilePermission> perms = PosixFilePermissions
//                .fromString(perm); //"rwxr-x---"
//        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions
//                .asFileAttribute(perms);
//        try {
//            Files.createDirectory(newdir, attr);
//        } catch (IOException e) {
//            System.err.println(e);
//        }

        Path target = Paths.get(path);
        try {
            Files.createDirectory(target);
        } catch (IOException e) {
            log.error("createDirectory error:"+e);
        }


    }

    /**
     * 创建多级文件目录
     * @param path
     * @param perm 文件目录权限
     */
    public static void createDirectories(String path,String perm){
        // 创建新目录
//        Path newdir = FileSystems.getDefault().getPath(path);
//        Set<PosixFilePermission> perms = PosixFilePermissions
//                .fromString(perm); //"rwxr-x---"
//        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions
//                .asFileAttribute(perms);
//        try {
//            Files.createDirectory(newdir, attr);
//        } catch (IOException e) {
//            System.err.println(e);
//        }

        Path target = Paths.get(path);
        try {
            Files.createDirectories(target);
        } catch (IOException e) {
            log.error("createDirectories error:"+e);
        }


    }
    /**
     * 创建文件
     * @param filename
     * @param perm 文件权限
     */
    public static void createFile(String filename ,String perm){
//        Path newfile = FileSystems.getDefault().getPath(filename);
//        Set<PosixFilePermission> perms1 = PosixFilePermissions
//                .fromString(perm);
//        FileAttribute<Set<PosixFilePermission>> attr2 = PosixFilePermissions
//                .asFileAttribute(perms1);
//        try {
//            Files.createFile(newfile, attr2);
//        } catch (IOException e) {
//            System.err.println(e);
//        }
        Path target = Paths.get(filename) ;
        try {
            Files.createFile(target);
        } catch (IOException e) {
            log.error("createFile error:"+e);
        }
    }


}
