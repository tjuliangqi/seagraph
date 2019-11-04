package cn.tju.seagraph.controller;


import cn.tju.seagraph.dao.PaperMapper;
import cn.tju.seagraph.daomain.FilterBean;
import cn.tju.seagraph.daomain.PaperMysqlBean;
import cn.tju.seagraph.daomain.RetResponse;
import cn.tju.seagraph.daomain.RetResult;
import org.apache.lucene.analysis.miscellaneous.StemmerOverrideFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static cn.tju.seagraph.service.PaperService.prepara;
import static cn.tju.seagraph.service.PaperService.searchList;

@RestController
@RequestMapping("/paper")
public class PaperController {
    @Autowired
    PaperMapper paperMapper;

    @RequestMapping(value = "/searchList",method = RequestMethod.POST)
    public RetResult<List> searchListResponse(@RequestBody Map<String,String> map) throws IOException {
//        System.out.println(map);
        String type = String.valueOf(map.get("type"));
        String value = String.valueOf(map.get("value"));
        Boolean ifPrepara = Boolean.valueOf(map.get("ifPrepara"));
        String preparaString = String.valueOf(map.get("preparaString"));
        int page = Integer.valueOf(map.get("page"));
        RetResult<List> result = searchList(type,value,ifPrepara,preparaString,page);
        return result;
    }

    @RequestMapping(value = "/prepara",method = RequestMethod.POST)
    public RetResult<FilterBean> preparaResponse(@RequestBody Map<String,String> map) throws IOException {
//        System.out.println(map);
        String type = map.get("type");
        String value = map.get("value");
        RetResult<FilterBean> result = prepara(type,value);
        return result;
    }

    @RequestMapping(value = "/detail",method = RequestMethod.POST)
    public RetResult<PaperMysqlBean> detailResponse(@RequestBody Map<String,String> map){
//        System.out.println(map);
        int browse;
        PaperMysqlBean result = paperMapper.getDataById(map.get("uuid"));
        PaperMysqlBean paperMysqlBean = result;
//        System.out.println(result);
//        System.out.println("**********");
//        System.out.println(paperMysqlBean);
        if (paperMysqlBean.getBrowse() != null){
            browse = Integer.valueOf(paperMysqlBean.getBrowse())+1;
        }
        else {
            browse = 1;
        }
        paperMysqlBean.setBrowse(String.valueOf(browse));
        paperMapper.updateData(paperMysqlBean);
        return RetResponse.makeOKRsp(result);
    }

}
