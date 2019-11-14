package cn.tju.seagraph.controller;

import cn.tju.seagraph.dao.StatisticsMapper;
import cn.tju.seagraph.dao.UserMapper;
import cn.tju.seagraph.daomain.RetResponse;
import cn.tju.seagraph.daomain.RetResult;
import cn.tju.seagraph.daomain.User;
import cn.tju.seagraph.service.EmailService;
import cn.tju.seagraph.utils.VerifyUtil;
import cn.tju.seagraph.utils.dateUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;


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
        String checkCode = String.valueOf(new Random().nextInt(899999) + 100000);
        String message = "您的验证码为："+checkCode;
        try {
            emailService.sendSimpleMail(json.get("email"), "验证码", message);
        }catch (Exception e){
            e.printStackTrace();
            return RetResponse.makeErrRsp("邮箱验证失败，请检查邮箱是否正确");
        }
        return RetResponse.makeOKRsp(checkCode);
    }

    @RequestMapping(value = "/getCheckUser", method = RequestMethod.POST)
    public RetResult<String> getCheckUser(@RequestBody Map<String,String> json){
        List<User> usernamelist = userMapper.getUserByUsername(json.get("username"));
        List<User> emaillist = userMapper.getUserByEmail(json.get("email"));
        if (usernamelist.size()==0 && emaillist.size() == 0){
            return RetResponse.makeOKRsp("ok");
        }else if (usernamelist.size() == 0){
            return RetResponse.makeErrRsp("该用户名已经被注册");
        }else if (emaillist.size() == 0){
            return RetResponse.makeErrRsp("该邮箱已经被注册");
        }else {
            return RetResponse.makeErrRsp("ERROR");
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public RetResult<Map> login(@RequestBody Map<String,String> json){
        List<User> list = userMapper.getUserByEmail(json.get("email"));
        System.out.println(json);
        if (list.size()==0){
            return RetResponse.makeErrRsp("邮箱不正确");
        }else {
            User user = list.get(0);
            if (user.getPasswd().equals(json.get("passwd"))){
                Map<String,String> map = new HashMap<>();
                map.put("username",user.getUsername());
                String token= JWT.create().withAudience(String.valueOf(user.getId())).sign(Algorithm.HMAC256(user.getPasswd()));
                user.setToken(token);
                userMapper.updateUser(user);
                map.put("token",token);
                dateUtils.update(statisticsMapper,0,dateUtils.gainDate());
                dateUtils.update(statisticsMapper,0,"2000-01-01");
                System.out.println(map);
                return RetResponse.makeOKRsp(map);
            }else {
                return RetResponse.makeErrRsp("密码不正确");
            }
        }
    }

    @RequestMapping(value = "/getImg", method = RequestMethod.POST)
    public RetResult<Object> getImg(HttpServletRequest httpRequest){
        HttpSession session = httpRequest.getSession();
        //利用图片工具生成图片
        //第一个参数是生成的验证码，第二个参数是生成的图片
        Object[] objs = VerifyUtil.createImage();
        //将验证码存入Session

        return RetResponse.makeOKRsp(objs);
    }

    @RequestMapping(value = "/checkUser", method = RequestMethod.POST)
    public RetResult<Object> checkUser(@RequestBody Map<String,String> map){
        List<User> list = userMapper.getUserByEmail(map.get("email"));
        if (list.size()==0){
            return RetResponse.makeErrRsp("邮箱不正确");
        }else {
            User user = list.get(0);
            if (user.getUsername().equals(map.get("username"))){
                return RetResponse.makeOKRsp();
            }else {
                return RetResponse.makeErrRsp("用户名不正确");
            }
        }
    }

    @RequestMapping(value = "/updateUser")
    public RetResult<String> updateUser(@RequestBody User user){
        List<User> list = userMapper.getUserByEmail(user.getEmail());
        User newUser = list.get(0);
        newUser.setPasswd(user.getPasswd());
        int flag = userMapper.updateUser(newUser);
        if (flag == 1){
            return RetResponse.makeOKRsp("ok");
        }else{
            return RetResponse.makeErrRsp("ERROR");
        }
    }

    @RequestMapping(value = "/updateKeywords")
    public RetResult<String> updateKeywords(@RequestBody User user){
        List<User> list = userMapper.getUserByToken(user.getToken());
        User newUser = list.get(0);
        String str = newUser.getKeywords();
        Set set = new HashSet();
        if (str!=null && str!=""){
            String[] keywords = str.replace("[","").replace("]","").split(",");
            for (String keyword:keywords){
                set.add(keyword);
            }
        }
        set.add(user.getKeywords());
        System.out.println(set.toString());
        newUser.setKeywords(set.toString());
        int flag = userMapper.updateUser(newUser);
        if (flag == 1){
            return RetResponse.makeOKRsp("ok");
        }else{
            return RetResponse.makeErrRsp("ERROR");
        }
    }

    @RequestMapping(value = "/deleteKeywords")
    public RetResult<String> deleteKeywords(@RequestBody User user){
        List<User> list = userMapper.getUserByToken(user.getToken());
        User newUser = list.get(0);
        String str = newUser.getKeywords();
        Set<String> set = new HashSet();
        String[] keywords = str.replace("[","").replace("]","").split(",");
        for (String keyword:keywords){
            set.add(keyword);
        }
        set.remove(user.getKeywords());
        newUser.setKeywords(set.toString());
        int flag = userMapper.updateUser(newUser);
        if (flag == 1){
            return RetResponse.makeOKRsp("ok");
        }else{
            return RetResponse.makeErrRsp("ERROR");
        }
    }

    @RequestMapping(value = "/getKeywords")
    public RetResult<List<String>> getKeywords(@RequestBody User user){
        List<User> list = userMapper.getUserByToken(user.getToken());
        if (list.size()!=0){
            String str = list.get(0).getKeywords();
            List<String> list1 = new ArrayList<>();
            if (str!=null || str!=""){
                String[] keywords = str.replace("[","").replace("]","").split(",");
                for (String keyword:keywords){
                    list1.add(keyword.trim());
                }
            }
            return RetResponse.makeOKRsp(list1);
        }else {
            return RetResponse.makeErrRsp("Error");
        }
    }

    @RequestMapping(value = "/regis", method = RequestMethod.POST)
    public RetResult<String> login(@RequestBody User user){
        user.setKeywords("[]");
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
