package com.mmall.util;

import java.math.BigDecimal;

/**
 * @author sunlele
 * @className BigDecimalUtil
 * @date 2019/7/9 22:06
 **/
public class BigDecimalUtil {

    private BigDecimalUtil(){
        //防止外部实例化
    }

    public static BigDecimal add(double v1 ,double v2){
        //String构造器
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }

    public static BigDecimal sub(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }

    public static BigDecimal mul(double v1 ,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }

    public static BigDecimal div(double v1 ,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        //四舍五入，保留两位小数
        return b1.divide(b2,2,BigDecimal.ROUND_HALF_UP);
    }
}
