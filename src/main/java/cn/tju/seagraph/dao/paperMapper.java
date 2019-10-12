package cn.tju.seagraph.dao;

import cn.tju.seagraph.daomain.paperMysqlBean;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface paperMapper {

    @Select("SELECT * FROM `science1` WHERE `uuid` = #{uuid}")
    paperMysqlBean getDataById(String uuid);
    @Select("SELECT * FROM `science1`")
    List<paperMysqlBean> getDataList();
    @Insert({"INSERT INTO `wy`.`science1`(`uuid`, `authors`, `affiliations`, " +
            "`doi`, `title`, `journal`, `abs`, `fulltext`, `references`, `pubdate`," +
            " `pdf_url`, `type`, `pic_url`, `pic_text`, `keywords`, `fulltext_url`, `download`, " +
            "`ch_title`, `re_uuid`, `or_title`, `browse`, `chemicallist`, `labels`) VALUES (#{uuid},#{authors}," +
            "#{affiliations},#{doi},#{title},#{journal},#{abs},#{fulltext},#{references},#{pubdate},#{pdf_url},#{type}," +
            "#{pic_url},#{pic_text},#{keywords},#{fulltext_url},#{download},#{ch_title},#{re_uuid},#{or_title},#{browse},#{chemicallist}," +
            "#{labels})"})
    int insertData(paperMysqlBean paperMysqlBean);
    @Delete("DELETE FROM science1 WHERE uuid = #{uuid}")
    int deleteById(String uuid);
    @Update("UPDATE `wy`.`science1` SET `authors` = #{authors}, `affiliations` = #{affiliations}," +
            " `doi` = #{doi}, `title` = #{title}, `journal` = #{journal}, `abs` = #{abs}, `fulltext` = #{fulltext}," +
            " `references` = #{references}, `pubdate` = #{pubdate}, `pdf_url` = #{pdf_url}, `type` = #{type},`pic_url` = #{pic_url}," +
            " `pic_text` = #{pic_text}, `keywords` = #{keywords}, `fulltext_url` = #{fulltext_url}, `download` = #{download}, `ch_title` = #{ch_title}," +
            " `re_uuid` = #{re_uuid}, `or_title` = #{or_title}, `browse` = #{browse}, `chemicallist` = #{chemicallist}, `labels` = #{labels} WHERE `uuid` = #{uuid}")
    int updateData(paperMysqlBean paperMysqlBean);
}
