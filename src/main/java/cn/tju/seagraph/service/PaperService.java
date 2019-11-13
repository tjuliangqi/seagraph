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
import java.lang.reflect.Type;
import java.util.*;

import static cn.tju.seagraph.utils.JsonToMapUtils.strToMap;

public class PaperService {

    public static String[] toStringList(String listString){
        String[] strings = listString.replace("[","").replace("]","").replace("\"","").split(",");
        return strings;
    }

    public static String[] toStringListMysql(String listString){
        String[] strings = listString.replace("['","").replace("']","").replace("\"","").split("', '");
        return strings;
    }

    public static String[] splitAffiliations(String affiliations){
        String[] strings = affiliations.replace("['","").replace("']","").replace("\\n","").replace("\\","").split("', '");
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
        SearchRequest searchRequestDESC = new SearchRequest(Config.PAPERINDEX);
        SearchRequest searchRequestASC = new SearchRequest(Config.PAPERINDEX);

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
//        System.out.println("正序：" + searchHitsASC.length);
//        System.out.println("反序：" + searchHitsDESC.length);
        pubdate.add(searchHitsASC[0].getSourceAsMap().get("pubdate"));
        pubdate.add(searchHitsDESC[0].getSourceAsMap().get("pubdate"));
        return pubdate;

    }

    public static Map<String,Object> mysqlMapToESMap(Map<String,Object> map){
        Map<String,Object> result = new HashMap<>();
        result.put("uuid",map.get("uuid"));
        result.put("authors",map.get("authors"));
        result.put("affiliations",map.get("affiliations"));
        result.put("title",map.get("title"));
        result.put("journal",map.get("journal"));
        result.put("abs",map.get("abs"));
        result.put("pubdate",map.get("pubdate"));
        result.put("type",map.get("type"));
        result.put("browse",map.get("browse"));
        result.put("keywords",map.get("keywords"));
        result.put("chemicallist",map.get("chemicallist"));
        result.put("labels",map.get("labels"));
        result.put("ch_title",map.get("ch_title"));
        result.put("or_title",map.get("or_title"));

        return result;
    }

    public static Map<String,Object> formatDetail(PaperMysqlBean paperMysqlBean){
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("uuid",paperMysqlBean.getUuid());
        List<String> authors = new ArrayList<>();
        List<String> references = new ArrayList<>();
        List<String> chemicallist = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        String[] strAffs ;
        List<List<String>> affiliations = new ArrayList<>();
        if (paperMysqlBean.getAuthors() != null){
            authors = Arrays.asList(toStringListMysql(paperMysqlBean.getAuthors()));
        }

        resultMap.put("authors",authors);

        if (paperMysqlBean.getAffiliations() != null){
            strAffs = splitAffiliations(paperMysqlBean.getAffiliations());
            for (String strAff : strAffs){
                affiliations.add(Arrays.asList(strAff.trim().split(";")));
            }
        }

        resultMap.put("Affiliations",affiliations);
        resultMap.put("doi",paperMysqlBean.getDoi());
        resultMap.put("title",paperMysqlBean.getTitle());
        resultMap.put("Journal",paperMysqlBean.getJournal());
        resultMap.put("abs",paperMysqlBean.getAbs().replace("\n","<br /><br />&nbsp;&nbsp;"));
        resultMap.put("fulltext",paperMysqlBean.getFulltext().replace("\n","<br /><br />&nbsp;&nbsp;"));
        System.out.println("************\n"+resultMap.get("fulltext"));
        if (paperMysqlBean.getReferences() != null){
            references = Arrays.asList(toStringListMysql(paperMysqlBean.getReferences()));
        }

        resultMap.put("references",references);
        resultMap.put("Pubdate",paperMysqlBean.getPubdate());
        resultMap.put("pdf_url",paperMysqlBean.getPdf_url());
        resultMap.put("type",paperMysqlBean.getType());
        resultMap.put("pic_url",paperMysqlBean.getPic_url());
        resultMap.put("pic_text",paperMysqlBean.getPic_text());
        //这里放的空list
        resultMap.put("keywords",new ArrayList<>());
        resultMap.put("fulltext_url",paperMysqlBean.getFulltext_url());
        resultMap.put("download",paperMysqlBean.getDownload());
        resultMap.put("ch_title",paperMysqlBean.getCh_title());
        //这里放的空list
        resultMap.put("re_uuid",new ArrayList<>());
        resultMap.put("or_title",paperMysqlBean.getOr_title());
        resultMap.put("browse",paperMysqlBean.getBrowse());

        if (paperMysqlBean.getChemicallist() != null){
            chemicallist = Arrays.asList(toStringListMysql(paperMysqlBean.getChemicallist()));
        }

        resultMap.put("chemicallist",chemicallist);

        if (paperMysqlBean.getLabels() != null){
            labels = Arrays.asList(toStringListMysql(paperMysqlBean.getLabels()));
        }

        resultMap.put("Labels",labels);

        return resultMap;

    }

