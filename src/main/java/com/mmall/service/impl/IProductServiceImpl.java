package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sunlele
 * @className IProuductServiceImpl
 * @date 2019/6/23 14:18
 **/
@Service("iProductService")
public class IProductServiceImpl implements IProductService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse saveOrUpdateProduct(Product product){
        if(product != null){
            if(StringUtils.isNotBlank(product.getSubImages())){
                String[] subImageArray = product.getSubImages().split(",");
                if(subImageArray.length > 0){
                    product.setMainImage(subImageArray[0]);
                }
            }
            if(product.getId() != null){
                int rowCount = productMapper.updateByPrimaryKey(product);
                if(rowCount > 0){
                    return ServerResponse.createBySuccessMessage("更新产品成功");
                }
                return ServerResponse.createBySuccessMessage("更新产品失败");
            }else {
                int rowCount = productMapper.insert(product);
                if(rowCount > 0){
                    return ServerResponse.createBySuccessMessage("新增产品成功");
                }
                return ServerResponse.createBySuccessMessage("新增产品失败");
            }
        }
        return ServerResponse.createByErrorMessage("新增或者更新产品参数不正确");
    }

    @Override
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status){
        if(productId == null || status == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKey(product);
        if(rowCount > 0){
            return ServerResponse.createBySuccessMessage("修改产品销售状态成功");
        }
        return ServerResponse.createBySuccessMessage("修改产品销售状态失败");
    }

    @Override
    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId){
        if(productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ServerResponse.createBySuccessMessage("产品已下架或者删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);


    }

    private ProductDetailVo assembleProductDetailVo(Product product){
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category == null){
            //默认根节点
            productDetailVo.setParentCategoryId(0);
        }else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));

        return productDetailVo;
    }


    /**
     *
     * 利用PageHelper进行分页时，在Sql语句后面无需分号，以防止PageHelpper加载limit pageNum offset pageSize 出现错误
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ServerResponse<PageInfo> getProductList(int pageNum,int pageSize){
        //startPage---start
        //填充Sql查询逻辑
        //pageHelper收尾---end
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.selectList();

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productItem :productList) {
            ProductListVo productListVo = assembleProductListVo(productItem);
            //仅展示需要的信息
            productListVoList.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(productList);
        //重置分页结果
        pageResult.setList(productListVoList);
        return  ServerResponse.createBySuccess(pageResult);
    }


    /**
     * 组装ProductListVo对象
     * @param product
     * @return
     */
    private ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setName(product.getName());
        productListVo.setStatus(product.getStatus());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setPrice(product.getPrice());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        return productListVo;
    }

    /**
     * 商品搜索
     * @param productName productName
     * @param productId productId
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return
     */
    @Override
    public ServerResponse<PageInfo> searchProduct(String productName,
                                                  Integer productId,
                                                  int pageNum,int pageSize){
            PageHelper.startPage(pageNum, pageSize);
            if(StringUtils.isNotBlank(productName)){
                productName = new StrBuilder().append("%").append(productName).append("%").toString();
            }
            List<Product> productList = productMapper.selectByNameAndProductId(productName, productId);

            List<ProductListVo> productListVoList = Lists.newArrayList();
            for (Product productItem : productList){
                ProductListVo productListVo = assembleProductListVo(productItem);
                productListVoList.add(productListVo);
             }
            PageInfo pageRsesult = new PageInfo(productList);
            pageRsesult.setList(productListVoList);
            return ServerResponse.createBySuccess(pageRsesult);

    }

}
