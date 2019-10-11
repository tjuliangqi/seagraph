package cn.tju.seagraph;

import cn.tju.seagraph.dao.dataMapper;
import cn.tju.seagraph.daomain.data;
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
    dataMapper dataMapper;
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
}
