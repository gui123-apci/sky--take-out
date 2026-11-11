package com.sky.controller.user;

import com.sky.constant.RedisConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Api("店铺相关接口")
@Slf4j
public class ShopController {


    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${sky.shop.name:苍穹外卖}")
    private String shopName;

    @Value("${sky.shop.address:北京市海淀区中关村软件园}")
    private String shopAddress;

    @Value("${sky.shop.phone:18500557668}")
    private String shopPhone;

    /**
     * 获取商家信息（小程序首页展示及联系商家）
     * @return
     */
    @GetMapping("/getMerchantInfo")
    @ApiOperation("获取商家信息")
    public Result<Map<String, Object>> getMerchantInfo(){
        Map<String, Object> merchantInfo = new HashMap<>();
        merchantInfo.put("shopName", shopName);
        merchantInfo.put("shopAddress", shopAddress);
        merchantInfo.put("phone", shopPhone);
        log.info("获取商家信息：{}", merchantInfo);
        return Result.success(merchantInfo);
    }

    /**
     * 获取店铺营业状态
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺的营业状态")
    public Result<Integer> getStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(RedisConstant.SHOP_STATUS);
        if (status == null) {
            status = RedisConstant.SHOP_CLOSED;
        }
        log.info("获取店铺营业状态为：{}", RedisConstant.SHOP_OPEN.equals(status) ? "营业中" : "打烊中");
        return Result.success(status);
    }
    }

