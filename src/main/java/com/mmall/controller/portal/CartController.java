package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.vo.CartVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author sunlele
 * @className CartController
 * @date 2019/7/9 21:17
 **/
@Controller
@RequestMapping("/cart/")
public class CartController {

    @Resource
    private ICartService iCartService;

    /**
     * 购物车列表
     * @param session session
     * @return ServerResponse<CartVo>
     */
    @ResponseBody
    @RequestMapping("list.do")
    public ServerResponse<CartVo> list(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.list(user.getId());
    }

    /**
     * 购物车添加
     * @param session session
     * @param count count
     * @param productId productId
     * @return ServerResponse<CartVo>
     */
    @ResponseBody
    @RequestMapping("add.do")
    public ServerResponse<CartVo> add(HttpSession session, Integer count, Integer productId){
        if(productId == null || count == null){
            return ServerResponse.createByErrorMessage("参数错误");
        }
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.add(user.getId(),productId,count);
    }

    /**
     * 购物车更新
     * @param session session
     * @param count 数量
     * @param productId productId
     * @return ServerResponse<CartVo>
     */
    @ResponseBody
    @RequestMapping("update.do")
    public ServerResponse<CartVo> update(HttpSession session, Integer count, Integer productId){
        if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.update(user.getId(),productId,count);
    }

    /**
     * 购物车删除
     * @param session session
     * @param productIds productIds
     * @return ServerResponse<CartVo>
     */
    @ResponseBody
    @RequestMapping("delete_product.do")
    public ServerResponse<CartVo> delProduct(HttpSession session,String productIds){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.del(user.getId(),productIds);
    }


    /**
     * 购物车全选
     * @param session session
     * @return ServerResponse<CartVo>
     */
    @ResponseBody
    @RequestMapping("select_all.do")
    public ServerResponse<CartVo> selectAll(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelectAll(user.getId(),Const.Cart.CHECKED);
    }

    /**
     * 全反选
     * @param session session
     * @return ServerResponse<CartVo>
     */
    @ResponseBody
    @RequestMapping("un_select_all.do")
    public ServerResponse<CartVo> unSelectAll(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelectAll(user.getId(),Const.Cart.UNCHECKED);
    }

}
