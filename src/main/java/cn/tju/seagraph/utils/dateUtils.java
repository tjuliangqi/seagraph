package cn.tju.seagraph.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class dateUtils {
    public static String gainDate(){
        Date date = new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String dateStr=sdf.format(date);
        return dateStr;
    }
}
