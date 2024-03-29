package cn.tju.seagraph.controller;


import cn.tju.seagraph.dao.AuthorMapper;
import cn.tju.seagraph.dao.PaperMapper;
import cn.tju.seagraph.dao.StatisticsMapper;
import cn.tju.seagraph.daomain.*;
import cn.tju.seagraph.service.AuthorSearch;
import cn.tju.seagraph.service.EmailService;
import cn.tju.seagraph.utils.JsonToMapUtils;
import org.json.JSONException;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

import static cn.tju.seagraph.service.AuthorSearch.relationAuthors;
import static org.neo4j.driver.v1.Values.parameters;

@RestController

@RequestMapping("/author")
public class AuthorController {
    @Autowired
    AuthorMapper authorMapper;
    @Autowired
    PaperMapper paperMapper;
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
        Map<String,Object> search = authorSearch.authorSearchList(json.get("type").toString(), json.get("value").toString(),json.get("page").toString(),Boolean.valueOf(json.get("ifPrepara").toString()),json.get("preparaString").toString());
        if (search.size()>0){
//            System.out.println(search.get("pic_url"));
            return RetResponse.makeOKRsp(search);
        }else {
            return RetResponse.makeErrRsp("查无数据");
        }
//        return null;
    }

    @RequestMapping(value = "/prepara", method = RequestMethod.POST)
    public RetResult<Map> prePara(@RequestBody Map<String,String>json) throws IOException, JSONException {
        AuthorSearch authorSearch = new AuthorSearch();
//        System.out.println(json.get("type"));
//        System.out.println(json.get("value"));
        AuthorEsBean search = authorSearch.authorSearchPrepara(json.get("type"), json.get("value"));
        Map m = new HashMap();
        m.put("labels",search.getLabels());
        m.put("influence",search.getInfluence());
        if (search.getPaperNum()>0){
//            System.out.println(search.get("pic_url"));
            return RetResponse.makeOKRsp(m);
        }else {
            return RetResponse.makeErrRsp("查无数据");
        }
    }


    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public RetResult<Map> detail(@RequestBody Map<String,String>json) throws IOException, JSONException {
        AuthorSearch authorSearch = new AuthorSearch();
        List<Author> list = authorMapper.getAuthorById(json.get("id"));
        if (list.size()>0){
            Map search = authorSearch.authorSearchDetail(list);
//            System.out.println(search.get("pic_url"));
            return RetResponse.makeOKRsp(search);
        }else {
            return RetResponse.makeErrRsp("查无数据");
        }
    }

    @RequestMapping(value = "/shortestPath",method = RequestMethod.POST)
    public RetResult<List> ShortestPath(@RequestBody Map<String,String> json){
//        System.out.println(map);
        String name1 = json.get("author1");
        String name2 = json.get("author2");
        List<List> resultList = new ArrayList<>();
        try{
            Driver driver = GraphDatabase.driver( "bolt://192.168.199.205:7687", AuthTokens.basic( "neo4j", "tju123" ) );
            Session session = driver.session();
//            String name1 = "KR Foltz";
//            String name2 = "WJ Lennarz";
            StatementResult result = session.run( "MATCH n=allshortestPaths((a:author{title:{name1}})-[*]-(b:author{title:{name2}})) return n",
                    parameters( "name1", name1, "name2", name2));

            int count = 0;
            while ( result.hasNext() )
            {
                List<String> list = new ArrayList<>();
                Record record = result.next();
                Iterable<Node> nodes = record.get("n").asPath().nodes();

                for (Node node : nodes){
                    list.add(node.get("title").asString());
                    System.out.println(list);
                }

                list.remove(name1);
                list.remove(name2);
                resultList.add(list);
                count++;

            }
            session.close();
            driver.close();

            if (count == 0){
                return RetResponse.makeOKRsp(null);
            }
            if (resultList.get(0).size() == 0){
                return RetResponse.makeOKRsp(new ArrayList());
            }

            System.out.println("*******");
            System.out.println(resultList);

        }catch (Exception e){
            System.out.println(e);
        }

        return RetResponse.makeOKRsp(resultList);

    }


    @RequestMapping(value = "/relate", method = RequestMethod.POST)
    public RetResult<List<String>> relate(@RequestBody Map<String,String> json) {
        List<String> result;
        String uuid = json.get("uuid");
        List<Author> authorList = authorMapper.getAuthorById(uuid);
        Author author = authorList.get(0);
        try {
            result = relationAuthors(author.getName());
        } catch (IOException e) {
            return RetResponse.makeErrRsp("es查询错误");
        }
        return RetResponse.makeOKRsp(result);
    }

    @RequestMapping(value = "/tupu", method = RequestMethod.POST)
    public RetResult<List<Map>> tupu(@RequestBody Map<String,String> json) throws IOException, JSONException {
        List<Map> result = new ArrayList<>();

        String label = json.get("message");
        System.out.println(label);
        AuthorSearch authorSearch = new AuthorSearch();
        List<String> authorListUUID = authorSearch.tuPuGetauthor(label);
        for (String uuid :authorListUUID){
            List<Author> authorList = authorMapper.getAuthorById(uuid);
            Author author = authorList.get(0);
            Map paperUUID = new HashMap();
            paperUUID = JsonToMapUtils.strToMap(author.getPaperUUID());
            for (Object each:paperUUID.keySet()) {
                if(each.toString().equals(label)){
                    List<String> labelPaperList = new ArrayList<String>();
                    labelPaperList = Arrays.asList(paperUUID.get(each).toString().replace("[","").replace("]","").replace("\"","").split(","));
                    for (String paper:labelPaperList){
                        PaperMysqlBean eachpaper = paperMapper.getDataById(paper);
                        Map<String,String> oneLine = new HashMap<>();
                        oneLine.put("entity1",author.getName());
                        oneLine.put("entity1ID",author.getId());
                        String pertitle = new String();
                        String[] titleHead = eachpaper.getTitle().split(" ");
                        if(titleHead.length>=3){
                            pertitle = titleHead[0]+" "+titleHead[1]+" "+titleHead[2];
                        }else {
                            pertitle = eachpaper.getTitle();
                        }
                        oneLine.put("entity2",pertitle);
                        oneLine.put("entity2ID",eachpaper.getUuid());
                        oneLine.put("rel","发文");
                        System.out.println(oneLine);
                        result.add(oneLine);
                    }
                }
            }
        }
        return RetResponse.makeOKRsp(result);
    }
}

