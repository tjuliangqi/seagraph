package cn.tju.seagraph.controller;

import cn.tju.seagraph.dao.AffiliationsMapper;
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


@RestController

@RequestMapping("/affiliation")
public class AffiliationsController {
    @Autowired
    AffiliationsMapper affiliationsMapper;
    @RequestMapping(value = "/searchList", method = RequestMethod.POST)
    public RetResult<ArrayList<AffiliationsMysqlBean>> searchList(@RequestParam("type") String type, @RequestParam("value") String value,
                                                                  @RequestParam("email") String email, @RequestParam("ifPrepara") String ifPrepara,
                                                                  @RequestParam("preparaString") String preparaString, @RequestParam("page") String page) throws IOException, JSONException {
        //RetResult retResult = new RetResult();
        boolean ifPrepara_new = Boolean.parseBoolean(ifPrepara);
        int page_new = Integer.parseInt(page);
        ArrayList<AffiliationsMysqlBean> affiliationsMysqlBeans = affiliationSearchList(type, value, ifPrepara_new, preparaString, page_new);
        //retResult.setCode(20000).setMsg("SUCCESS").setData(affiliationsMysqlBeans);
        return RetResponse.makeOKRsp(affiliationsMysqlBeans);
    }



    @RequestMapping(value = "/prepara", method = RequestMethod.POST)
    public RetResult<AffiliationsMysqlBean> prepara(@RequestParam("type") String type, @RequestParam("value") String value, @RequestParam("page") String page) throws IOException {
        //RetResult retResult = new RetResult();
        int page_new = Integer.parseInt(page);
        AffiliationsMysqlBean affiliationsMysqlBean = affiliationPrepara(type, value, page_new);
        //retResult.setCode(20000).setMsg("SUCCESS").setData(affiliationsMysqlBean);
        return RetResponse.makeOKRsp(affiliationsMysqlBean);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public RetResult<AffiliationsMysqlBean> detail(@RequestParam("id") String id, @RequestParam("email") String email) throws IOException {
        //RetResult retResult = new RetResult();
        AffiliationsMysqlBean affiliationsMysqlBean = affiliationsMapper.getDataById(id);
        //retResult.setCode(20000).setMsg("SUCCESS").setData(affiliationsMysqlBean);
        return RetResponse.makeOKRsp(affiliationsMysqlBean);
    }
}

