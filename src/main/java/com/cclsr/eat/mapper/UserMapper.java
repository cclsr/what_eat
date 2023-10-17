package com.cclsr.eat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cclsr.eat.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
