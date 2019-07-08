package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 供前台使用
 * @author sunlele
 * @className ProductController
 * @date 2019/7/8 21:47
 **/
@Controller
@RequestMapping("/product/")
public class ProductController {

    @Resource
    private IProductService iProductService;

    /**
     * 前台商品详情
     * @param producrtId producrtId
     * @return ServerResponse
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getProductDetail(Integer producrtId){
        return iProductService.getProductDetail(producrtId);
    }

    /**
     * 用户端的产品搜索
     * @param keyWord keyWord
     * @param categoryId categoryId
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @param orderBy orderBy
     * @return ServerResponse<PageInfo>
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword",required = false) String keyWord,
                                         @RequestParam(value = "categoryId",required = false)Integer categoryId,
                                         @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                                         @RequestParam(value = "orderBy",defaultValue = "")String orderBy){
        return iProductService.getProductByKeywordCatrgory(keyWord, categoryId, pageNum, pageSize, orderBy);
    }


}
