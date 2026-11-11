package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    /*
    * 新增菜品和对应口味
    * */
    public void saveWithFlavor(DishDTO dishDTO);


    /*菜品分页查询
    * */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    void deleteBatch(List<Long> ids);



/**
 * 根据id查询菜品及其口味
 * @param id
 * @return
 */
    DishVO getByIdWithFlavor(long id);

    void updateWithFlavor(DishDTO dishDTO);


    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    List<Dish> list(Long categoryId);


    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);


    void cleanCache(String pattern);

    /**
     * 菜品起售停售
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

}
