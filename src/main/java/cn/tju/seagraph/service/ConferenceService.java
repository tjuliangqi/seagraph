package cn.tju.seagraph.service;

import cn.tju.seagraph.Config;
import cn.tju.seagraph.daomain.Conference;
import cn.tju.seagraph.daomain.RetResponse;
import cn.tju.seagraph.utils.EsUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.json.JSONException;

import java.io.IOException;
import java.util.*;

import static cn.tju.seagraph.service.PaperService.hitToBean;
import static cn.tju.seagraph.service.PaperService.toStringList;
import static cn.tju.seagraph.utils.JsonToMapUtils.strToMap;

public class ConferenceService {
    public static Set<String> dateRange(String type, String value) throws IOException {
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.getConnection();
        Set date = new TreeSet();
        QueryBuilder queryBuilder = null;
        SearchSourceBuilder searchSourceBuilderDESC = new SearchSourceBuilder();
        SearchSourceBuilder searchSourceBuilderASC = new SearchSourceBuilder();
        searchSourceBuilderDESC.sort("date", SortOrder.DESC);
        searchSourceBuilderASC.sort("date", SortOrder.ASC);
        SearchRequest searchRequestDESC = new SearchRequest(Config.CONFERENCEINDEX);
        SearchRequest searchRequestASC = new SearchRequest(Config.CONFERENCEINDEX);

        if (type.equals("1")) {
            queryBuilder = QueryBuilders.matchAllQuery();
        }
        if (type.equals("0")){
            queryBuilder = QueryBuilders.wildcardQuery("labels","*"+value+"*");
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
            return date;
        }
        try {
            searchResponseASC = client.search(searchRequestASC, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            client.close();
            return date;
        }
        client.close();
        SearchHit[] searchHitsDESC = searchResponseDESC.getHits().getHits();
        SearchHit[] searchHitsASC = searchResponseASC.getHits().getHits();
        date.add(searchHitsASC[0].getSourceAsMap().get("date"));
        date.add(searchHitsDESC[0].getSourceAsMap().get("date"));
        return date;

    }

    public static List<Conference> getConferenceByFilter(String type, String value, Boolean ifPrepara, String preparaString, int page) throws IOException {
        List<Conference> result = new ArrayList();
        EsUtils esUtils = new EsUtils();
        Map map = new HashMap();
        RestHighLevelClient client = esUtils.getConnection();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(10*(page-1));
        SearchRequest searchRequest = new SearchRequest(Config.CONFERENCEINDEX);
        if (type.equals("1")){
            boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        }else {
            if (type.equals("0")){
                boolQueryBuilder.must(QueryBuilders.wildcardQuery("labels","*"+value+"*"));
            }else {
                return null;
            }
        }

        if (ifPrepara){
            try {
                map = strToMap(preparaString);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            for (Object key : map.keySet()){
                if (key.toString().equals("pubdate")){
                    if (map.get(key).toString().equals("")){
                        continue;
                    }
                    String[] strings = toStringList(map.get(key).toString());
//                    System.out.println(strings);
                    boolQueryBuilder.filter(QueryBuilders.rangeQuery("date").from(strings[0]).to(strings[1]));
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
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            client.close();
            return null;
        }
        client.close();
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit searchHit : searchHits){
            Conference conference = new Conference();
            Map hitMap = searchHit.getSourceAsMap();
            conference.setId(Integer.valueOf(String.valueOf(hitMap.get("id"))));
            conference.setLevel(String.valueOf(hitMap.get("level")));
            conference.setName(String.valueOf(hitMap.get("name")));
            conference.setLocation(String.valueOf(hitMap.get("location")));
            conference.setHomepage(String.valueOf(hitMap.get("homepage")));
            conference.setDate(String.valueOf(hitMap.get("date")));
            conference.setLabels(String.valueOf(hitMap.get("labels")));
            result.add(conference);
        }
        return result;
    }
}
