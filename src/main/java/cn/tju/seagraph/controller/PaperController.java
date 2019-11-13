package cn.tju.seagraph.controller;


import cn.tju.seagraph.dao.PaperMapper;
import cn.tju.seagraph.daomain.*;
import org.apache.lucene.analysis.miscellaneous.StemmerOverrideFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

import static cn.tju.seagraph.service.PaperService.*;

@RestController
@RequestMapping("/paper")
public class PaperController {
    @Autowired
    PaperMapper paperMapper;

    @RequestMapping(value = "/searchList",method = RequestMethod.POST)
    public RetResult<Map<String,Object>> searchListResponse(@RequestBody Map<String,String> map) throws IOException {
//        System.out.println(map);
        String type = String.valueOf(map.get("type"));
        String value = String.valueOf(map.get("value"));
        Boolean ifPrepara = Boolean.valueOf(map.get("ifPrepara"));
        String preparaString = String.valueOf(map.get("preparaString"));
        int page = Integer.valueOf(map.get("page"));
        RetResult<Map<String,Object>> result = null;
        if (type.equals("3")){
            Map resultMap = new HashMap();
            List<Map<String,Object>> resultList = new ArrayList<>();
            System.out.println(preparaString);
            String[] strs = value.replace("\"","").replace("[","").replace("]","").split(",");
            int count = strs.length;
            for (String str : strs){
                System.out.println(str);
                PaperMysqlBean paperMysqlBean = paperMapper.getDataById(str);
                int browse;
                if (paperMysqlBean.getBrowse() != null){
                    browse = Integer.valueOf(paperMysqlBean.getBrowse())+1;
                }
                else {
                    browse = 1;
                }
                paperMysqlBean.setBrowse(String.valueOf(browse));
                Map<String,Object> mysqlMap = formatDetail(paperMysqlBean);
                resultList.add(mysqlMapToESMap(mysqlMap));
                paperMapper.updateData(paperMysqlBean);
            }
            resultMap.put("result",resultList);
            resultMap.put("count",count);
            result = RetResponse.makeOKRsp(resultMap);
        }else {
            result = searchList(type,value,ifPrepara,preparaString,page);
        }

        return result;
    }

    @RequestMapping(value = "/prepara",method = RequestMethod.POST)
    public RetResult<FilterBean> preparaResponse(@RequestBody Map<String,String> map) throws IOException {
//        System.out.println(map);
        String type = map.get("type");
        String value = map.get("value");
        RetResult<FilterBean> result = prepara(type,value);
        return result;
    }

    @RequestMapping(value = "/detail",method = RequestMethod.POST)
    public RetResult<Map<String,Object>> detailResponse(@RequestBody Map<String,String> map){
        System.out.println(map);
        int browse;
        PaperMysqlBean result = paperMapper.getDataById(map.get("uuid"));
        PaperMysqlBean paperMysqlBean = result;
        Map<String,Object> resultMap = new HashMap<>();
        System.out.println(result);
        System.out.println("**********");
        System.out.println(paperMysqlBean);
        if (paperMysqlBean.getBrowse() != null){
            browse = Integer.valueOf(paperMysqlBean.getBrowse())+1;
        }
        else {
            browse = 1;
        }
        paperMysqlBean.setBrowse(String.valueOf(browse));
        paperMapper.updateData(paperMysqlBean);
        resultMap = formatDetail(paperMysqlBean);
        return RetResponse.makeOKRsp(resultMap);
    }

    @RequestMapping(value = "/keywordsheat",method = RequestMethod.POST)
    public RetResult<List> KeywordsHeat(){
//        System.out.println(map);
        Random r = new Random();
        List l = new ArrayList();
        String[] keys = {"Immunotherapy", "Innate immunity", "Signal transduction"};
        for (int j = 0;j<keys.length;j++){

            Map m = new HashMap();
//
            m.put("keyword",keys[j]);
            for (int i = 1 ; i<=12;i++){
                m.put(""+i,r.nextInt(99)+1+"");
            }
            l.add(m);
        }

        return RetResponse.makeOKRsp(l);
    }



}
