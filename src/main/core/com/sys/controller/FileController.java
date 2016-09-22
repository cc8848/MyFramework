package com.sys.controller;


import com.sys.constant.Globals;
import com.sys.entity.FileBean;
import com.sys.service.ISystemService;
import com.sys.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 文件上传
 * @author zzl
 * Date:2014-09-11
 */
@Controller
@RequestMapping("/file")
public class FileController {
    @Resource
    private ISystemService systemService;


    private static String swfPath="";

    /**
     * 文件上传
     * @param request
     * @param file
     * @return
     */
    @RequestMapping(params = "upload")
    @ResponseBody
    public SuccessMsg uploadFile(HttpServletRequest request,@RequestParam("file")MultipartFile file){
        String dirPath=request.getParameter("dirPath");
        String fileName=request.getParameter("fileName");
        if(StringUtils.isBlank(dirPath))
            dirPath=FileUtil.getAbsoluteFilePath("upload");
        else
            dirPath=FileUtil.getAbsoluteFilePath(dirPath);
        File dir=new File(dirPath);
        if(!dir.exists())
            dir.mkdirs();
        if(StringUtils.isBlank(fileName))
            fileName=file.getOriginalFilename();
        else{
            if(fileName.lastIndexOf(".")!=-1)
                fileName=fileName.substring(0,fileName.indexOf("."));
            if(file.getOriginalFilename().lastIndexOf(".")!=-1)
                fileName+=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        }
        SuccessMsg json=new SuccessMsg(true,fileName+"上传成功！");
        try {
            json.setSuccess(FileUtil.uploadFile(file.getInputStream(),new File(dirPath,fileName)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!json.isSuccess())
            json.setMsg(fileName+"上传失败！");
        systemService.addLog(json.getMsg(), Globals.LOG_INSERT);
        return json;
    }

    /**
     * 转入上传页面
     * @return
     */
    @RequestMapping(params = "toupload",method = RequestMethod.GET)
    public String toUpload(HttpServletRequest request){
        String filePath=request.getParameter("filePath"); //文件夹路径
        String showType=request.getParameter("showType"); //字段显示类型 file 或file_view
        String type=request.getParameter("type"); //update为 可编辑
        String actionUrl=request.getParameter("actionUrl"); //业务请求url 作为判断权限依据
        if(StringUtils.isNotBlank(filePath))
            try{
                filePath=new String(filePath.getBytes("iso-8859-1"),"utf-8");
            }catch (Exception e){
               e.printStackTrace();
            }
        request.setAttribute("filePath",filePath==null?"":filePath);
        request.setAttribute("showType",showType==null?"":showType);
        request.setAttribute("type",type==null?"":type);
        request.setAttribute("actionUrl",actionUrl==null?"":actionUrl);
        return "sys/fileview/filelist";
    }
    /**
     * 文件下载
     * @param filePath
     * @return
     */
    @RequestMapping(params = "download",method= RequestMethod.GET)
    @ResponseBody
    public void downLoadFile(HttpServletRequest request,String filePath,String fileName,HttpServletResponse response){
        Assert.notNull(filePath);
        try{
            filePath=new String(filePath.getBytes("iso-8859-1"),"utf-8");
            filePath=FileUtil.getAbsoluteFilePath(filePath);
            File file=new File(filePath);
            if(!file.exists())
                file=new File(request.getSession().getServletContext().getRealPath("/")+File.separator+filePath);
            if(!file.exists())
                return ;
            if(fileName==null)
                fileName=file.getName();
            else
                fileName=new String(fileName.getBytes("iso-8859-1"),"utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }

        FileUtil.downLoadFile(filePath,fileName,response);
    }

    /**
     * 查看文件夹下的文件
     * @param request
     * @param filePath
     * @param response
     * @return
     */
    @RequestMapping(params = "listFiles")
    @ResponseBody
    public List<FileBean> listFiles(HttpServletRequest request,String filePath,HttpServletResponse response){
        List<FileBean> files=new ArrayList<>(0);
        if(StringUtils.isNotBlank(filePath))
        try{
            filePath=StringUtil.getDecodeStr(filePath,request.getMethod());
            filePath=FileUtil.getAbsoluteFilePath(filePath);
            File file=new File(filePath);
            if(file.exists()&&file.isDirectory()){
                File[] files1=file.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        if("txt".equals(FileUtil.getFIleSuffix(pathname.getName()).toLowerCase()))
                            return false;
                        return true;
                    }
                });
                if(files1!=null){
                    for(File file1:files1){
                        if(file1.getName().contains("swf")) {
                            files.add(new FileBean("", file1.getName(), file1.getAbsolutePath().replace("\\", "/"), "", file1.length() * 1.00 / 1024 / 1024, 100));
                        }
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
       return files;
    }
    /**
     * pdf文件浏览
     * @param filePath
     */
    @RequestMapping(params = "view",method= RequestMethod.GET)
    public String  viewFile(String filePath,HttpServletRequest request){
        String webpath=request.getSession().getServletContext().getRealPath("/");
        String tempDir=webpath+File.separator+"tempFile";
        File tempDirFile=new File(tempDir);
        if(!tempDirFile.exists())
            tempDirFile.mkdirs();
        try{
            filePath=new String(filePath.getBytes("iso-8859-1"),"utf-8");
//            filePath=FileUtil.getAbsoluteFilePath(filePath);
        }catch (Exception e){
            e.printStackTrace();
        }
        File file=new File(filePath);
        if(!file.exists()){
            file=new File(webpath+File.separator+filePath);
            if(!file.exists())
                request.setAttribute(Globals.SYSTEM_MSG,"文件不存在");
        }else{
            if(file.isDirectory()){
               File[]  fileNames=file.listFiles();
               if(fileNames!=null){
                   request.setAttribute("fileList",new ArrayList<File>(Arrays.asList(fileNames)));
               }
            }else{
                File tempFile=new File(tempDir,System.currentTimeMillis()+"."+FileUtil.getFIleSuffix(filePath));
                try {
                    FileCopyUtils.copy(file,tempFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                request.setAttribute("filePath","/tempFile/"+tempFile.getName());
            }
        }
        request.setAttribute("_auitCon",ResourceUtil.getParamMap(request));
        return viewPdf(filePath,request);
    }


//    /**
//     * pdf文件浏览
//     * @param filePath
//     */
//    @RequestMapping(params = "viewWj",method= RequestMethod.GET)
//    public String  viewWj(String filePath,HttpServletRequest request){
//        BusinessWjEntity wj = this.businessWjService.getEntity(BusinessWjEntity.class,filePath);
//        String ywlj = wj.getYwlj();
//        String webpath=request.getSession().getServletContext().getRealPath("/");
//        String tempDir=webpath+File.separator+"tempFile";
//        File tempDirFile=new File(tempDir);
//        if(!tempDirFile.exists())
//            tempDirFile.mkdirs();
//        try{
////            filePath=new String(filePath.getBytes("iso-8859-1"),"utf-8");
////            filePath=FileUtil.getAbsoluteFilePath(filePath);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        File file=new File(ywlj);
//        if(!file.exists()){
//            file=new File(webpath+File.separator+ywlj);
//            if(!file.exists())
//                request.setAttribute(Globals.SYSTEM_MSG,"文件不存在");
//        }else{
//            if(file.isDirectory()){
//                File[]  fileNames=file.listFiles();
//                if(fileNames!=null){
//                    request.setAttribute("fileList",new ArrayList<File>(Arrays.asList(fileNames)));
//                }
//            }else{
//                File tempFile=new File(tempDir,System.currentTimeMillis()+"."+FileUtil.getFIleSuffix(ywlj));
//                try {
//                    FileCopyUtils.copy(file,tempFile);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                request.setAttribute("filePath","/tempFile/"+tempFile.getName());
//            }
//        }
//        request.setAttribute("_auitCon",ResourceUtil.getParamMap(request));
//        return viewPdf(ywlj,request);
//    }


    @RequestMapping(params = "outPutFile")
    public void  outPutFile(String filePath,HttpServletRequest request,HttpServletResponse response){
        filePath=StringUtil.getDecodeStr(filePath,request.getMethod());
        File file=new File(filePath);
        Assert.isTrue(file.exists(),"文件不存在！");
        Assert.isTrue(!file.isDirectory(),"文件是目录！");
        InputStream inputStream=null;
        try{
            inputStream=FileUtils.openInputStream(file);
            FileUtil.outputFile(inputStream,response);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    //查看pdf
    private String viewPdf(String filePath,HttpServletRequest request){
        swfPath=filePath;
        return "sys/fileview/view_swf";
    }

    @RequestMapping(params = "showXmlSwf")
    public void showXmlSwf(HttpServletRequest request,HttpServletResponse response){

        String newPath=swfPath;

        File file=new File(newPath);
        if(!file.exists())
            return;
        InputStream inputStream=null;
        try{
            inputStream=FileUtils.openInputStream(file);
            FileUtil.outputFile(inputStream,response);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @RequestMapping(params = "showXmlPic")
    public void showXmlPic(String path,HttpServletRequest request,HttpServletResponse response){

        File file=new File(path);
        if(!file.exists())
            return;
        InputStream inputStream=null;
        try{
            inputStream=FileUtils.openInputStream(file);
            FileUtil.outputFile(inputStream,response);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    /**
     * 删除文件
     * @param filePath
     * @return
     */
    @RequestMapping(params = "deletefile",method= RequestMethod.GET)
    @ResponseBody
    public SuccessMsg deleteFile(String filePath){
        Assert.notNull(filePath);
        try{
            filePath=new String(filePath.getBytes("iso-8859-1"),"utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        filePath=FileUtil.getAbsoluteFilePath(filePath);
        SuccessMsg json=new SuccessMsg(true,"文件删除成功！");
        if(!new File(filePath).delete()){
            json.setSuccess(false);
            json.setMsg("文件删除失败!");
        }
        return json;
    }
}
