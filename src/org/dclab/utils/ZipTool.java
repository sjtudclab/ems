package org.dclab.utils;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.*;

/**
 * Zip file & unzip
 * Created by zhaoz on 2016/8/18.
 */
public class ZipTool {
    static final int BUFFER = 8192;
    
    /**
     * Compress the src folder to dst ZIP file
     * @param src
     * @param dst
     */
    public static void compress(String src, String dst) {
        File file = new File(src);
        File zip  = new File(dst);
        if (!file.exists()){  
            throw new RuntimeException(src + "不存在！");
        }  
        try {    
            FileOutputStream fileOutputStream = new FileOutputStream(zip);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,new CRC32());    
            ZipOutputStream out = new ZipOutputStream(cos);
            String basedir = "";    
            compressByType(file, out, basedir);    
            out.close();    
        } catch (Exception e) {   
            e.printStackTrace();  
            throw new RuntimeException(e);
        }    
    }    
    
    /** 
     * 判断是目录还是文件，根据类型（文件/文件夹）执行不同的压缩方法 
     * @param file  
     * @param out 
     * @param basedir 
     */  
    private static void compressByType(File file, ZipOutputStream out, String basedir) {
        /* 判断是目录还是文件 */
        System.out.println("压缩：" + basedir + file.getName());
        if (file.isDirectory()) {    
            compressDirectory(file, out, basedir);
        } else {
            compressFile(file, out, basedir);
        }    
    }    
    
    /** 
     * 压缩一个目录 
     * @param dir 
     * @param out 
     * @param basedir 
     */  
    private static void compressDirectory(File dir, ZipOutputStream out, String basedir) {
        if (!dir.exists()){  
             return;    
        }  
             
        File[] files = dir.listFiles();    
        for (int i = 0; i < files.length; i++) {    
            /* 递归 */    
            compressByType(files[i], out, basedir + dir.getName() + "/"); //from this directory '/'
        }    
    }    
    
    /** 
     * 压缩一个文件 
     * @param file 
     * @param out 
     * @param basedir 
     */  
    private static void compressFile(File file, ZipOutputStream out, String basedir) {
        if (!file.exists()) {    
            return;    
        }    
        try {    
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));    
            ZipEntry entry = new ZipEntry(basedir + file.getName());
            out.putNextEntry(entry);    
            int count;    
            byte data[] = new byte[BUFFER];    
            while ((count = bis.read(data, 0, BUFFER)) != -1) {    
                out.write(data, 0, count);    
            }    
            bis.close();    
        } catch (Exception e) {    
            throw new RuntimeException(e);    
        }    
    }

    public static void  unzip(String srcZip, String dstName){
        File file = new File(srcZip);
        // folderName   =   file.getName().substring(0, file.getName().lastIndexOf("."));
        if (!file.exists()){
            throw new RuntimeException(srcZip + "不存在！");
        }

        try {

            String basedir = dstName;//+ File.separator+folderName;
            ZipFile zipFile =   new ZipFile(srcZip);
            for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements();){
                ZipEntry    entry   =   e.nextElement();

                System.out.println("Unzip "+entry.getName());
                if (entry.isDirectory()){
                    File dir    =   new File(basedir, entry.getName());
                    if(dir.mkdirs())
                        System.out.println("\tcreating dir "+entry.getName());

                }else {
                    File outFile    =   new File(basedir, entry.getName());
                    File    parent  =   outFile.getParentFile();
                    if(!parent.exists())
                        parent.mkdirs();

                    BufferedInputStream  zis =   new BufferedInputStream(zipFile.getInputStream(entry));
                    FileOutputStream fos = new FileOutputStream(outFile);
                    byte[] data = new byte[BUFFER];
                    int cnt = 0;        //ZipInputStream doesn't work, use BufferedInputStream
                    while ((cnt = zis.read(data)) != -1) {
                        fos.write(data, 0, cnt);
                        //System.out.println("Writing bytes: "+cnt+"B to "+entry.getName());
                    }
                    fos.close();
                }
            }
            zipFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String args[]){
    /*    ZipTool.compress("D:\\测试", "D:\\测试压缩.zip");*/
        ZipTool.unzip("D:\\测试压缩.zip","D:\\test");
    }
}  