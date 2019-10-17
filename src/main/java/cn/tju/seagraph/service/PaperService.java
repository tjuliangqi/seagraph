package cn.tju.seagraph.service;

import cn.tju.seagraph.Config;
import cn.tju.seagraph.dao.PaperMapper;
import cn.tju.seagraph.daomain.*;
import cn.tju.seagraph.utils.EsUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

import static cn.tju.seagraph.utils.JsonToMapUtils.strToMap;

public class PaperService {

    public static String[] toStringList(String listString){
        String[] strings = listString.replace("[","").replace("]","").replace("\"","").split(",");
        return strings;
    }

    public static String[] splitAffiliations(String affiliations){
        String[] strings = affiliations.replace("['","").replace("']","").split("', '");
        return strings;
    }

    public static List pubdateRange(String type, String value) throws IOException {
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.getConnection();
        List pubdate = new ArrayList();
        QueryBuilder queryBuilder = null;
        SearchSourceBuilder searchSourceBuilderDESC = new SearchSourceBuilder();
        SearchSourceBuilder searchSourceBuilderASC = new SearchSourceBuilder();
        searchSourceBuilderDESC.sort("pubdate", SortOrder.DESC);
        searchSourceBuilderASC.sort("pubdate", SortOrder.ASC);
        SearchRequest searchRequestDESC = new SearchRequest(Config.INDEX);
        SearchRequest searchRequestASC = new SearchRequest(Config.INDEX);

        if (type.equals("0")) {
            queryBuilder = QueryBuilders.matchQuery("title", value);
        }

        searchSourceBuilderDESC.query(queryBuilder);
        searchRequestDESC.source(searchSourceBuilderDESC);
        searchSourceBuilderASC.query(queryBuilder);
        searchRequestASC.source(searchSourceBuilderASC);
        SearchResponse searchResponseDESC;
        SearchResponse searchResponseASC;
        try {
            searchResponseDESC = client.search(searchRequestDESC, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            client.close();
            pubdate.add("es查询错误");
            return pubdate;
        }
        try {
            searchResponseASC = client.search(searchRequestASC, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            client.close();
            pubdate.add("es查询错误");
            return pubdate;
        }
        client.close();
        SearchHit[] searchHitsDESC = searchResponseDESC.getHits().getHits();
        SearchHit[] searchHitsASC = searchResponseASC.getHits().getHits();
        System.out.println("正序：" + searchHitsASC.length);
        System.out.println("反序：" + searchHitsDESC.length);
        pubdate.add(searchHitsASC[0].getSourceAsMap().get("pubdate"));
        pubdate.add(searchHitsDESC[0].getSourceAsMap().get("pubdate"));
        return pubdate;

    }


    public static PaperEsBean hitToBean(SearchHit searchHit){
        PaperEsBean paperEsBean = new PaperEsBean();
        Map hitMap = searchHit.getSourceAsMap();
        paperEsBean.setUuid(searchHit.getId().split("-")[1]);
        paperEsBean.setAuthors(String.valueOf(hitMap.get("author")));
        paperEsBean.setAffiliations(String.valueOf(hitMap.get("affiliations")));
        paperEsBean.setTitle(String.valueOf(hitMap.get("title")));
        paperEsBean.setJournal(String.valueOf(hitMap.get("journal")));
        paperEsBean.setAbs(String.valueOf(hitMap.get("abs")));
        paperEsBean.setPubdate(String.valueOf(hitMap.get("pubdate")));
        paperEsBean.setType(String.valueOf(hitMap.get("type")));
        paperEsBean.setBrowse(String.valueOf(hitMap.get("browse")));
        paperEsBean.setKeywords(String.valueOf(hitMap.get("keywords")));
        paperEsBean.setChemicallist(String.valueOf(hitMap.get("chemicallist")));
        paperEsBean.setLabels(String.valueOf(hitMap.get("labels")));
        paperEsBean.setOr_title(String.valueOf(hitMap.get("or_title")));
        paperEsBean.setCh_title(String.valueOf(hitMap.get("ch_title")));

        return paperEsBean;
    }

    public static RetResult<List> searchList(String type, String value, Boolean ifPrepara, String preparaString, int page) throws IOException {
        List result = new ArrayList();
        Map map = new HashMap();
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.getConnection();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(10*(page-1));
        SearchRequest searchRequest = new SearchRequest(Config.INDEX);
        if (type.equals("0")){
            boolQueryBuilder.must(QueryBuilders.matchQuery("title",value));
            if (ifPrepara){
                try {
                    map = strToMap(preparaString);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return RetResponse.makeErrRsp("json解析错误");
                }
                for (Object key : map.keySet()){
                    if (key.toString().equals("journal")){
                        if (map.get(key).toString().equals("")){
                            continue;
                        }
                        String[] strings = toStringList(map.get(key).toString());
                        for (String each :strings){
                            boolQueryBuilder.filter(QueryBuilders.matchQuery("journal",each.trim()));
                        }
                    }
                    if (key.toString().equals("pubdate")){
                        if (map.get(key).toString().equals("")){
                            continue;
                        }
                        String[] strings = toStringList(map.get(key).toString());
                        boolQueryBuilder.filter(QueryBuilders.rangeQuery("pubdate").from(strings[0]).to(strings[1]));
                    }
                    if (key.toString().equals("affiliations")){
                        if (map.get(key).toString().equals("")){
                            continue;
                        }
                        String[] strings = toStringList(map.get(key).toString());
                        for (String each :strings){
                            boolQueryBuilder.filter(QueryBuilders.matchQuery("affiliations",each.trim()));
                        }
                    }
                    if (key.toString().equals("labels")){
                        if (map.get(key).toString().equals("")){
                            continue;
                        }
                        String[] strings = toStringList(map.get(key).toString());
                        for (String each :strings){
                            boolQueryBuilder.filter(QueryBuilders.matchQuery("labels",each.trim()));
                        }
                    }
                }
            }
        }
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            client.close();
            return RetResponse.makeErrRsp("es查询错误");
        }
        client.close();
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit searchHit : searchHits){
            result.add(hitToBean(searchHit));
        }
        System.out.println(result);
        return RetResponse.makeRsp(20000,"ok",result);
    }

