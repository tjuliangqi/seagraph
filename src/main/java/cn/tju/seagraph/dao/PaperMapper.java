package cn.tju.seagraph.dao;

import cn.tju.seagraph.daomain.PaperMysqlBean;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PaperMapper {

    @Select("SELECT * FROM `science` WHERE `uuid` = #{uuid}")
    PaperMysqlBean getDataById(String uuid);
    @Select("SELECT * FROM `science`")
    List<PaperMysqlBean> getDataList();
    @Insert({"INSERT INTO `wy`.`science`(`uuid`, `authors`, `affiliations`, " +
            "`doi`, `title`, `journal`, `abs`, `fulltext`, `references`, `pubdate`," +
            " `pdf_url`, `type`, `pic_url`, `pic_text`, `keywords`, `fulltext_url`, `download`, " +
            "`ch_title`, `re_uuid`, `or_title`, `browse`, `chemicallist`, `labels`) VALUES (#{uuid},#{authors}," +
            "#{affiliations},#{doi},#{title},#{journal},#{abs},#{fulltext},#{references},#{pubdate},#{pdf_url},#{type}," +
            "#{pic_url},#{pic_text},#{keywords},#{fulltext_url},#{download},#{ch_title},#{re_uuid},#{or_title},#{browse},#{chemicallist}," +
            "#{labels})"})
    int insertData(PaperMysqlBean paperMysqlBean);
    @Delete("DELETE FROM science WHERE uuid = #{uuid}")
    int deleteById(String uuid);
    @Update("UPDATE `wy`.`science` SET `authors` = #{authors}, `affiliations` = #{affiliations}," +
            " `doi` = #{doi}, `title` = #{title}, `journal` = #{journal}, `abs` = #{abs}, `fulltext` = #{fulltext}," +
            " `references` = #{references}, `pubdate` = #{pubdate}, `pdf_url` = #{pdf_url}, `type` = #{type},`pic_url` = #{pic_url}," +
            " `pic_text` = #{pic_text}, `keywords` = #{keywords}, `fulltext_url` = #{fulltext_url}, `download` = #{download}, `ch_title` = #{ch_title}," +
            " `re_uuid` = #{re_uuid}, `or_title` = #{or_title}, `browse` = #{browse}, `chemicallist` = #{chemicallist}, `labels` = #{labels} WHERE `uuid` = #{uuid}")
    int updateData(PaperMysqlBean paperMysqlBean);
}
