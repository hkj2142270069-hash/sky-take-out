package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    void insertBatch(@Param( "dishFlavors") List<DishFlavor> dishFlavors);
    /**
     * 根据菜品id查询对应的口味数据
     * @param dishId
     * @return
     */
@Delete( "delete from dish_flavor where dish_id = #{id}")
    void deleteByDishId(Long id);

    void deleteByDishIds(List<Long> ids);
}
