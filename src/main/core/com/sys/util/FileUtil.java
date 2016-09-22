/***********************************************************************
 * Module:  FileUtil.java
 * Author:  lenovo
 * Purpose: Defines the Class FileUtil
 ***********************************************************************/

package com.sys.util;

import com.sys.entity.FileTreeBean;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipOutputStream;


/** 文件工具类
 * 
 * @pdOid d2fcbbf0-f853-4210-a5bd-ba39a90529ce */
public class FileUtil {

   
   /** 获取文件前缀名
    * 
    * @param filePath
    * @pdOid fd31e02f-b2c1-4387-83bb-327d3dac8300 */
   public static String getPrefixPath(String filePath) {
       if(filePath.indexOf(".")!=-1&&filePath.lastIndexOf(".")<filePath.length()-1)
           return filePath.substring(0,filePath.lastIndexOf("."));
       return filePath;
   }
   
   /** 获取简单文件名前缀
    * 
    * @param filePath
    * @pdOid cf56a3f7-e6e6-451d-a611-085375ba9181 */
   public static String getSImplePrefixPath(String filePath) {
        File file=new File(filePath);
        String fileName=file.getName();

      return getPrefixPath(fileName);
   }
    /**
     * 压缩，打包zip
     */
    public static boolean doZip(String filePath, String zipFilePath){
        boolean result = false;
        File source = new File(filePath);
        if (!source.exists() || !source.isDirectory()){
            return result;
        }
        File zipFile = new File(zipFilePath+"/"+source.getName()+".zip");
        if (zipFile.exists()){
            zipFile.delete();
        }else if(!zipFile.getParentFile().exists()) {
            zipFile.getParentFile().mkdirs();
        }

        FileOutputStream dest = null;
        ZipOutputStream out = null;
        try {
            dest = new FileOutputStream(zipFile);
            out = new ZipOutputStream(new BufferedOutputStream(dest));
            out.setMethod(ZipOutputStream.DEFLATED);
            compress(source, out, source.getName());
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally{
            if(out != null){
                try {
                    out.closeEntry();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

    public static void compress(File source, ZipOutputStream out,
                                String name) {
        if(source.isFile()) {
            BufferedInputStream bis = null;
            FileInputStream in = null;
            try {
                in = new FileInputStream(source);
                bis = new BufferedInputStream(in, 1024*10);
                int index = source.getAbsolutePath().indexOf(name);
                String entryName = source.getAbsolutePath().substring(index);
              //  out.putNextEntry(new ZipEntry(entryName));
                byte[] data =new byte[1024*10];
                int count = -1;
                while((count = bis.read(data,0,1024*10)) > 0){
                    out.write(data, 0, count);
                }
                out.closeEntry();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in!=null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else if(source.isDirectory()){
            File[] fs=source.listFiles();
            if(fs !=null && fs.length > 0) {
                for(File f: fs) {
                    compress(f, out, name);
                }
            }
        }
    }
   /** 获取文件后缀
    * 
    * @param filePath
    * @pdOid 076d7096-1df5-49b9-93e4-fcb85f827ce5 */
   public static String getFIleSuffix(String filePath) {
       if(filePath.indexOf(".")!=-1&&filePath.lastIndexOf(".")<filePath.length()-1)
           return filePath.substring(filePath.lastIndexOf(".")+1);
      return "";
   }

    /**
     * 下载文件
     * @param filePath
     * @param fileName
     * @param response
     */
   public static void downLoadFile(String filePath,String fileName,HttpServletResponse response){
       File file=new File(filePath);
       if(!file.exists())
           return ;
       try {
           downLoadFile(FileUtils.openInputStream(file),fileName,file.length(),response);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }

    /**
     * 下载文件
     * @param inputStream
     * @param fileName
     * @param size
     * @param response
     */
   public static void downLoadFile(InputStream inputStream,String fileName,long size,HttpServletResponse response){
        try{
            fileName=new String(fileName.getBytes("utf-8"),"iso-8859-1");
        }catch (Exception e){
            e.printStackTrace();
        }
       response.setContentType("application/octet-stream; charset=utf-8");
       response.setHeader("Content-Disposition", "attachment;filename="+fileName);
       if(size>0)
            response.addHeader("Content-Length",size+"");
       OutputStream stream= null;
       try {
           stream = response.getOutputStream();
           FileCopyUtils.copy(inputStream, stream);
           stream.flush();
       } catch (IOException e) {
           e.printStackTrace();
       }finally {
           if(stream!=null){
               try {
                   stream.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }

       }

   }

    /**
     * 输出文件
     * @param inputStream
     * @param response
     */
    public static void outputFile(InputStream inputStream,HttpServletResponse response){
        OutputStream stream= null;
        try {
            stream = response.getOutputStream();
            FileCopyUtils.copy(inputStream, stream);
            stream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(stream!=null){
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 上传文件
     * @param inputStream
     * @param destFile
     * @return
     */
   public static boolean uploadFile(InputStream inputStream,File destFile){
       OutputStream outputStream=null;
       try {
           outputStream=new FileOutputStream(destFile);
           FileCopyUtils.copy(inputStream,outputStream);
       } catch (Exception e) {
           e.printStackTrace();
          return false;
       }finally {
           if(outputStream!=null){
               try {
                   outputStream.flush();
                   outputStream.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
           try {
               if(inputStream!=null){
                   inputStream.close();
               }
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
       return true;
   }

    /**
     * 获取绝对路径
     * 若输入相对路径则返回项目路径+相对路径
     * @param filePath
     * @return
     */
   public static String getAbsoluteFilePath(String filePath){
        if(StringUtils.isBlank(filePath))
            return "";
        String newfilePath=filePath.replace("\\","/").toLowerCase();
        File [] roots=File.listRoots();
        for(File f:roots){
           if(newfilePath.startsWith(f.getAbsolutePath().toLowerCase().replace("\\","/")))
            return  filePath;
        }
       if(newfilePath.startsWith("/"))
           filePath=filePath.substring(1);

       return FileUtil.class.getClassLoader().getResource("../../").getPath()+filePath;
   }

    /**
     * 列出文件树
     * @param dirPath 文件夹路径
     * @param fileSuffix 需要列出的文件后缀名
     * @param includeFolder 是否包含文件夹
     * @param includeSubFile 是否包含子文件
     * @return
     */
    public static List<FileTreeBean> listFileTree(String dirPath,String fileSuffix,boolean includeFolder,boolean includeSubFile){
        List<FileTreeBean> list=new ArrayList<>();
        File dir=new File(dirPath);
        if(dir.exists()&&dir.isDirectory()){
           File[] files=dir.listFiles();
           if(!ArrayUtils.isEmpty(files)){
               for(File file:files){
                   FileTreeBean treeBean=new FileTreeBean(file.getName(),file.getAbsolutePath().replace("\\","/"));
                   if(file.isDirectory()&&includeSubFile){
                       treeBean.setState("closed");
                       treeBean.setChildren(listFileTree(file.getAbsolutePath(),fileSuffix,includeFolder,includeSubFile));
                       list.add(treeBean);
                   }else{
                       if(StringUtils.isNotBlank(fileSuffix)){
                           if(file.getName().endsWith(fileSuffix))
                                list.add(treeBean);
                       }else{
                           list.add(treeBean);
                       }
                   }

               }
           }
        }
        return list;
    }
}