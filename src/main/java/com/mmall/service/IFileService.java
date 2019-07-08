package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author sunlele
 * @className IFileService
 * @date 2019/7/7 18:39
 **/
public interface IFileService {

    String upload(MultipartFile file, String path);
}
