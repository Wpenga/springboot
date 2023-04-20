package com.system.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.system.springboot.common.Result;
import com.system.springboot.entity.Health;
import com.system.springboot.entity.User;
import com.system.springboot.mapper.HealthMapper;
import com.system.springboot.service.IHealthService;
import com.system.springboot.service.IUserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.jws.soap.SOAPBinding;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/echarts")
@Api(tags = "数据处理")
public class EchartsController {

    @Resource
    private HealthMapper healthMapper;
    @Autowired
    private IUserService userService;
    @Autowired
    private IHealthService healthService;
    @GetMapping("/example")
    public Result get(){
        Map<String, Object>  map = new HashMap<>();
        map.put("x", CollUtil.newArrayList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        map.put("y",CollUtil.newArrayList(150, 230, 224, 218, 135, 147, 260));
        return Result.success(map);
    }
    //获取统计数据
    @GetMapping("/getdata")
    public Result getdata(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("role","ROLE_STUDENT");
        List<User> list1 = userService.list(queryWrapper);
        int total = list1.size();                       //学生户数
        List<Health> list2 = healthMapper.getisSign();
        DateTime currentDate  = new DateTime();         // 获取当前时间
//        System.out.println("当前时间"+currentDate);
        int signCount = 0;                              // 已打卡人数初始化为 0
        for (Health health : list2) {                   // 遍历 List
            Date checkInDate = health.getPunchDate();   // 获取打卡日期
//            System.out.println("打卡时间"+checkInDate);
            // 比较当前日期与打卡日期是否一致
            if (DateUtil.isSameDay(currentDate, checkInDate)) {
                signCount++;
            }
        }
        int isFeverCount = healthMapper.isFeverCount();  //发烧人数
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("signCount", signCount);
        map.put("isFeverCount", isFeverCount);
        return Result.success(map);
    }
//    @GetMapping("members")
    @GetMapping("members")
    public  Result members(){
        //获取打卡表信息
//        QueryWrapper<Health> queryWrapper = new QueryWrapper<>();
//        queryWrapper.in("username",username)
        List<Health> list = healthService.list();
//        List<User> list = userService.list();
        int q1 = 0; // 0-6点
        int q2 = 0; // 6-12点
        int q3 = 0; // 12-18点
        int q4 = 0; // 18-24点
        for (Health health : list){
            Date PunchDate = health.getPunchDate();
            int hour = DateUtil.hour(PunchDate,true);
            if(hour>=0 && hour<6) {
                q1 += 1;
            } else if(hour>=6 && hour<12) {
                q2 += 1;
            } else if(hour>=12 && hour<18) {
                q3 += 1;
            } else if(hour>=18 && hour<24) {
                q4 += 1;
            }
        }
        return Result.success(CollUtil.newArrayList( q1, q2, q3, q4));
    }
//    @GetMapping("/studentinfo")
//    public Result getStuInfo(){
////        return Result.success(userService.getStuInfo());
//    }
}
