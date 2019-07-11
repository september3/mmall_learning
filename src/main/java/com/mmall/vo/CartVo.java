package com.mmall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author sunlele
 * @className CartVo
 * @date 2019/7/9 21:49
 **/
@Data
public class CartVo {

    private List<CartProductVo> cartProductVoList;
    private BigDecimal cartTotalPrice;
    private Boolean allChecked;//是否已经都勾选
    private String imageHost;
}
