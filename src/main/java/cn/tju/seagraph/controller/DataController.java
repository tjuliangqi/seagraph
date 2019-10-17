package cn.tju.seagraph.controller;

import cn.tju.seagraph.dao.DataMapper;
import cn.tju.seagraph.daomain.RetResponse;
import cn.tju.seagraph.daomain.RetResult;

import cn.tju.seagraph.daomain.data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

@RequestMapping("/data")
public class DataController {
    @Autowired
    DataMapper dataMapper;
    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public RetResult<List<data>> getCount() {
        List<data> list = dataMapper.getDataList();
        return RetResponse.makeOKRsp(list);
    }
}