    public static PaperEsBean mysqlToES(PaperMysqlBean paperMysqlBean){
        PaperEsBean paperEsBean = new PaperEsBean();
        paperEsBean.setUuid(paperMysqlBean.getUuid());
        paperEsBean.setAuthors(paperMysqlBean.getAuthors());
        paperEsBean.setAffiliations(paperMysqlBean.getAffiliations());
        paperEsBean.setTitle(paperMysqlBean.getTitle());
        paperEsBean.setJournal(paperMysqlBean.getJournal());
        paperEsBean.setAbs(paperMysqlBean.getAbs());
        paperEsBean.setPubdate(paperMysqlBean.getPubdate());
        paperEsBean.setType(paperMysqlBean.getType());
        paperEsBean.setBrowse(paperMysqlBean.getBrowse());
        paperEsBean.setKeywords(paperMysqlBean.getKeywords());
        paperEsBean.setChemicallist(paperMysqlBean.getChemicallist());
        paperEsBean.setLabels(paperMysqlBean.getLabels());
        paperEsBean.setOr_title(paperMysqlBean.getOr_title());
        paperEsBean.setCh_title(paperMysqlBean.getCh_title());

        return paperEsBean;

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

    public static Map<String,Object> hitToMap(SearchHit searchHit){
        Map<String,Object> resultMap = new HashMap<>();
        Map hitMap = searchHit.getSourceAsMap();
        resultMap.put("uuid",searchHit.getId().split("-")[1]);
        List<String> authors = new ArrayList<>();
        List<String> chemicallist = new ArrayList<>();
        List<List<String>> affiliations = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        String[] strAffs;
        if (hitMap.get("author") != null){
            authors = Arrays.asList(toStringListMysql(hitMap.get("author").toString()));
        }

        resultMap.put("authors",authors);

        if (hitMap.get("affiliations") != null){
            strAffs = splitAffiliations(hitMap.get("affiliations").toString());
            for (String strAff : strAffs){
                affiliations.add(Arrays.asList(strAff.trim().split(";")));
            }
        }

        resultMap.put("affiliations",affiliations);
        resultMap.put("title",hitMap.get("title"));
        resultMap.put("journal",hitMap.get("journal"));
        resultMap.put("abs",hitMap.get("abs"));
        resultMap.put("pubdate",hitMap.get("pubdate"));
        resultMap.put("type",hitMap.get("type"));
        resultMap.put("browse",hitMap.get("browse"));
        //这里输入空的list
        resultMap.put("keywords",new ArrayList<>());

        if (hitMap.get("chemicallist") != null){
            chemicallist = Arrays.asList(toStringListMysql(hitMap.get("chemicallist").toString()));
        }

        resultMap.put("chemicallist",chemicallist);

        if ((hitMap.get("labels") != null)){
            labels = Arrays.asList(toStringListMysql(hitMap.get("labels").toString()));
        }

        resultMap.put("labels",labels);
        resultMap.put("or_title",hitMap.get("or_title"));
        resultMap.put("ch_title",hitMap.get("ch_title"));

        return resultMap;
    }

    public static RetResult<Map<String,Object>> searchList(String type, String value, Boolean ifPrepara, String preparaString, int page) throws IOException {
        List result = new ArrayList();
        Map map = new HashMap();
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.getConnection();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(page-1).size(20);
        SearchRequest searchRequest = new SearchRequest(Config.PAPERINDEX);
        if (type.equals("0")){
            boolQueryBuilder.must(QueryBuilders.matchQuery("title",value));
        }
        else if (type.equals("1")){
            boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        }
        else if (type.equals("2")){
            boolQueryBuilder.must(QueryBuilders.matchQuery("labels",value));
        }

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
        long count = searchResponse.getHits().getTotalHits();
        for (SearchHit searchHit : searchHits){
            result.add(hitToMap(searchHit));
        }
        System.out.println(value);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("result",result);
        resultMap.put("count",count);
        System.out.println(resultMap);
        return RetResponse.makeOKRsp(resultMap);
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

        else if (type.equals("2")){
            queryBuilder = QueryBuilders.matchQuery("labels", value);
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SearchRequest searchRequest = new SearchRequest(Config.PAPERINDEX);
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
//        System.out.println(filterBean.toString());

        return RetResponse.makeOKRsp(filterBean);

    }

    public static void main(String[] args) throws JSONException {
        String a = "{'nephrogenic': ['77a3d264ea3611e9815800d861171bd5'], 'peptide': ['77a3d264ea3611e9815800d861171bd5'], 'photosynthetic': ['77a3d264ea3611e9815800d861171bd5']}";
        Map map = strToMap(a);
        System.out.println(map);
        System.out.println(map.get("peptide"));
    }
}
