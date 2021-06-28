package com.johns.dynamicdatasource.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.johns.dynamicdatasource.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 用户映射器
 *
 * @author johns-li
 * @date 2021/06/23
 */
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user")
    List<User> selectAll();
}
