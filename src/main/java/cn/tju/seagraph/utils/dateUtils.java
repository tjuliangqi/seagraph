package cn.tju.seagraph.utils;

import cn.tju.seagraph.dao.DataMapper;
import cn.tju.seagraph.dao.StatisticsMapper;
import cn.tju.seagraph.daomain.Statistics;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class dateUtils {
    public static String gainDate(){
        Date date = new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String dateStr=sdf.format(date);
        return dateStr;
    }

    public static void update(StatisticsMapper statisticsMapper, int type, String date){
        List<Statistics> list = statisticsMapper.selectStatisticsByDate(date);
        if (list.size()==0){
            statisticsMapper.insertStatistics(date);
        }
        List<Statistics> list1 = statisticsMapper.selectStatisticsByDate(date);
        Statistics statistics = list1.get(0);
        switch (type){
            case 0:{
                int n = statistics.getLogin();
                n++;
                statistics.setLogin(n);
                break;
            }
            case 1:{
                int n = statistics.getRegiste();
                n++;
                statistics.setRegiste(n);
                break;
            }
            case 2:{
                int n = statistics.getDetail();
                n++;
                statistics.setDetail(n);
                break;
            }
            case 3:{
                int n = statistics.getDownload();
                n++;
                statistics.setDownload(n);
                break;
            }
            default:break;
        }
        statisticsMapper.updateStatistics(statistics);
    }
    
}
