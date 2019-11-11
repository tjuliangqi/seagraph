package cn.tju.seagraph.controller;

import cn.tju.seagraph.dao.DataMapper;
import cn.tju.seagraph.daomain.RetResponse;
import cn.tju.seagraph.daomain.RetResult;

import cn.tju.seagraph.daomain.data;
import cn.tju.seagraph.utils.JsonToMapUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController

@RequestMapping("/data")
public class DataController {
    @Autowired
    DataMapper dataMapper;
    @RequestMapping(value = "/label", method = RequestMethod.POST)
    public RetResult<List<Map<String,Object>>> getCount() {
        List<Map<String,Object>> mapList = new ArrayList<>();
        List<data> list = dataMapper.getDataList();
        for (data data:list){
            Map<String,Object> map = new HashMap<>();
            map.put("labels",data.getLabels());
            try {
                map.put("count",JsonToMapUtils.strToMap(data.getCount().replace("[","{").replace("]","}")));
            } catch (JSONException e) {
                System.out.println(data.getCount());
                RetResponse.makeErrRsp("查询失败");
            }
            mapList.add(map);
        }
        return RetResponse.makeOKRsp(mapList);
    }
}
