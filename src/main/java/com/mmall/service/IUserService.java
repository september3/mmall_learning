package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import org.springframework.stereotype.Service;

/**
 * Created by sunlele
 * Date 2019/1/17 1:48
 * Description
 */

public interface IUserService {

    ServerResponse<User> login(String username, String password);
}
