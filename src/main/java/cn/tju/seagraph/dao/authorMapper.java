package cn.tju.seagraph.dao;

import cn.tju.seagraph.daomain.Author;
import cn.tju.seagraph.daomain.data;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface authorMapper {
    List<Author> getAuthorById(@Param("id") String id);
    List<Author> getAuthorList();
    int insertAuthor(@Param("author") Author author);
    int deleteById(@Param("id") String id);
    int updateAuthor(@Param("author") Author author);

}