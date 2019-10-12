package cn.tju.seagraph.server;

import cn.tju.seagraph.Config;
import cn.tju.seagraph.utils.EsUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;

public class EsSerch {

    public void TestSearch() throws IOException {
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.client;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder match = QueryBuilders.matchQuery("title","Chromosomal region of the cystic fibrosis gene in yeast artificial chromosomes: a model for human genome mapping");
        searchSourceBuilder.query(match);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(Config.INDEX);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit searchHit:searchHits){
            System.out.println(searchHit.toString());
        }

    }

    public static void main(String[] args) throws IOException {
        EsSerch esSerch = new EsSerch();
        esSerch.TestSearch();
    }

}
