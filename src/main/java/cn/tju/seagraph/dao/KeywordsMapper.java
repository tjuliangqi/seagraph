package cn.tju.seagraph.dao;

import cn.tju.seagraph.daomain.keywords;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface KeywordsMapper {
    @Select("SELECT * FROM `hot` WHERE `id` = ${id}")
    keywords getKeywordsById(int id);
    @Select("SELECT * FROM `wy`.`hot`")
    List<keywords> getKeywordsList();
    @Insert({"INSERT INTO `hot` (`keywords`) VALUES (#{keywords.words}"})
    int insertKeywords(keywords keywords);
    @Delete("DELETE FROM hot WHERE id = ${id}")
    int deleteById(int id);
    @Update("UPDATE `wy`.`hot` SET `keywords` = #{keywords.words} WHERE `id` = ${keywords.id}")
    int updateData(keywords keywords);
}
