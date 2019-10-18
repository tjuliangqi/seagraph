package cn.tju.seagraph.controller;

import cn.tju.seagraph.dao.PaperMapper;
import cn.tju.seagraph.dao.StatisticsMapper;
import cn.tju.seagraph.daomain.PaperMysqlBean;
import cn.tju.seagraph.daomain.RetResponse;
import cn.tju.seagraph.daomain.RetResult;
import cn.tju.seagraph.daomain.Statistics;
import cn.tju.seagraph.utils.dateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController

@RequestMapping("/statistics")
public class StatisticsController {
    @Autowired
    StatisticsMapper statisticsMapper;
    @Autowired
    PaperMapper paperMapper;
    @RequestMapping(value = "/addDownload", method = RequestMethod.POST)
    public RetResult<String> Count(@RequestBody Map<String,String> json) {
        dateUtils.update(statisticsMapper,3,dateUtils.gainDate());
        dateUtils.update(statisticsMapper,3,"2000-01-01");
        PaperMysqlBean paperMysqlBean = paperMapper.getDataById(json.get("paperId"));
        int n = 0;
        if (paperMysqlBean.getDownload()==null){
            n =0;
        }else {
            n = Integer.valueOf(paperMysqlBean.getDownload());
        }
        n++;
        paperMysqlBean.setDownload(String.valueOf(n));
        paperMapper.updateData(paperMysqlBean);
        return RetResponse.makeOKRsp("ok");
    }

    @RequestMapping(value = "/statistics", method = RequestMethod.POST)
    public RetResult<List<Statistics>> getCount(@RequestBody Map<String,String> json) {
        List<Statistics> list0 = statisticsMapper.selectStatisticsByDate("2000-01-01");
        List<Statistics> list = statisticsMapper.selectStatisticsByDate(dateUtils.gainDate());
        List<Statistics> list1 = statisticsMapper.selectStatisticsByDate(json.get("date"));
        if (list.size()==0){
            list0.add(null);
        }else {
            list0.addAll(list);
        }
        if (list1.size()==0){
            list0.add(null);
        }else {
            list0.addAll(list1);
        }
        return RetResponse.makeOKRsp(list0);
    }
}
