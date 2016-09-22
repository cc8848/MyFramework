package com.sys.entity;

/**
 * 文件信息
 * @author zzl
 * Date:2014-09-15
 */
public class FileBean {
    private String id;
    private String fileName;
    private String filePath;
    private String md5;
    private Double  fileSize;//单位为MB
    private Integer pro;
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public FileBean() {
    }

    public FileBean(String fileName, String filePath, String md5) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.md5 = md5;
    }

    public FileBean(String id, String fileName, String filePath, String md5, Double fileSize, Integer pro) {
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
        this.md5 = md5;
        this.fileSize = fileSize;
        this.pro = pro;
    }

    public Double getFileSize() {
        return fileSize;
    }

    public void setFileSize(Double fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getPro() {
        return pro;
    }

    public void setPro(Integer pro) {
        this.pro = pro;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