    public static RetResult<FilterBean> prepara(String type, String value) throws IOException {


        Map map = new HashMap();
        FilterBean filterBean = new FilterBean();
        List pubdate = new ArrayList();
        Set journal = new HashSet();
        Set affiliations = new HashSet();
        Set labels = new HashSet();
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.getConnection();
        QueryBuilder queryBuilder = null;
        if (type.equals("0")){
            queryBuilder = QueryBuilders.matchQuery("title", value);
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SearchRequest searchRequest = new SearchRequest(Config.INDEX);
        searchSourceBuilder.size(100);
        searchSourceBuilder.query(queryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse;

        try {
            pubdate = pubdateRange(type,value);
        }catch (Exception e){
            System.out.println(e);
            return RetResponse.makeErrRsp("es查询错误");
        }

        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            client.close();
            return RetResponse.makeErrRsp("es查询错误");
        }
        client.close();
        SearchHit[] searchHits = searchResponse.getHits().getHits();

        for (SearchHit searchHit : searchHits){
            Map hitmap = searchHit.getSourceAsMap();
            journal.add(hitmap.get("journal"));
            String[] affiliationList = splitAffiliations(String.valueOf(hitmap.get("affiliations")));
            for (String affiliation : affiliationList){
                for (String each : affiliation.split(";")){
                    if (affiliation.length() < 2){
                        continue;
                    }
                    affiliations.add(each.trim());
                }
            }
            String[] labelList = toStringList(String.valueOf(hitmap.get("labels")));
            for (String label : labelList){
                if (label.length() < 2){
                    continue;
                }
                labels.add(label.replace("'","").trim());
            }

        }
        filterBean.setPubdate(pubdate);
        filterBean.setAffiliations(affiliations);
        filterBean.setJournal(journal);
        filterBean.setLabels(labels);
        System.out.println(filterBean.toString());

        return RetResponse.makeRsp(20000,"ok",filterBean);

    }


    public static void main(String[] args) throws IOException {
        String jsonStr = "{journal:['Science'],pubdate:['1966-06-21T00:00:00','2019-06-21T00:00:00'],affiliations:['L. Rowen and L. Hood are in the Department of Molecular Biotechnology, University of Washington, Seattle, WA 98195-7730, USA.'],labels:['fluorescence', 'detect']}";
        String title = "The Complete 685-Kilobase DNA Sequence of the Human β T Cell Receptor Locus";
        RetResult<List> result1 = searchList("0",title,Boolean.TRUE,jsonStr,1);
        System.out.println("************");
        RetResult<FilterBean> result2 = prepara("0",title);


    }
}
