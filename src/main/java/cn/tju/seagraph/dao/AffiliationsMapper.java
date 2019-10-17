package cn.tju.seagraph.dao;
import cn.tju.seagraph.daomain.AffiliationsMysqlBean;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import java.util.List;

@Mapper
@Repository
public interface AffiliationsMapper {

    @Select("SELECT * FROM `affiliations` WHERE `uuid` = #{uuid}")
    AffiliationsMysqlBean getDataById(String uuid);

    @Select("SELECT * FROM `affiliations`")
    List<AffiliationsMysqlBean> getDataList();

    @Insert({"INSERT INTO `wy`.`affiliations`(`uuid`, `name`, `labels`, " +
            "`paperList`, `paperNum`, `influence`) VALUES (#{uuid},#{name}," +
            "#{labels},#{paperList},#{paperNum},#{influence})"})
    int insertData(AffiliationsMysqlBean affiliationsMysqlBean);

    @Delete("DELETE FROM affiliations WHERE uuid = #{uuid}")
    int deleteById(String uuid);

    @Update("UPDATE `wy`.`affiliations` SET `name` = #{name}, `labels` = #{labels}," +
            " `paperList` = #{paperList}, `paperNum` = #{paperNum}, `influence` = #{influence} WHERE `uuid` = #{uuid}")
    int updateData(AffiliationsMysqlBean affiliationsMysqlBean);
}
