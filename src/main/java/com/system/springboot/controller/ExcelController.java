package com.system.springboot.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.system.springboot.common.Result;
import com.system.springboot.entity.User;
import com.system.springboot.mapper.UserMapper;
import com.system.springboot.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/excel")
@Api(tags = "表格文件操作")
public class ExcelController {
    @Resource
    private IUserService userService;
    @Resource
    private UserMapper userMapper;
    //导出未签到的学生信息
    @GetMapping("/notfever")
    @ApiOperation("下载用户表")
    public void export(HttpServletResponse response) throws Exception{
        List<User> list =userMapper.getStuExportList(false);//发烧
        // 通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("nickname", "姓名");
        writer.addHeaderAlias("phone", "手机号");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("sex", "性别");
        writer.addHeaderAlias("address", "地址");
        writer.addHeaderAlias("isFever", "发烧情况");
        writer.addHeaderAlias("punchDate", "签到时间");


        // 一次性写出内容，使用默认样式，强制输出标题
        Field[] fields = list.get(0).getClass().getDeclaredFields(); // 获取 User 类中所有的成员变量
        int count = fields.length; // 获取成员变量数量
        writer.merge(count-1, "学生情况表");   //标题位置
        writer.write(list, true);

        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        //文件名
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(date);
        String fileName = URLEncoder.encode("学生情况表 "+formattedDate,"UTF-8");
        response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");
        //输出流
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out,true);
        out.close();
        // 关闭writer，释放内存
        writer.close();
    }
    //导入
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception{
        //获取输入流
        InputStream inputStream = file.getInputStream();
        //通过插件提供的方法读取输入流
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //读取的类型为泛型User
        List<User> list = reader.readAll(User.class);
        //数据批量保存到数据库
        userService.saveBatch(list);
        System.out.println(list);
        return Result.success(true);
    }
}
