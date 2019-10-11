package cn.tju.seagraph;

import cn.tju.seagraph.dao.ConferenceMapper;
import cn.tju.seagraph.dao.DataMapper;
import cn.tju.seagraph.dao.StatisticsMapper;
import cn.tju.seagraph.daomain.Conference;
import cn.tju.seagraph.daomain.Statistics;
import cn.tju.seagraph.daomain.data;
import cn.tju.seagraph.utils.dateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SeagraphApplicationTests {

    @Autowired
    DataMapper dataMapper;
    @Autowired
    StatisticsMapper statisticsMapper;
    @Autowired
    ConferenceMapper conferenceMapper;
    @Test
    public void contextLoads() {
    }
    @Test
    public void testData(){
        List<data> list = dataMapper.getDataById(1);
        System.out.println(list.get(0).getLabels());
        int i = dataMapper.insertData(list.get(0));
        List<data> list1 = dataMapper.getDataList();
        System.out.println(list1.size());
        data data = list1.get(1);
        System.out.println(data.getCount());
        data.setCount(91);
        int k = dataMapper.updateData(data);
        List<data> list2 = dataMapper.getDataById(2);
        System.out.println(list2.get(0).getCount());
        int j = dataMapper.deleteById(2);
        List<data> list3 = dataMapper.getDataList();
        System.out.println(list3.size());

    }
    @Test
    public void testStatistics(){
        int i = statisticsMapper.insertStatistics(dateUtils.gainDate());
        List<Statistics> list1 = statisticsMapper.selectStatisticsByDate(dateUtils.gainDate());
        System.out.println(list1.size());
        Statistics statistics = list1.get(0);
        System.out.println(statistics.getDetail());
        statistics.setDetail(1);
        int k = statisticsMapper.updateStatistics(statistics);
        List<Statistics> list2 = statisticsMapper.selectStatisticsByDate(dateUtils.gainDate());
        System.out.println(list2.get(0).getDetail());
        int j = statisticsMapper.deleteByDate(dateUtils.gainDate());
        List<Statistics> list3 = statisticsMapper.selectStatisticsByDate(dateUtils.gainDate());
        System.out.println(list3.size());

    }
    @Test
    public void testConference(){
        List<String> list = new ArrayList<>();
        list.add("机器学习");
        list.add("计算机视觉");
        Conference conference = new Conference();
        conference.setDate(dateUtils.gainDate());
        conference.setHomepage("1");
        conference.setLabels(list.toString());
        conference.setLevel("A");
        conference.setLocation("China");
        conference.setName("VALSE");
        conferenceMapper.insertConference(conference);
        conference.setLevel("B");
        conference.setId(1);
        int i = conferenceMapper.updateConference(conference);
        List<Conference> list1 = conferenceMapper.getAllConference();
        for (Conference c:list1) {
            System.out.println(c.getLevel());
        }
        int k = conferenceMapper.deleteById(2);
        List<Conference> list2 = conferenceMapper.getAllConference();
        System.out.println(list2.size());
    }
}
