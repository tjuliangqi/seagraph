package cn.tju.seagraph.utils;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.json.JSONException;
import java.util.Map;
import static cn.tju.seagraph.utils.JsonToMapUtils.strToMap;

public class ToBuildersUtils {

    public static SearchSourceBuilder queryTextToBuilder (String type, String value, boolean ifPrepara, String preparaString, int page) throws JSONException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder builder0;
        QueryBuilder builderAdd;

        if (ifPrepara == false || preparaString.equals("{}")) {
            if (type.equals("0")) {
                builder0 = QueryBuilders.matchQuery("name", value);
                searchSourceBuilder.from(page - 1)
                        .size(20)
                        .query(builder0);
            } else if (type.equals("1")) {
                builder0 = QueryBuilders.matchAllQuery();
                searchSourceBuilder.from(page - 1)
                        .size(20)
                        .query(builder0)
                        .sort("influence", SortOrder.DESC);
            } else {
                builder0 = QueryBuilders.matchQuery("labels", value);
                searchSourceBuilder.from(page - 1)
                        .size(20)
                        .query(builder0)
                        .sort("influence", SortOrder.DESC);
            }
        } else {
            if (type.equals("0")) {
                builder0 = QueryBuilders.matchQuery("name", value);
                builderAdd = addFilterBuilder(builder0, preparaString);
                searchSourceBuilder.from(page - 1)
                        .size(20)
                        .query(builderAdd);
            } else if (type.equals("1")) {
                builder0 = QueryBuilders.matchAllQuery();
                builderAdd = addFilterBuilder(builder0, preparaString);
                searchSourceBuilder.from(page - 1)
                        .size(20)
                        .query(builderAdd)
                        .sort("influence", SortOrder.DESC);
            } else {
                builder0 = QueryBuilders.matchQuery("labels", value);
                builderAdd = addFilterBuilder(builder0, preparaString);
                searchSourceBuilder.from(page - 1)
                        .size(20)
                        .query(builderAdd)
                        .sort("influence", SortOrder.DESC);
            }
        }

        return searchSourceBuilder;
    }

    /**
     * @ description this function add builder0 and filterbuilder
     * @param builder0
     * @param preparaString
     * @return builderAdd
     * @throws JSONException
     */
    public static QueryBuilder addFilterBuilder(QueryBuilder builder0, String preparaString) throws JSONException {
        Map map = strToMap(preparaString);
        try {
            String labels = map.get("labels").toString().replace("[", "").replace("]", "").replace("\"", "");
            String[] labelsToList = labels.split(",");
            QueryBuilder builder1 = QueryBuilders.matchQuery("labels", labelsToList[0]);
            for(int i =1;i<labelsToList.length;i++){
                builder1 = QueryBuilders.boolQuery()
                        .should(builder1)
                        .should(QueryBuilders.matchQuery("labels",labelsToList[i]));
            }
            builder0 = QueryBuilders.boolQuery()
                    .must(builder0)
                    .must(builder1);
        }catch (Exception e){
            System.out.println("No filtration labels");
        }

        try {
            String influence = map.get("influence").toString().replace("[", "").replace("]", "").replace("\"", "");
//            String[] influenceToList = influence.split(",");
//            Float influenceMIN = Float.parseFloat(influenceToList[0].trim());
//            Float influenceMAX = Float.parseFloat(influenceToList[1].trim());
            Double influenceMIN;
            Double influenceMAX;
            if (influence.equals("0-5")){
                influenceMIN = 0.00;
                influenceMAX = 5.00;
            }else if(influence.equals("5-10")){
                influenceMIN = 5.00;
                influenceMAX = 10.00;
            }else {
                influenceMIN = 10.00;
                influenceMAX = Double.POSITIVE_INFINITY;
            }
            QueryBuilder builder2;
            builder2 = QueryBuilders.rangeQuery("influence").from(influenceMIN).to(influenceMAX);

            builder0 = QueryBuilders.boolQuery()
                    .must(builder0)
                    .must(builder2);
        }catch (Exception e){
            System.out.println("No filtration influence");
        }
        return builder0;
    }
    public static void test() throws JSONException {
        String jsonStr = "{example:{'labels':['oocyte recovery','monospecific antiserum','photosynthetic'],influence:['1','1','1']}}";
        String jsonStr2 = "{example:{'labels':['oocyte recovery','monospecific antiserum','photosynthetic']}}";
        Map map = strToMap(jsonStr2);
        System.out.println(map);

        String labels = map.get("labels").toString().replace("[", "").replace("]", "").replace("\"", "");
        String[] strsToList1 = labels.split(",");
        for(int i =1;i<strsToList1.length;i++){
//            System.out.println(strsToList1[i]);
        }
        Object influence = map.get("influence");
        System.out.println(influence);
    }
}
