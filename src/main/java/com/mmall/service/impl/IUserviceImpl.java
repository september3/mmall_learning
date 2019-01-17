package com.mmall.service.impl;

import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sunlele
 * Date 2019/1/17 2:02
 * Description
 */

@Service("iUserService")
public class IUserviceImpl implements IUserService {


    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录接口
     * @param username
     * @param password
     * @return
     */
    @Override
    public ServerResponse<User> login(String username, String password) {
        //数据库交互之后查到的结果赋给resultMap
       int resultCount = userMapper.checkUsername(username);
       if (resultCount == 0){
           return ServerResponse.createByErrorMessage("用户名不存在");
       }
       User user = userMapper.selectLogin(username, password);
       if(user == null){
           return ServerResponse.createByErrorMessage("密码错误");
        }
       //密码置空
        user.setPassword(StringUtils.EMPTY);
       return ServerResponse.createBySuccess("登录成功",user);
    }
}
