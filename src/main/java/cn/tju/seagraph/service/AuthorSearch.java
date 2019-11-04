package cn.tju.seagraph.service;

import cn.tju.seagraph.Config;
import cn.tju.seagraph.daomain.Author;
import cn.tju.seagraph.daomain.AuthorEsBean;
import cn.tju.seagraph.utils.EsUtils;
import cn.tju.seagraph.utils.JsonToMapUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONException;

import java.io.IOException;
import java.util.*;

public class AuthorSearch {

    public AuthorEsBean authorSearchDetail(List<Author> list) throws IOException, JSONException {
//        System.out.println(id);
//        List<Author> list = authorMapper.getAuthorById(id);
        AuthorEsBean aEB = new AuthorEsBean();


        String[] aff = list.get(0).getAffiliations().replace("['","").replace("']","").split("', '");
        Set affS = new HashSet();
        for (int i = 0; i <aff.length ; i++) {
            affS.add(aff[i]);
        }
        aEB.setAffiliations(affS);

        String[] lab = list.get(0).getLabels().replace("['","").replace("']","").split("', '");
        Set labS = new HashSet();
        for (int i = 0; i <lab.length ; i++) {
            labS.add(lab[i]);
        }
        aEB.setLabels(labS);
        aEB.setName(list.get(0).getName());
        aEB.setPic_url(list.get(0).getPic_url());
        aEB.setPaperList(list.get(0).getPaperList());
        aEB.setPaperUUID(list.get(0).getPaperUUID());
        aEB.setPaperNum(list.get(0).getPaperNum());
        return aEB;
//        Map authorResult = new HashMap();
//        AuthorEsBean result = new AuthorEsBean();
////        Map authorResultHandle = new HashMap();
//        EsUtils esUtils = new EsUtils();
//
//        RestHighLevelClient client = esUtils.client;
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        QueryBuilder match = QueryBuilders.matchQuery("uuid",id);
//        searchSourceBuilder.query(match);
//        SearchRequest searchRequest = new SearchRequest();
//        searchRequest.indices(Config.INDEX);
//        searchRequest.source(searchSourceBuilder);
//        SearchResponse searchResponse = client.search(searchRequest);
//        SearchHit[] searchHits = searchResponse.getHits().getHits();
//
//        JsonToMapUtils j = new JsonToMapUtils();
//        for (SearchHit searchHit:searchHits){
//            authorResult = j.strToMap(searchHit.toString());
//
//        }
//
//        if(authorResult.size()==0){
//            result.setPaperNum(0);
//        }
//        else {
//            String[] aff = authorResult.get("affiliations").toString().replace("['","").replace("']","").split("', '");
//            Set affS = new HashSet();
//            for (int i = 0; i <aff.length ; i++) {
//                affS.add(aff[i]);
//            }
//            String[] lab = authorResult.get("labels").toString().replace("['","").replace("']","").split("', '");
//            Set labS = new HashSet();
//            for (int i = 0; i <lab.length ; i++) {
//                labS.add(lab[i]);
//            }
//            result.setName(authorResult.get("name").toString());
//            result.setAffiliations(affS);
//            result.setLabels(labS);
//            result.setPic_url(authorResult.get("pic_url").toString());
//            result.setPaperList(authorResult.get("paperList").toString());
//            result.setPaperNum((int)authorResult.get("paperNum"));
//        }
//
//
//        return result;
    }

    public List<AuthorEsBean> authorSearchList(String type, String value) throws IOException, JSONException {

        List<AuthorEsBean> resultList = new ArrayList<AuthorEsBean>();
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.client;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder match = null;
        if (type.equals("0")) {
            match = QueryBuilders.matchQuery("name",value);
        }else{
            match = QueryBuilders.matchQuery("uuid",value);
        }
        searchSourceBuilder.query(match);
        searchSourceBuilder.size(100);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(Config.AUTHORINDEX);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHit[] searchHits = searchResponse.getHits().getHits();


        JsonToMapUtils j = new JsonToMapUtils();
        for (SearchHit searchHit:searchHits){
            Map authorListResult = new HashMap();
            AuthorEsBean result = new AuthorEsBean();
            authorListResult = j.strToMap(searchHit.toString());
            String[] aff = authorListResult.get("affiliations").toString().replace("['","").replace("']","").split("', '");
            Set affS = new HashSet();
            for (int i = 0; i <aff.length ; i++) {
                affS.add(aff[i]);
            }
            String[] lab = authorListResult.get("labels").toString().replace("['","").replace("']","").split("', '");
            Set labS = new HashSet();
            for (int i = 0; i <lab.length ; i++) {
                labS.add(lab[i]);
            }
            result.setName(authorListResult.get("name").toString());
            result.setAffiliations(affS);
            result.setLabels(labS);
            result.setPic_url(authorListResult.get("pic_url").toString());
            result.setPaperList(authorListResult.get("paperList").toString());
            result.setPaperNum((int)authorListResult.get("paperNum"));
            result.setId(authorListResult.get("uuid").toString());
            resultList.add(result);
        }

        return resultList;
    }
    public AuthorEsBean authorSearchPrepara(String type, String value) throws IOException, JSONException {

        AuthorEsBean result = new AuthorEsBean();
//        Map authorResultHandle = new HashMap();
        EsUtils esUtils = new EsUtils();

        RestHighLevelClient client = esUtils.client;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder match = null;
        if (type.equals("0")) {
            match = QueryBuilders.matchQuery("name",value);
        }else{
            match = QueryBuilders.matchQuery("uuid",value);
        }
        searchSourceBuilder.query(match);
        searchSourceBuilder.size(100);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(Config.AUTHORINDEX);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHit[] searchHits = searchResponse.getHits().getHits();

//        System.out.println(searchHits.length);

        JsonToMapUtils j = new JsonToMapUtils();
        Set authorresult = new HashSet();
        for (SearchHit searchHit:searchHits){
            Map authorPreResult = new HashMap();
            authorPreResult = j.strToMap(searchHit.toString());
//            System.out.println("=====================================");
//            System.out.println(authorPreResult.get("labels").toString());
//            System.out.println("=====================================");
            String[] lables;
            lables = authorPreResult.get("labels").toString().replace("['", "").replace("']", "").split("', '");
            for (int i = 0; i <lables.length ; i++) {
                authorresult.add(lables[i]);
            }

        }

        if(searchHits.length==0){
            result.setPaperNum(0);
        }
        else {
            result.setLabels(authorresult);
            result.setPaperNum(searchHits.length);
        }


        return result;
    }

}
