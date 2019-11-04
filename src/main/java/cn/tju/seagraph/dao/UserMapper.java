package cn.tju.seagraph.dao;

import cn.tju.seagraph.daomain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMapper {
    List<User> getUserByEmail(@Param("email") String email);
    List<User> getUserByUsername(@Param("username") String username);
    List<User> getUserByToken(@Param("token") String token);
    int insertUser(@Param("user") User user);
    int deleteUserById(@Param("id") int id);
    int updateUser(@Param("user") User user);
}
