package cn.tju.seagraph.controller;

import cn.tju.seagraph.dao.StatisticsMapper;
import cn.tju.seagraph.dao.UserMapper;
import cn.tju.seagraph.daomain.RetResponse;
import cn.tju.seagraph.daomain.RetResult;
import cn.tju.seagraph.daomain.User;
import cn.tju.seagraph.service.EmailService;
import cn.tju.seagraph.utils.dateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


@RestController

@RequestMapping("/user")
public class UserController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    EmailService emailService;
    @Autowired
    StatisticsMapper statisticsMapper;
    @RequestMapping(value = "/getCheckCode", method = RequestMethod.POST)
    public RetResult<String> getCheckCode(@RequestBody Map<String,String> json){
        List<User> list = userMapper.getUserByEmail(json.get("email"));
        if (list.size()==0){
            String checkCode = String.valueOf(new Random().nextInt(899999) + 100000);
            String message = "您的注册验证码为："+checkCode;
            try {
                emailService.sendSimpleMail(json.get("email"), "注册验证码", message);
            }catch (Exception e){
                return RetResponse.makeErrRsp("邮箱注册失败，请检查邮箱是否正确");
            }
            return RetResponse.makeOKRsp(checkCode);
        }else {
            return RetResponse.makeErrRsp("该邮箱已经被注册");
        }
    }

    @RequestMapping(value = "/getCheckUser", method = RequestMethod.POST)
    public RetResult<String> getCheckUser(@RequestBody Map<String,String> json){
        List<User> list = userMapper.getUserByUsername(json.get("username"));
        if (list.size()==0){
            return RetResponse.makeOKRsp("ok");
        }else {
            return RetResponse.makeErrRsp("该用户名已经被注册");
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public RetResult<Map> login(@RequestBody Map<String,String> json){
        List<User> list = userMapper.getUserByEmail(json.get("email"));
        if (list.size()==0){
            return RetResponse.makeErrRsp("邮箱不正确");
        }else {
            User user = list.get(0);
            if (user.getPasswd().equals(json.get("passwd"))){
                Map<String,String> map = new HashMap<>();
                map.put("username",user.getUsername());
                dateUtils.update(statisticsMapper,0,dateUtils.gainDate());
                dateUtils.update(statisticsMapper,0,"2000-01-01");
                return RetResponse.makeOKRsp(map);
            }else {
                return RetResponse.makeErrRsp("密码不正确");
            }
        }
    }

    @RequestMapping(value = "/regis", method = RequestMethod.POST)
    public RetResult<String> login(@RequestBody User user){
        int flag = userMapper.insertUser(user);
        if (flag==1){
            dateUtils.update(statisticsMapper,1,dateUtils.gainDate());
            dateUtils.update(statisticsMapper,1,"2000-01-01");
            return RetResponse.makeOKRsp("ok");
        }else {
            return RetResponse.makeErrRsp("注册失败");
        }
    }
}
