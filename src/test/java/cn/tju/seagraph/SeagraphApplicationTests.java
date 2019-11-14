package cn.tju.seagraph;

import cn.tju.seagraph.dao.*;
import cn.tju.seagraph.daomain.*;
import cn.tju.seagraph.utils.dateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.cypher.internal.frontend.v2_3.ast.functions.Str;
import org.neo4j.cypher.internal.frontend.v3_2.ast.functions.Nodes;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.v1.Values.parameters;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SeagraphApplicationTests {

    @Autowired
    UserMapper userMapper;
    @Autowired
    DataMapper dataMapper;
    @Autowired
    StatisticsMapper statisticsMapper;
    @Autowired
    ConferenceMapper conferenceMapper;
    @Autowired
    PaperMapper paperMapper;

    @Test
    public void contextLoads() {
    }
    @Test
    public void testData(){
        List<data> list = dataMapper.getDataById(1);
//        System.out.println(list.get(0).getLabels());
        int i = dataMapper.insertData(list.get(0));
        List<data> list1 = dataMapper.getDataList();
//        System.out.println(list1.size());
        data data = list1.get(1);
//        System.out.println(data.getCount());
//        data.setCount(91);
        int k = dataMapper.updateData(data);
        List<data> list2 = dataMapper.getDataById(2);
//        System.out.println(list2.get(0).getCount());
        int j = dataMapper.deleteById(2);
        List<data> list3 = dataMapper.getDataList();
//        System.out.println(list3.size());

    }
    @Test
    public void testStatistics(){
        int i = statisticsMapper.insertStatistics(dateUtils.gainDate());
        List<Statistics> list1 = statisticsMapper.selectStatisticsByDate(dateUtils.gainDate());
//        System.out.println(list1.size());
        Statistics statistics = list1.get(0);
//        System.out.println(statistics.getDetail());
        statistics.setDetail(1);
        int k = statisticsMapper.updateStatistics(statistics);
        List<Statistics> list2 = statisticsMapper.selectStatisticsByDate(dateUtils.gainDate());
//        System.out.println(list2.get(0).getDetail());
        int j = statisticsMapper.deleteByDate(dateUtils.gainDate());
        List<Statistics> list3 = statisticsMapper.selectStatisticsByDate(dateUtils.gainDate());
//        System.out.println(list3.size());

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
//        System.out.println(list2.size());
    }

    @Test
    public void testPaper(){
        PaperMysqlBean paperMysqlBean = paperMapper.getDataById("2f03e746ea3611e9a3d800d861171bd5");
//        System.out.println(paperMysqlBean.toString());
//        System.out.println("测试list");
        List<PaperMysqlBean> list = paperMapper.getDataList();
        for(PaperMysqlBean each:list){
//            System.out.println("*******");
//            System.out.println(each.toString());
        }
        PaperMysqlBean t1 = new PaperMysqlBean();
        t1.setUuid("1245422");

//        System.out.println("测试delete");
        paperMapper.deleteById("1245422");

//        System.out.println("测试insert");
//        System.out.println(t1.toString());
        paperMapper.insertData(t1);

//        System.out.println("测试update");
        t1.setAbs("wedswerwerf");
        paperMapper.updateData(t1);


    }

    @Test
    public void Neo4jTest(){
        try{
            Driver driver = GraphDatabase.driver( "bolt://192.168.199.205:7687", AuthTokens.basic( "neo4j", "tju123" ) );
            Session session = driver.session();
            String name1 = "KR Foltz";
            String name2 = "WJ Lennarz";
            StatementResult result = session.run( "MATCH n=allshortestPaths((a:author{title:{name1}})-[*]-(b:author{title:{name2}})) return n",
                    parameters( "name1", name1, "name2", name2));
            List<List> resultList = new ArrayList<>();
            while ( result.hasNext() )
            {
                List<String> list = new ArrayList<>();
                Record record = result.next();
                Iterable<Node> nodes = record.get("n").asPath().nodes();
                System.out.println(nodes);
                int count = 0;
                for (Node node : nodes){
                    list.add(node.get("title").asString());
                    count ++;
                }
                if (list.size() == 0){
                    resultList.add(null);
                }else {
                    list.remove(name1);
                    list.remove(name2);
                    resultList.add(list);
                }

            }


            session.close();
            driver.close();
            System.out.println("*******");
            System.out.println(resultList);

        }catch (Exception e){
            System.out.println(e);
        }



    }

}
