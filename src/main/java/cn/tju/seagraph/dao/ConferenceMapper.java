package cn.tju.seagraph.dao;

import cn.tju.seagraph.daomain.Conference;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ConferenceMapper {
    List<Conference> getConferenceByFilter(@Param("datefrom") String datefrom, @Param("dateto") String dateto, @Param("item") List<String> item);
    List<Conference> getAllConference();
    Integer insertConference(@Param("conference") Conference conference);
    int deleteById(@Param("id") int id);
    int updateConference(@Param("conference") Conference conference);
}
