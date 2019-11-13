package cn.tju.seagraph.controller;

import cn.tju.seagraph.dao.AffiliationsMapper;
import cn.tju.seagraph.daomain.AffiliationsEsBean;
import cn.tju.seagraph.daomain.AffiliationsMysqlBean;
import cn.tju.seagraph.daomain.RetResponse;
import cn.tju.seagraph.daomain.RetResult;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

import static cn.tju.seagraph.service.AffiliationsService.affiliationPrepara;
import static cn.tju.seagraph.service.AffiliationsService.affiliationSearchList;
import static cn.tju.seagraph.service.AffiliationsService.mySqlBeanToEsBean;


@RestController

@RequestMapping("/affiliation")
public class AffiliationsController {
    @Autowired
    AffiliationsMapper affiliationsMapper;
    @RequestMapping(value = "/searchList", method = RequestMethod.POST)
    public RetResult<Map<String,Object>> searchList(@RequestBody Map<String,String> map) throws IOException, JSONException {
        //RetResult retResult = new RetResult();
        String type = String.valueOf(map.get("type"));
        String value = String.valueOf(map.get("value"));
        Boolean ifPrepara = Boolean.valueOf(map.get("ifPrepara"));
        String preparaString = String.valueOf(map.get("preparaString"));
        int page = Integer.valueOf(map.get("page"));

        Map<String,Object> affiliationsEsBeans = affiliationSearchList(type, value, ifPrepara, preparaString, page);
        return RetResponse.makeOKRsp(affiliationsEsBeans);
    }


    @RequestMapping(value = "/prepara", method = RequestMethod.POST)
    public RetResult<Map> prepara(@RequestBody Map<String,String> map) throws IOException {
        //RetResult retResult = new RetResult();
        String type = String.valueOf(map.get("type"));
        String value = String.valueOf(map.get("value"));
        Map<String, Object> selectTags = affiliationPrepara(type, value);

        return RetResponse.makeOKRsp(selectTags);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public RetResult<Map> detail(@RequestBody Map<String,String> map) throws IOException {
        //RetResult retResult = new RetResult();
        String id = String.valueOf(map.get("id"));
        AffiliationsMysqlBean affiliationsMysqlBean = affiliationsMapper.getDataById(id);

        Map<String, Object> affiliationsEsBean = mySqlBeanToEsBean(affiliationsMysqlBean);
        //retResult.setCode(20000).setMsg("SUCCESS").setData(affiliationsMysqlBean);
        return RetResponse.makeOKRsp(affiliationsEsBean);
    }
}