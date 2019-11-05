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
import java.util.*;

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
    public RetResult<Map<String,Object>> searchList(@RequestBody Map json) throws IOException, JSONException {
        AuthorSearch authorSearch = new AuthorSearch();
//        System.out.println(json.get("type"));
//        System.out.println(json.get("value"));
//        System.out.println(json.get("email"));
//        System.out.println(json.get("ifPrepara"));
//        System.out.println(json.get("preparaString"));
        Map<String,Object> search = authorSearch.authorSearchList(json.get("type").toString(), json.get("value").toString(),json.get("page").toString());
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
//        System.out.println(json.get("type"));
//        System.out.println(json.get("value"));
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

    @RequestMapping(value = "/shortestPath",method = RequestMethod.POST)
    public RetResult<List> ShortestPath(@RequestBody Map<String,List>json){
//        System.out.println(map);
        Random r = new Random();
        List l = new ArrayList();
        String[] names1 = {"Qiqun Zeng", "Iacovos P. Michael", "Peng Zhang"};
        String[] names2 = {"Sadegh Saghafinia", "Graham Knott", "Wei Jiao", "Brian D. McCabe"};
        String[] names3 = {"José A. Galván", "Hugh P. C. Robinson", "Inti Zlobec"};
        String[] names4 = {"Giovanni Ciriello", "Douglas Hanahan", "Andres Barria"};
        Set<String> setnames1 = new HashSet<>(Arrays.asList(names1));
        Set<String> setnames2 = new HashSet<>(Arrays.asList(names2));
        Set<String> setnames3 = new HashSet<>(Arrays.asList(names3));
        Set<String> setnames4 = new HashSet<>(Arrays.asList(names4));
        List<Set> shortpath = new ArrayList<Set>();
        shortpath.add(setnames1);
        shortpath.add(setnames2);
        shortpath.add(setnames3);
        shortpath.add(setnames4);


        return RetResponse.makeOKRsp(shortpath);
    }
}

