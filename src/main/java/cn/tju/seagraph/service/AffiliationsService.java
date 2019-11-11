package cn.tju.seagraph.service;

import cn.tju.seagraph.Config;
import cn.tju.seagraph.daomain.AffiliationsEsBean;
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
    public static Map<String,Object> affiliationSearchList(String type, String value, boolean ifPrepara, String preparaString, int page) throws IOException, JSONException {
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.client;
        ArrayList<AffiliationsEsBean> affiliationsEsBeans = new ArrayList<>();
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
//            System.out.println(searchHit.getSourceAsString());
            AffiliationsEsBean affiliationsEsBean = new AffiliationsEsBean();
            String uuid = (String)searchHit.getSourceAsMap().get("uuid");
            String name = (String)searchHit.getSourceAsMap().get("name");
            name = name.replace("\\n","");
            String labels = (String)searchHit.getSourceAsMap().get("labels");
            String influence = (String)searchHit.getSourceAsMap().get("influence");
            Integer paperNum = (Integer) searchHit.getSourceAsMap().get("paperNum");
            String[] labelsArray = labels.replace("['","").replace("']","").split("', '");
            Set<String> labelsSet = new HashSet<>(Arrays.asList(labelsArray));

            affiliationsEsBean.setUuid(uuid);
            affiliationsEsBean.setName(name);
            affiliationsEsBean.setLabels(labelsSet);
            affiliationsEsBean.setInfluence(influence);
            affiliationsEsBean.setPaperNum(paperNum);
            affiliationsEsBeans.add(affiliationsEsBean);
        }
        long count = searchResponse.getHits().getTotalHits();
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("result",affiliationsEsBeans);
        resultMap.put("count",count);
        return resultMap;
    }

    /**
     *
     * @param type
     * @param value
     * @return
     * @throws IOException
     */
    public static Map<String, Set> affiliationPrepara(String type, String value) throws IOException {
        String queryField = "name";
        QueryBuilder builder = QueryBuilders.matchAllQuery();
        if (type.equals("0")){
            queryField = "name";
            builder = QueryBuilders.matchQuery(queryField,value);
        }else if(type.equals("1")){
            builder = QueryBuilders.matchAllQuery();
        }else if(type.equals("2")){
            queryField = "labels";
            builder = QueryBuilders.matchQuery(queryField,value);
        }
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.client;

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(0)
                .size(10)
                .query(builder);
        SearchRequest searchRequest = new SearchRequest(Config.INDEX_AFFILIATIONS);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHit[] searchHits = searchResponse.getHits().getHits();

        ArrayList<String> labelsList = new ArrayList<>();
        ArrayList<String> influenceList = new ArrayList<>();
        Map<String,Set> selectTags = new HashMap<>();
        AffiliationsEsBean affiliationsEsBean = new AffiliationsEsBean();
        for (SearchHit searchHit:searchHits){
//            System.out.println(searchHit.getSourceAsString());

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
        selectTags.put("labels",result1);
        selectTags.put("influence",result2);

        return selectTags;
    }

    public static Map<String, Object> mySqlBeanToEsBean(AffiliationsMysqlBean affiliationsMysqlBean){
        Map<String, Object> Affiliations = new HashMap();
        String name = affiliationsMysqlBean.getName().replace("\\n","");
        Affiliations.put("uuid",affiliationsMysqlBean.getUuid());
        Affiliations.put("name",name);
        String paperListStr = affiliationsMysqlBean.getPaperList();
        Map<String, Object> paperListMap = new HashMap();
        String[] alist = paperListStr.replace("{","").replace("]}","").split("],");
        for (String each:alist){
            String eachName = each.split(":")[0].replace("'","").trim();
            String eachValue = each.split(":")[1].replace("['","").replace("'","").trim();
            String[] eachList = eachValue.split(",");
            paperListMap.put(eachName,eachList);
        }
        Affiliations.put("paperList",paperListMap);
        Affiliations.put("influence",affiliationsMysqlBean.getInfluence());
        String paperUUIDStr = affiliationsMysqlBean.getPaperUUID();
        Map<String, Object> paperUUIDMap = new HashMap();
        String[] blist = paperUUIDStr.replace("{","").replace("]}","").split("],");
        for (String each:blist){
            String eachName = each.split(":")[0].replace("'","").trim();
            String eachValue = each.split(":")[1].replace("['","").replace("'","").trim();
            String[] eachList = eachValue.split(",");
            paperUUIDMap.put(eachName,eachList);
        }
        Affiliations.put("paperUUID",paperUUIDMap);

        String labelsText = affiliationsMysqlBean.getLabels();
        String[] labelsArray = labelsText.replace("['","").replace("']","").split("', '");
        Set<String> labelsSet = new HashSet<>(Arrays.asList(labelsArray));
        Affiliations.put("labels",labelsSet);

        int pageNum = Integer.parseInt(affiliationsMysqlBean.getPaperNum());
        Affiliations.put("paperNum",pageNum);

        return Affiliations;
    }

}
