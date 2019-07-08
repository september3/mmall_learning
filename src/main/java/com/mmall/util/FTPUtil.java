package com.mmall.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author sunlele
 * @className FTPUtil
 * @date 2019/7/7 21:33
 **/
@Data
@Slf4j
public class FTPUtil {

    private static final String ftpIp = PropertiesUtil.getProperty("127.0.0.1");
    private static final String ftpUser = PropertiesUtil.getProperty("sunlele");
    private static final String ftpPass = PropertiesUtil.getProperty("sunlele");

    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public FTPUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    /**
     * 批量上传文件---对外暴露
     * @param fileList
     * @return
     */
    public static boolean uploadFile(List<File> fileList) throws IOException {
       FTPUtil ftpUtil = new FTPUtil(ftpIp,21, ftpUser,ftpPass);
       log.info("开始连接ftp服务器");
       boolean result = ftpUtil.uploadFile("img",fileList);
       log.info("开始连接ftp服务器，结束上传，上传结果：{}");
       return result;
    }

    private boolean uploadFile(String remotePath,List<File> fileList) throws IOException {
        boolean uploaded = true;
        FileInputStream fileInputStream = null;
        //连接FTP服务器
        if(connectServer(this.ip,this.port,this.user,this.pwd)){
            try {
                //改变文件工作目录
                ftpClient.changeWorkingDirectory(remotePath);
                //设置缓冲区大小
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("utf-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                for (File fileItem : fileList){
                    fileInputStream = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(),fileInputStream);
                }

            } catch (IOException e) {
                log.error("上传文件异常",e);
                uploaded = false;
            } finally {
                fileInputStream.close();
                ftpClient.disconnect();
            }
        }
        return uploaded;
    }

    /**
     * 连接ftp服务器
     * @param ip
     * @param port
     * @param user
     * @param pwd
     * @return
     */
    private boolean connectServer(String ip,int port,String user,String pwd){
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user,pwd);
        } catch (IOException e) {
            log.error("连接FTP服务器异常",e);
        }
        return  isSuccess;
    }

}
