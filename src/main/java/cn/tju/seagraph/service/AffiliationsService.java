package cn.tju.seagraph.service;

import cn.tju.seagraph.Config;
import cn.tju.seagraph.daomain.AffiliationsMysqlBean;
import cn.tju.seagraph.utils.EsUtils;
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

import static cn.tju.seagraph.utils.ToBuildersUtils.queryTextToBuilder;

public class AffiliationsService {
    /**
     *
     * @param type
     * @param value
     * @param ifPrepara
     * @param preparaString
     * @param page
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static ArrayList<AffiliationsMysqlBean> affiliationSearchList(String type, String value, boolean ifPrepara, String preparaString, int page) throws IOException, JSONException {
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.client;
        ArrayList<AffiliationsMysqlBean> affiliationsMysqlBeans = new ArrayList<>();
        QueryBuilder builder = queryTextToBuilder(type, value, ifPrepara, preparaString);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(page)
                .size(10)
                .query(builder);
        SearchRequest searchRequest = new SearchRequest(Config.INDEX_AFFILIATIONS);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHit[] searchHits = searchResponse.getHits().getHits();

        for (SearchHit searchHit:searchHits){
            System.out.println(searchHit.getSourceAsString());
            AffiliationsMysqlBean affiliationsMysqlBean = new AffiliationsMysqlBean();
            String uuid = (String)searchHit.getSourceAsMap().get("uuid");
            String name = (String)searchHit.getSourceAsMap().get("name");
            String labels = (String)searchHit.getSourceAsMap().get("labels");
            String influence = (String)searchHit.getSourceAsMap().get("influence");

            affiliationsMysqlBean.setUuid(uuid);
            affiliationsMysqlBean.setName(name);
            affiliationsMysqlBean.setLabels(labels);
            affiliationsMysqlBean.setInfluence(influence);
            affiliationsMysqlBeans.add(affiliationsMysqlBean);
        }
        return affiliationsMysqlBeans;
    }

    /**
     *
     * @param type
     * @param value
     * @param page
     * @return
     * @throws IOException
     */
    public static AffiliationsMysqlBean affiliationPrepara(String type, String value, int page) throws IOException {
        String queryField = "paperList";
        if (type.equals("0")){
            queryField = "paperList";
        }
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.client;
        QueryBuilder builder = QueryBuilders.matchQuery(queryField,value);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(page)
                .size(10)
                .query(builder);
        SearchRequest searchRequest = new SearchRequest(Config.INDEX_AFFILIATIONS);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHit[] searchHits = searchResponse.getHits().getHits();

        ArrayList<String> labelsList = new ArrayList<>();
        ArrayList<String> influenceList = new ArrayList<>();
        AffiliationsMysqlBean affiliationsMysqlBean = new AffiliationsMysqlBean();
        for (SearchHit searchHit:searchHits){
            System.out.println(searchHit.getSourceAsString());


            String labels = (String)searchHit.getSourceAsMap().get("labels");
            String influence = (String)searchHit.getSourceAsMap().get("influence");
            labels = labels.replace("['", "").replace("']", "");
            influence = influence.replace("['", "").replace("']", "");
            String[] eachLabels = labels.split("', '");
            List<String> eachLabelsList= new ArrayList<>(Arrays.asList(eachLabels));
            labelsList.addAll(eachLabelsList);

            String[] eachInfluence = influence.split("', '");
            List<String> eachInfluenceList= new ArrayList<>(Arrays.asList(eachInfluence));
            influenceList.addAll(eachInfluenceList);
        }
        Set result1 = new HashSet(labelsList);
        Set result2 = new HashSet(influenceList);
        affiliationsMysqlBean.setLabels(result1.toString());
        affiliationsMysqlBean.setInfluence(result2.toString());
        return affiliationsMysqlBean;
    }

}
