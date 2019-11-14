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
        SearchSourceBuilder searchSourceBuilder;
        searchSourceBuilder = queryTextToBuilder(type, value, ifPrepara, preparaString, page);

        SearchRequest searchRequest = new SearchRequest(Config.INDEX_AFFILIATIONS);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        Map<String,Object> resultMap = new HashMap<>();
        if (searchHits.length < 1){
            resultMap = resultMap;
        }else {
            for (SearchHit searchHit:searchHits){
//            System.out.println(searchHit.getSourceAsString());
                AffiliationsEsBean affiliationsEsBean = new AffiliationsEsBean();
                String uuid = (String)searchHit.getSourceAsMap().get("uuid");
                String name = (String)searchHit.getSourceAsMap().get("name");
                name = name.replace("\\n","");
                String labels = (String)searchHit.getSourceAsMap().get("labels");
                Double influenceNum = (Double)searchHit.getSourceAsMap().get("influence");
                String influence = influenceNum.toString();
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

            resultMap.put("result",affiliationsEsBeans);
            resultMap.put("count",count);
        }
        return resultMap;
    }

    /**
     *
     * @param type type只有 0 和 1 两种
     * @param value
     * @return
     * @throws IOException
     */
    public static Map<String, Object> affiliationPrepara(String type, String value) throws IOException {
        QueryBuilder builder;
        if (type.equals("0")){
            builder = QueryBuilders.matchQuery("name",value);
        }else {
            builder = QueryBuilders.matchAllQuery();// type = 1
        }
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.client;

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(0)
                .size(100)
                .query(builder);

        SearchRequest searchRequest = new SearchRequest(Config.INDEX_AFFILIATIONS);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHit[] searchHits = searchResponse.getHits().getHits();

        ArrayList<String> labelsList = new ArrayList<>();
        ArrayList<Double> influenceList = new ArrayList<>();
        Map<String,Object> selectTags = new HashMap<>();
        if (searchHits.length < 2){
            selectTags = selectTags;
        }else {
            for (SearchHit searchHit:searchHits){
                //System.out.println(searchHit.getSourceAsString());
                // find all labels
                String labels = (String)searchHit.getSourceAsMap().get("labels");
                labels = labels.replace("['", "").replace("']", "");
                String[] eachLabels = labels.split("', '");
                List<String> eachLabelsList= new ArrayList<>(Arrays.asList(eachLabels));
                labelsList.addAll(eachLabelsList);
                // find all influence
                Double influenceNum = (Double)searchHit.getSourceAsMap().get("influence");
                influenceList.add(influenceNum);
            }

            Set result1 = new HashSet(labelsList);
            ArrayList<Double> result2 = new ArrayList<>();
            result2.add(Collections.min(influenceList));
            result2.add(Collections.max(influenceList));
            selectTags.put("labels",result1);
            selectTags.put("influence",result2);
        }

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
            String[] eachList = eachValue.split(", ");
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
            String[] eachList = eachValue.split(", ");
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
