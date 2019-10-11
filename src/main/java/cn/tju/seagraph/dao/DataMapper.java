package cn.tju.seagraph.dao;

import cn.tju.seagraph.daomain.data;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DataMapper {
    List<data> getDataById(@Param("id") int id);
    List<data> getDataList();
    int insertData(@Param("data") data data);
    int deleteById(@Param("id") int id);
    int updateData(@Param("data") data data);

}
