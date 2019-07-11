package com.mmall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author sunlele
 * @className CartProductVo
 * @date 2019/7/9 21:39
 **/
@Data
public class CartProductVo {

    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer quantity;//购物车中此商品的数量
    private String productName;
    private String productSubtitle;
    private String productMainImage;
    private BigDecimal productPrice;
    private Integer productStatus;
    private BigDecimal productTotalPrice;
    private Integer productStock;
    private Integer productChecked;//此商品是否勾选

    private String limitQuantity;//限制数量的一个返回结果
}
