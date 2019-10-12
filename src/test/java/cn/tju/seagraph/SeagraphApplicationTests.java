package cn.tju.seagraph;

import cn.tju.seagraph.dao.DataMapper;
<<<<<<< Updated upstream
=======
import cn.tju.seagraph.dao.StatisticsMapper;
import cn.tju.seagraph.dao.authorMapper;
import cn.tju.seagraph.daomain.Author;
import cn.tju.seagraph.daomain.Conference;
import cn.tju.seagraph.daomain.Statistics;
>>>>>>> Stashed changes
import cn.tju.seagraph.daomain.data;
import cn.tju.seagraph.daomain.paperMysqlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SeagraphApplicationTests {

    @Autowired
    DataMapper dataMapper;
    @Autowired
<<<<<<< Updated upstream
    cn.tju.seagraph.dao.paperMapper paperMapper;
=======
    StatisticsMapper statisticsMapper;
    @Autowired
    ConferenceMapper conferenceMapper;
    @Autowired
    authorMapper authorMapper;
>>>>>>> Stashed changes
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
        List<cn.tju.seagraph.daomain.data> list2 = dataMapper.getDataById(5);
        System.out.println(list2.get(0).getCount());
        int j = dataMapper.deleteById(2);
        List<cn.tju.seagraph.daomain.data> list3 = dataMapper.getDataList();
        System.out.println(list3.size());

    }

    @Test
    public void testPaper(){
        paperMysqlBean paperMysqlBean = paperMapper.getDataById("2f03e746ea3611e9a3d800d861171bd5");
        System.out.println(paperMysqlBean.toString());
        System.out.println("测试list");
        List<cn.tju.seagraph.daomain.paperMysqlBean> list = paperMapper.getDataList();
        for(cn.tju.seagraph.daomain.paperMysqlBean each:list){
            System.out.println("*******");
            System.out.println(each.toString());
        }
        cn.tju.seagraph.daomain.paperMysqlBean t1 = new paperMysqlBean();
        t1.setUuid("1245422");

        System.out.println("测试delete");
        paperMapper.deleteById("12454");

        System.out.println("测试insert");
        System.out.println(t1.toString());
        paperMapper.insertData(t1);

        System.out.println("测试update");
        t1.setAbs("wedswerwerf");
        paperMapper.updateData(t1);


    }
    @Test
    public void testAuthor(){
        //通过ID查询
        List<Author> list = authorMapper.getAuthorById("0005a3a9b58b4ebc81254ada09cd4218");
        System.out.println(list.get(0).getName());
        System.out.println(list.get(0).toString());

        //查询整个列表
        List<Author> list1 = authorMapper.getAuthorList();
        System.out.println(list1.size());
        Author author = list1.get(4);
        System.out.println(author.getLabels());

        //增加
        int i = authorMapper.insertAuthor(author);
        System.out.println(i);

        //修改
        author.setPaperNum(91);
        author.setName("zhangsan");
        author.setAffiliations("hello");
        int k = authorMapper.updateAuthor(author);
        System.out.println(k);

        //删除
        int j = authorMapper.deleteById("fff9b37fa96c499aa8f3228d9eb3251a");
        System.out.println(j);


    }
}
