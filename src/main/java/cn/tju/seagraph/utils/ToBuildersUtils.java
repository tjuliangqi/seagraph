package cn.tju.seagraph.utils;
import org.elasticsearch.common.util.concurrent.EsRejectedExecutionException;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.json.JSONException;

import java.util.Map;

import static cn.tju.seagraph.utils.JsonToMapUtils.strToMap;

public class ToBuildersUtils {
    /**
     *
     * @param type
     * @param value
     * @param ifPrepara
     * @param preparaString
     * @return
     * @throws JSONException
     */
    public static QueryBuilder queryTextToBuilder (String type, String value, boolean ifPrepara, String preparaString) throws JSONException {
        String queryField0 = "name";
        QueryBuilder builder0 = QueryBuilders.matchAllQuery();

        if (ifPrepara==false || preparaString.equals("{}")) {
            if (type.equals("0")){
                queryField0 = "name";
                builder0 = QueryBuilders.matchQuery(queryField0, value);
            }else if(type.equals("1")){
                builder0 = QueryBuilders.matchAllQuery();
            }else if(type.equals("2")){
                queryField0 = "labels";
                builder0 = QueryBuilders.matchQuery(queryField0, value);
            }
        }
        else {
            if (type.equals("0")){
                queryField0 = "name";
                builder0 = QueryBuilders.matchQuery(queryField0, value);
            }else if(type.equals("1")){
                builder0 = QueryBuilders.matchAllQuery();
            }else if(type.equals("2")){
                queryField0 = "labels";
                builder0 = QueryBuilders.matchQuery(queryField0, value);
            }
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
                String[] influenceToList = influence.split(",");
                QueryBuilder builder2 = QueryBuilders.matchQuery("influence", influenceToList[0]);
                for(int j =1;j<influenceToList.length;j++){
                    builder2 = QueryBuilders.boolQuery()
                            .should(builder2)
                            .should(QueryBuilders.matchQuery("influence",influenceToList[j]));
                }
                builder0 = QueryBuilders.boolQuery()
                        .must(builder0)
                        .must(builder2);
            }catch (Exception e){
                System.out.println("No filtration influence");
            }

        }
//        System.out.println(builder0);
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
