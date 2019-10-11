package cn.tju.seagraph.dao;

import cn.tju.seagraph.daomain.Conference;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ConferenceMapper {
    List<Conference> getConferenceByDate(@Param("date") String date);
    List<Conference> getAllConference();
    int insertConference(@Param("conference") Conference conference);
    int deleteById(@Param("id") int id);
    int updateConference(@Param("conference") Conference conference);
}
