package com.sky.constant;

/**
 * Redis 相关的常量
 */
public class RedisConstant {

    /**
     * 店铺营业状态在 Redis 中的 key
     * 1: 营业中   0: 打烊中
     */
    public static final String SHOP_STATUS = "SHOP_STATUS";

    /**
     * 营业中
     */
    public static final Integer SHOP_OPEN = 1;

    /**
     * 打烊中
     */
    public static final Integer SHOP_CLOSED = 0;
}