package cn.tju.seagraph.service;

import cn.tju.seagraph.Config;
import cn.tju.seagraph.daomain.Author;
import cn.tju.seagraph.daomain.AuthorEsBean;
import cn.tju.seagraph.utils.EsUtils;
import cn.tju.seagraph.utils.JsonToMapUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
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

import static cn.tju.seagraph.utils.JsonToMapUtils.strToMap;

public class AuthorSearch {

    public Map<String , Object> authorSearchDetail(List<Author> list) throws IOException, JSONException {
//        System.out.println(id);
//        List<Author> list = authorMapper.getAuthorById(id);
//        AuthorEsBean aEB = new AuthorEsBean();
//        Map<String, List> paperList = new HashMap();
        Map<String, Object> aSD = new HashMap();
        String[] aff = list.get(0).getAffiliations().replace("['","").replace("']","").replace("\\n","").replace("\\\\","").replace("\\","").split("', '");
        Set affS = new HashSet();
        for (int i = 0; i <aff.length ; i++) {
            affS.add(aff[i].trim());
        }
//        aEB.setAffiliations(affS);
        aSD.put("affiliations",new ArrayList<>(affS));

        String[] lab = list.get(0).getLabels().replace("['","").replace("']","").split("', '");
        Set labS = new HashSet();
        for (int i = 0; i <lab.length ; i++) {
            labS.add(lab[i]);
        }
//        aEB.setLabels(labS);
        aSD.put("labels",new ArrayList<>(labS));
        aSD.put("name",list.get(0).getName());
        aSD.put("pic_url",list.get(0).getPic_url());
        aSD.put("paperNum",list.get(0).getPaperNum());
        Map paperList = new HashMap();
        paperList = JsonToMapUtils.strToMap(list.get(0).getPaperList());
        for (Object each:paperList.keySet()) {
            paperList.put(each.toString(),Arrays.asList(paperList.get(each).toString().replace("[","").replace("]","").replace("\"","").split(",")));
        }
        aSD.put("paperList",paperList);

        Map paperUUID = new HashMap();
        paperUUID = JsonToMapUtils.strToMap(list.get(0).getPaperUUID());
        for (Object each:paperUUID.keySet()) {
            paperUUID.put(each.toString(),Arrays.asList(paperUUID.get(each).toString().replace("[","").replace("]","").replace("\"","").split(",")));
        }
        aSD.put("paperUUID",paperUUID);

//        aEB.setName(list.get(0).getName());
//        aEB.setPic_url(list.get(0).getPic_url());
//        aEB.setPaperList(list.get(0).getPaperList());
//        aEB.setPaperUUID(list.get(0).getPaperUUID());
//        aEB.setPaperNum(list.get(0).getPaperNum());
        return aSD;

    }

    public Map<String,Object> authorSearchList(String type, String value, String page, Boolean ifPrepara, String preparaString)  {

        List<Map> resultList = new ArrayList<Map>();
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.client;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder match = QueryBuilders.boolQuery();
        Map map = new HashMap();

        int p = Integer.valueOf(page);
        if (type.equals("0")) {
            match.must(QueryBuilders.matchQuery("name",value));
        }else if(type.equals("2")){
            match.must(QueryBuilders.matchQuery("labels",value));
        }else {
            match.must(QueryBuilders.matchAllQuery());
        }
//        searchSourceBuilder.query(match);
//        searchSourceBuilder.size(100);

        if (ifPrepara){
            try {
                map = strToMap(preparaString);
            } catch (JSONException e) {
                e.printStackTrace();
//                return RetResponse.makeErrRsp("json解析错误");
            }
            for (Object key : map.keySet()){
                if (key.toString().equals("labels")){
                    if (map.get(key).toString().equals("")){
                        continue;
                    }
                    String[] strings = map.get(key).toString().replace("[","").replace("]","").replace("\"","").split(",");
                    for (String each :strings){
                        match.filter(QueryBuilders.matchQuery("labels",each.trim()));
                    }
                }
            }
        }



        searchSourceBuilder.from(p-1).size(20).query(match).sort("paperNum", SortOrder.DESC);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(Config.AUTHORINDEX);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SearchHit[] searchHits = searchResponse.getHits().getHits();


        JsonToMapUtils j = new JsonToMapUtils();
        for (SearchHit searchHit:searchHits){
            Map authorListResult = new HashMap();
//            AuthorEsBean result = new AuthorEsBean();
            Map<String,Object> result = new HashMap();
            try {
                authorListResult = strToMap(searchHit.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String[] aff = authorListResult.get("affiliations").toString().replace("[","").replace("]","").replace("\"","").replace("\\n","").replace("\\\\","").replace("\\","").split(",");
            Set affS = new HashSet();
            for (int i = 0; i <aff.length ; i++) {
                affS.add(aff[i].trim());
            }
            String[] lab = authorListResult.get("labels").toString().replace("[","").replace("]","").replace("\"","").split(",");
            Set labS = new HashSet();
            for (int i = 0; i <lab.length ; i++) {
                labS.add(lab[i]);
            }

            Map paperList = new HashMap();
            try {
                paperList = JsonToMapUtils.strToMap(authorListResult.get("paperList").toString());
                for (Object each:paperList.keySet()) {
                    paperList.put(each.toString(),Arrays.asList(paperList.get(each).toString().replace("[","").replace("]","").replace("\"","").split(",")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            result.put("name",authorListResult.get("name").toString());
            result.put("pic_url",authorListResult.get("pic_url").toString());
            result.put("paperNum",authorListResult.get("paperNum"));
            result.put("affiliations",new ArrayList<>(affS));
            result.put("labels",new ArrayList<>(labS));
            result.put("paperList",paperList);
            result.put("id",authorListResult.get("uuid").toString());
//            result.setName(authorListResult.get("name").toString());
//            result.setAffiliations(affS);
//            result.setLabels(labS);
//            result.setPic_url(authorListResult.get("pic_url").toString());
//            result.setPaperList(authorListResult.get("paperList").toString());
//            result.setPaperNum((int)authorListResult.get("paperNum"));
//            result.setId(authorListResult.get("uuid").toString());
            resultList.add(result);
        }

        long count = searchResponse.getHits().getTotalHits();
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("result",resultList);
        resultMap.put("count",count);

        return resultMap;
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
        }else if(type.equals("2")){
            match = QueryBuilders.matchQuery("labels",value);
        }else {
            match = QueryBuilders.matchAllQuery();
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
            authorPreResult = strToMap(searchHit.toString());
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

    public static List<String> relationAuthors(String name) throws IOException {
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.client;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder match = QueryBuilders.matchQuery("authors",name);
        searchSourceBuilder.query(match);
        searchSourceBuilder.size(100);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(Config.PAPERINDEX);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        Set<String> result = new HashSet<>();
        for (SearchHit searchHit:searchHits){
            Map<String,Object> map = searchHit.getSourceAsMap();
            System.out.println(map.get("authors"));
            String[] strs = String.valueOf(map.get("authors")).replace("\"","").replace("[","").replace("}","").split(",");
            for (String str : strs){
                result.add(str);
            }
        }
        List final_result = new ArrayList(result);
        return final_result;

    }

}
