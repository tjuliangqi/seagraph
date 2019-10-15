package cn.tju.seagraph.server;

import cn.tju.seagraph.Config;
import cn.tju.seagraph.daomain.RetResponse;
import cn.tju.seagraph.daomain.RetResult;
import cn.tju.seagraph.utils.EsUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.tju.seagraph.utils.JsonToMapUtils.strToMap;

public class EsSerch {

    public void testSearch() throws IOException {
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.getConnection();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("title","The Complete 685-Kilobase DNA Sequence of the Human β T Cell Receptor Locus"))
                .filter(QueryBuilders.rangeQuery("pubdate").from("1995-01-01T00:00:00").to("2019-01-01T00:00:00"))
                .filter(QueryBuilders.matchQuery("journal","Science"))
                .filter(QueryBuilders.matchQuery("affiliations","L. Rowen and L. Hood are in the Department of Molecular Biotechnology, University of Washington, Seattle, WA 98195-7730, USA."));
        System.out.println(queryBuilder.toString());
        QueryBuilder match = QueryBuilders.matchQuery("title","Chromosomal region of the cystic fibrosis gene in yeast artificial chromosomes: a model for human genome mapping");
        searchSourceBuilder.query(queryBuilder);
        SearchRequest searchRequest = new SearchRequest(Config.INDEX);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        System.out.println(searchResponse.getHits().getTotalHits());
        System.out.println("************");
        for (SearchHit searchHit:searchHits){
            System.out.println(searchHit.toString());
        }
        client.close();

    }

    public static String[] toStringList(String listString){
        String [] strings = listString.replace("[","").replace("]","").replace("\"","").split(",");
        return strings;
    }

    public static Map toDataMap(SearchHit searchHit){
        Map map = new HashMap();
        Map hitMap = searchHit.getSourceAsMap();
        map.put("uuid", searchHit.getId().split("-")[1]);
        map.put("author",hitMap.get("authors"));
        map.put("affiliations",hitMap.get("affiliations"));
        map.put("title",hitMap.get("title"));
        map.put("journal",hitMap.get("journal"));
        map.put("abs",hitMap.get("abs"));
        map.put("pubdate",hitMap.get("pubdate"));
        return map;
    }


    public RetResult<List> searchList(String type, String value, Boolean ifPrepara, String preparaString) throws IOException {
        List result = new ArrayList();
        Map map;
        EsUtils esUtils = new EsUtils();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RestHighLevelClient client = esUtils.getConnection();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SearchRequest searchRequest = new SearchRequest(Config.INDEX);

        if(type.equals("0")){
            queryBuilder.must(QueryBuilders.matchQuery("title",value));
            if(ifPrepara){
                try {
                    map = strToMap(preparaString);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return RetResponse.makeErrRsp("json解析失败");
                }
                for (Object key : map.keySet()){
                    if(key.toString().equals("journal")){
                        String [] strings = toStringList(map.get(key).toString());
                        for (String each : strings){
                            queryBuilder.filter(QueryBuilders.matchQuery("journal",each));
                        }
                    }
                    if(key.toString().equals("pubdate")){
                        String [] strings = toStringList(map.get(key).toString());
                        queryBuilder.filter(QueryBuilders.rangeQuery("pubdate").from(strings[0]).to(strings[1]));
                    }
                    if(key.toString().equals("affiliations")){
                        String [] strings = toStringList(map.get(key).toString());
                        for (String each : strings){
                            queryBuilder.filter(QueryBuilders.matchQuery("affiliations",each));
                        }

                    }
                    if(key.toString().equals("labels")){
                        String [] strings = toStringList(map.get(key).toString());
                        for (String each : strings){
                            queryBuilder.filter(QueryBuilders.matchQuery("labels",each));
                        }
                    }
                }

            }

        }
        searchSourceBuilder.query(queryBuilder);
        searchRequest.source(searchSourceBuilder);
        System.out.println(queryBuilder);
        SearchResponse searchResponse;
        try {
            searchResponse = client.search(searchRequest);
        } catch (IOException e) {
            e.printStackTrace();
            client.close();
            return RetResponse.makeErrRsp("es查询失败");
        }

        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit searchHit:searchHits){
            result.add(toDataMap(searchHit));
        }
        client.close();
        return RetResponse.makeRsp(20000,"ok",result);
    }

    public RetResult<List> prepara(String type, String value) throws IOException {
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.getConnection();
        QueryBuilder queryBuilder = null;
        if(type.equals("0")){
            queryBuilder = QueryBuilders.matchQuery("title",value);
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SearchRequest searchRequest = new SearchRequest(Config.INDEX);
        searchSourceBuilder.query(queryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;

        try {
            searchResponse = client.search(searchRequest);
        } catch (IOException e) {
            e.printStackTrace();
            client.close();
            return RetResponse.makeErrRsp("es查询失败");
        }

        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit searchHit:searchHits){
            searchHit.getSourceAsMap();
        }

        return null;
    }

    public static void main(String[] args) throws IOException {
        EsSerch esSerch = new EsSerch();
        String test = "{journal:['science'],pubdate:['1995-01-01T00:00:00','2019-01-01T00:00:00'],affiliations:['L. Rowen and L. Hood are in the Department of Molecular Biotechnology, University of Washington, Seattle, WA 98195-7730, USA.'],labels:['detect', 'congenital']}";
        esSerch.searchList("0","The Complete 685-Kilobase DNA Sequence of the Human β T Cell Receptor Locus",Boolean.TRUE,test);

    }

}
