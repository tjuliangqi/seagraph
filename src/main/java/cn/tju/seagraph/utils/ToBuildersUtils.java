package cn.tju.seagraph.utils;
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
        String queryField0 = "paperList";
        if (type.equals("0")){
            queryField0 = "paperList";
        }
        QueryBuilder builder0;
        if (ifPrepara==false) {
            builder0 = QueryBuilders.matchQuery(queryField0, value);
        }
        else {
            builder0 = QueryBuilders.matchQuery(queryField0, value);
            Map map = strToMap(preparaString);
            String labels = map.get("labels").toString().replace("[", "").replace("]", "").replace("\"", "");
            String[] labelsToList = labels.split(",");
            QueryBuilder builder1 = QueryBuilders.matchQuery("labels", labelsToList[0]);
            for(int i =1;i<labelsToList.length;i++){
                builder1 = QueryBuilders.boolQuery()
                        .should(builder1)
                        .should(QueryBuilders.matchQuery("labels",labelsToList[i]));
            }

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
                    .must(builder1)
                    .must(builder2);
        }
        System.out.println(builder0);
        return builder0;
    }

    public static void test() throws JSONException {
        String jsonStr = "{example:{'labels':['oocyte recovery','monospecific antiserum','photosynthetic'],influence:['1','1','1']}}";
        Map map = strToMap(jsonStr);
        System.out.println(map);

        String labels = map.get("labels").toString().replace("[", "").replace("]", "").replace("\"", "");
        String[] strsToList1 = labels.split(",");
        for(int i =1;i<strsToList1.length;i++){
            System.out.println(strsToList1[i]);
        }
        String influence = map.get("influence").toString();
        System.out.println(map.get("labels"));
    }
}
