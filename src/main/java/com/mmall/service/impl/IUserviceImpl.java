package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
       //登录需要的密码是经过md5加密的
       String md5Password = MD5Util.MD5EncodeUtf8(password);
       User user = userMapper.selectLogin(username, md5Password);
       if(user == null){
           return ServerResponse.createByErrorMessage("密码错误");
        }
       //密码置空
        user.setPassword(StringUtils.EMPTY);
       return ServerResponse.createBySuccess("登录成功",user);
    }


    /**
     * 注册
     * @param user
     * @return
     */
    public ServerResponse<String> register(User user) {
//        int resultCount = userMapper.checkUsername(user.getUsername());
//        if (resultCount > 0) {
//            return ServerResponse.createByErrorMessage("用户名已存在");
//        }
//        resultCount = userMapper.checkEmail(user.getEmail());
//        if (resultCount > 0) {
//            return ServerResponse.createByErrorMessage("email已存在");
//        }
        ServerResponse validResponse = this.checkVaild(user.getUsername(),Const.USERNAME);
        if (!validResponse.isSuccess()){
            return validResponse;
        }
        validResponse = this.checkVaild(user.getEmail(),Const.EMAIL);
        if (!validResponse.isSuccess()){
            return validResponse;
        }
        //注册为普通用户
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //通过MD5算法对密码进行加密处理
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    /**
     * 安全校验
     * @param str
     * @param type
     * @return
     */
    public ServerResponse<String> checkVaild(String str,String type){
        if(StringUtils.isNotBlank(type)){
            if(Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUsername(str);
                if(resultCount > 0){
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)){
                int resultCount = userMapper.checkEmail(str);
                if(resultCount > 0){
                    return ServerResponse.createByErrorMessage("email已存在");
                }
            }

        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    /**
     * 忘记密码
     * @param username
     * @return
     */
    public ServerResponse<String> selectQuestion(String username){
        ServerResponse validResponse = this.checkVaild(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            return ServerResponse.createBySuccessMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }

    /**
     * 忘记密码---验证答案
     * @param username
     * @param question
     * @param answer
     * @return
     */
    public ServerResponse<String> checkAnswer(String username,String question,String answer){
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if(resultCount > 0){
            //说明问题及问题的答案是这个用户的，并且是正确的
            //利用Guava来做本地缓存的token
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX,forgetToken);
            return ServerResponse.createBySuccessMessage(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }

    /**
     * 忘记密码——重置密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        if(StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("参数错误，token需要传递");
        }
        ServerResponse validResponse = this.checkVaild(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if(StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token无效或者过期");
        }

        if(StringUtils.equals(forgetToken,token)){
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username,md5Password);
            if(rowCount > 0){
                return ServerResponse.createBySuccessMessage("更新密码成功");
            }
        } else {
            return ServerResponse.createBySuccessMessage("token错误，请重新获取重置密码的token");
        }
        return ServerResponse.createBySuccessMessage("修改密码失败");
    }
}
