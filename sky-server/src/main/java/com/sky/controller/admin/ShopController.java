package com.sky.controller.admin;

import com.sky.constant.RedisConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api("管理端店铺相关接口")
@Slf4j
public class ShopController {



    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 设置店铺营业状态
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("设置店铺营业状态")
    public Result setStatus(@PathVariable Integer status){
        if (!RedisConstant.SHOP_OPEN.equals(status) && !RedisConstant.SHOP_CLOSED.equals(status)) {
            return Result.error("营业状态只能是 0 或 1");
        }
        log.info("设置店铺营业状态为：{}", RedisConstant.SHOP_OPEN.equals(status) ? "营业中" : "打烊中");
        redisTemplate.opsForValue().set(RedisConstant.SHOP_STATUS, status);
        return Result.success();
    }


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
