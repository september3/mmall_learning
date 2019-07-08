package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author sunlele
 * @className IFileServiceImpl
 * @date 2019/7/7 18:39
 **/
@Slf4j
@Service("iFileService")
public class IFileServiceImpl implements IFileService {

    /**
     * 文件上传
     * @param file
     * @param path
     * @return
     */
    @Override
    public String upload(MultipartFile  file,String path){
        String filename = file.getOriginalFilename();
        //扩展名  abc.jpg
        String fileExtensionName = filename.substring(filename.lastIndexOf(",") + 1);
        //防止文件重复
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        log.info("开始上传文件，上传的文件名:{}","上传的路径:{}","新文件名:{}",filename,path,uploadFileName);

        //创建文件目录
        File fileDir = new File(path);
        if(!fileDir.exists()){
            //对文件设置写权限
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }

        File targetFile = new File(path,uploadFileName);
        try {
            file.transferTo(targetFile);
            //文件已经上传完成
            // 将targetFile上传到我们的FTP服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //已经上传到ftp服务器上
            //上传完之后，删除upload下面的文件
            targetFile.delete();
        } catch (IOException e) {
            log.error("上传文件异常",e);
            return null;
        }
        return targetFile.getName();
    }
}
