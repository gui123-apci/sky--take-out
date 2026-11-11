package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /*批量插入口味 数据*/
    void insertBatch(List<DishFlavor> flavors);


    /**
     * 根据菜品id批量删除菜品口味
     * @param dishids
     */
    void deleteByDishIds(List<Long> dishids);

    /**
     * 根据菜品id查询口味数据
     * @param dishId
     * @return
     */
    @Select("select * from sky_take_out.dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);
}
