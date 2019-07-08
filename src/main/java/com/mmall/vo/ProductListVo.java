package com.mmall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author sunlele
 * @className ProductListVo
 * @date 2019/7/7 17:28
 **/
@Data
public class ProductListVo {

    private Integer id;
    private Integer categoryId;

    private String name;
    private String subtitle;
    private String mainImage;
    private BigDecimal price;

    private Integer status;

    private String imageHost;

}
