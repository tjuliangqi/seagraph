package cn.tju.seagraph.dao;

import cn.tju.seagraph.daomain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {
    User getUserById(@Param("id") int id);
    int insertUser(@Param("user") User user);
    int deleteUserById(@Param("id") int id);
    int updateUser(@Param("user") User user);
}
