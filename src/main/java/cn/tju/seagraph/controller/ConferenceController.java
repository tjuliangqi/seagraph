package cn.tju.seagraph.controller;

import cn.tju.seagraph.dao.ConferenceMapper;
import cn.tju.seagraph.daomain.Conference;
import cn.tju.seagraph.daomain.RetResponse;
import cn.tju.seagraph.daomain.RetResult;
import cn.tju.seagraph.service.ConferenceService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

import static cn.tju.seagraph.utils.JsonToMapUtils.strToMap;

@RestController

@RequestMapping("/conference")
public class ConferenceController {
    @Autowired
    ConferenceMapper conferenceMapper;
    @RequestMapping(value = "/prepara", method = RequestMethod.POST)
    public RetResult<Map> getPrepra(Map<String,String> map) {
        String type = String.valueOf(map.get("type"));
        String value = String.valueOf(map.get("value"));
        List<Conference> list = conferenceMapper.getAllConference();
        Set<String> dataset;
        try {
            dataset = ConferenceService.dateRange(type,value);
        } catch (IOException e) {
            return RetResponse.makeErrRsp("查询失败");
        }
        Set<String> labelSet = new HashSet<>();
        for (Conference conference:list){
            String labels = conference.getLabels();
            String[] labellist = labels.replace("[","").replace("]","").split(",");
            for (String str:labellist){
                labelSet.add(str.trim());
            }
        }
        Map<String,Set> result = new HashMap<>();
        result.put("date",dataset);
        result.put("labels",labelSet);
        return RetResponse.makeOKRsp(result);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public RetResult<String> add(@RequestBody Conference conference){
        conferenceMapper.insertConference(conference);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = "/searchList",method = RequestMethod.POST)
    public RetResult<List> searchListResponse(@RequestBody Map<String,String> map) throws IOException {
        String type = String.valueOf(map.get("type"));
        String value = String.valueOf(map.get("value"));
        Boolean ifPrepara = Boolean.valueOf(map.get("ifPrepara"));
        String preparaString = String.valueOf(map.get("preparaString"));
        int page = Integer.valueOf(map.get("page"));
        List<Conference> result = ConferenceService.getConferenceByFilter(type,value,ifPrepara,preparaString,page);
        if (result!=null){
            return RetResponse.makeOKRsp(result);
        }else {
            return RetResponse.makeErrRsp("error");
        }
    }
}
