package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from sky_take_out.dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /*插入菜品数据*/
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    /*菜品分页查询
    * */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    @Select("select * from sky_take_out.dish where id = #{id}")
    Dish getById(Long id);


    /**
     * 根据主键删除菜品数据
     * @param id
     */
    @Delete("delete from sky_take_out.dish where id = #{id}")
    void delete(Long id);


    /**
     * 根据id删除菜品口味数据
     * @param dishId
     */
    @Delete("delete from sky_take_out.dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);


    /**
     * 根据菜品id集合批量删除菜品
     * @param dishids
     */
    void deleteByIds(List<Long> dishids);


    /**
     * 根据菜品id查询口味数据
     * @param dishId
     * @return
     */
    @Select("select * from sky_take_out.dish_flavor where  dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);

    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);


    /**
     * 动态条件查询菜品
     * @param dish
     * @return
     */
    List<Dish> list(Dish dish);

    /**
     * 根据套餐id查询菜品
     * @param setmealId
     * @return
     */
    @Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{setmealId}")
    List<Dish> getBySetmealId(Long setmealId);

    /**
     * 根据动态条件统计菜品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
