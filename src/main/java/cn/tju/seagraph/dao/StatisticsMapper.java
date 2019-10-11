package cn.tju.seagraph.dao;

import cn.tju.seagraph.daomain.Statistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StatisticsMapper {
    List<Statistics> selectStatisticsByDate(@Param("date") String date);
    int insertStatistics(@Param("date") String date);
    int updateStatistics(@Param("statistics") Statistics statistics);
    int deleteByDate(@Param("date") String date);
}
