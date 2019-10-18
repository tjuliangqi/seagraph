package cn.tju.seagraph.controller;

import cn.tju.seagraph.dao.AffiliationsMapper;
import cn.tju.seagraph.daomain.AffiliationsEsBean;
import cn.tju.seagraph.daomain.AffiliationsMysqlBean;
import cn.tju.seagraph.daomain.RetResponse;
import cn.tju.seagraph.daomain.RetResult;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public RetResult<ArrayList<AffiliationsEsBean>> searchList(@RequestParam("type") String type, @RequestParam("value") String value,
                                                               @RequestParam("email") String email, @RequestParam("ifPrepara") String ifPrepara,
                                                               @RequestParam("preparaString") String preparaString, @RequestParam("page") String page) throws IOException, JSONException {
        //RetResult retResult = new RetResult();
        boolean ifPrepara_new = Boolean.parseBoolean(ifPrepara);
        int page_new = Integer.parseInt(page);
        ArrayList<AffiliationsEsBean> affiliationsEsBeans = affiliationSearchList(type, value, ifPrepara_new, preparaString, page_new);
        //retResult.setCode(20000).setMsg("SUCCESS").setData(affiliationsMysqlBeans);
        return RetResponse.makeOKRsp(affiliationsEsBeans);
    }



    @RequestMapping(value = "/prepara", method = RequestMethod.POST)
    public RetResult<Map> prepara(@RequestParam("type") String type, @RequestParam("value") String value) throws IOException {
        //RetResult retResult = new RetResult();
        Map<String, Set> selectTags = affiliationPrepara(type, value);
        //retResult.setCode(20000).setMsg("SUCCESS").setData(affiliationsMysqlBean);
        return RetResponse.makeOKRsp(selectTags);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public RetResult<AffiliationsEsBean> detail(@RequestParam("id") String id, @RequestParam("email") String email) throws IOException {
        //RetResult retResult = new RetResult();
        AffiliationsMysqlBean affiliationsMysqlBean = affiliationsMapper.getDataById(id);
        AffiliationsEsBean affiliationsEsBean = mySqlBeanToEsBean(affiliationsMysqlBean);
        //retResult.setCode(20000).setMsg("SUCCESS").setData(affiliationsMysqlBean);
        return RetResponse.makeOKRsp(affiliationsEsBean);
    }
}

