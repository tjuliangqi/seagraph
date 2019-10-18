package cn.tju.seagraph.controller;


import cn.tju.seagraph.dao.AuthorMapper;
import cn.tju.seagraph.dao.StatisticsMapper;
import cn.tju.seagraph.daomain.Author;
import cn.tju.seagraph.daomain.AuthorEsBean;
import cn.tju.seagraph.daomain.RetResponse;
import cn.tju.seagraph.daomain.RetResult;
import cn.tju.seagraph.service.AuthorSearch;
import cn.tju.seagraph.service.EmailService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController

@RequestMapping("/author")
public class AuthorController {
    @Autowired
    AuthorMapper authorMapper;
    @Autowired
    EmailService emailService;
    @Autowired
    StatisticsMapper statisticsMapper;
    @RequestMapping(value = "/searchList", method = RequestMethod.POST)
    public RetResult<List> searchList(@RequestBody Map json) throws IOException, JSONException {
        AuthorSearch authorSearch = new AuthorSearch();
        System.out.println(json.get("type"));
        System.out.println(json.get("value"));
        System.out.println(json.get("email"));
        System.out.println(json.get("ifPrepara"));
        System.out.println(json.get("preparaString"));
        List search = authorSearch.authorSearchList(json.get("type").toString(), json.get("value").toString());
        if (search.size()>0){
//            System.out.println(search.get("pic_url"));
            return RetResponse.makeOKRsp(search);
        }else {
            return RetResponse.makeErrRsp("查无数据");
        }
//        return null;
    }

    @RequestMapping(value = "/prepara", method = RequestMethod.POST)
    public RetResult<AuthorEsBean> prePara(@RequestBody Map<String,String>json) throws IOException, JSONException {
        AuthorSearch authorSearch = new AuthorSearch();
        System.out.println(json.get("type"));
        System.out.println(json.get("value"));
        AuthorEsBean search = authorSearch.authorSearchPrepara(json.get("type"), json.get("value"));
        if (search.getPaperNum()>0){
//            System.out.println(search.get("pic_url"));
            return RetResponse.makeOKRsp(search);
        }else {
            return RetResponse.makeErrRsp("查无数据");
        }
    }


    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public RetResult<AuthorEsBean> detail(@RequestBody Map<String,String>json) throws IOException, JSONException {
        AuthorSearch authorSearch = new AuthorSearch();
        List<Author> list = authorMapper.getAuthorById(json.get("id"));
        if (list.size()>0){
            AuthorEsBean search = authorSearch.authorSearchDetail(list);
//            System.out.println(search.get("pic_url"));
            return RetResponse.makeOKRsp(search);
        }else {
            return RetResponse.makeErrRsp("查无数据");
        }
    }
}

