package com.system.springboot.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.TemplateType;

import java.util.Collections;
/*
* 代码生成器
* by 吴泽鹏
* since 2023/3/16
* */
public class CodeGenerator  {
    public static void main(String[] args) {
            generate();
    }
    private static void generate() {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/mydb?serverTimezone=GMT%2b8", "root", "")
                .globalConfig(builder -> {
                    builder.author("吴泽鹏") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("H:\\管理系统\\springboot-maven\\src\\main\\java\\"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.system.springboot") // 设置父包名
                            //.moduleName("system") // 设置父包模块名
                            .moduleName(null) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "H:\\管理系统\\springboot-maven\\src\\main\\resources\\mapper\\")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableLombok();//使用Lombok简写
                    //builder.mapperBuilder().enableMapperAnnotation().build();  //自动注解@Mapper
                    builder.controllerBuilder().enableHyphenStyle()  // 开启驼峰转换字符
                            .enableRestStyle();  // 开启生成@RestController 控制器
                    builder.addInclude("stu_leave") // 设置需要生成的表名
                            .addTablePrefix("sys_", "stu_"); // 设置过滤表前缀
                })
                //.templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
    public static void controller(){
        new TemplateConfig.Builder()
//            .disable(TemplateType.ENTITY)
//            .entity("/templates/entity.java")
//            .service("/templates/service.java")
//            .serviceImpl("/templates/serviceImpl.java")
//            .mapper("/templates/mapper.java")
//            .mapperXml("/templates/mapper.xml")
//            .controller("src/main/resources/templates/controller.java")
            .build();
    }
}
