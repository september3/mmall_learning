package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TokenCache {

    //声明日志
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    public static final String  TOKEN_PREFIX = "token_";


    //声明一个静态的内存块 ，调用链模式不考虑顺序
    private  static LoadingCache<String,String> localCache = CacheBuilder.newBuilder()
            .initialCapacity(1000)  //设置缓存的初始化容量
            .maximumSize(10000 ) //缓存的最大容量，当超过这个容量的时候，Guava的cache会使用LRU算法来移除缓存项
            .expireAfterAccess(12,TimeUnit.HOURS) //缓存的有效期是12个小时
            .build(new CacheLoader<String, String>() {
                //默认的的数据加载实现，当调用get取值时，如果key没有对应的值，就调用这个方法进行加载
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    public static void setKey(String key,String value){
        localCache.put(key,value);
    }
    public static String getKey(String key){
        String value = null;
        try {
            value = localCache.get(key);
            if("null".equals(value)){
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            logger.error("localCache get error",e);
        }
        return null;

    }
}
