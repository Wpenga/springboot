package com.system.springboot.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.springboot.common.Result;
import com.system.springboot.entity.Files;
import com.system.springboot.mapper.FileMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Api(tags = "文件类操作")
@RestController
@RequestMapping("file")
public class FileController {

    @Value("${files.upload.path}")
    private String fileUploadPath;  //读取上传路径

    @Value("${server.ip}")
    private String serverIp;
    @Value("${server.port}")
    private String port;
    @Resource
    private FileMapper fileMapper;

    @ApiOperation("文件上传")
    @PostMapping("upload")
    public Result upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename(); //文件名
        String type = FileUtil.extName(originalFilename);//获取文件类型
        long size = file.getSize();         //获取文件大小

        String uuid= IdUtil.fastSimpleUUID(); //生成一个随机UUID
        String fileUUID = uuid + StrUtil.DOT + type; //组成文件名abc.jpg

        File uploadFile = new File(fileUploadPath +fileUUID); //新建文件

        // 新建存储文件目录，若不存在则新建
        File parentFile = uploadFile.getParentFile();
        if(!parentFile.exists()) {
            parentFile.mkdirs();
        }
//        file.transferTo(uploadFile);
        //获取文件的url
        String url;
        //获取文件md5，通过判断md5是否存在避免重复上传相同内容的文件
        String md5 = SecureUtil.md5(file.getInputStream());

        //获取文件记录
        Files dbFiles = getFileByMd5(md5);
        if(dbFiles != null){
            url = dbFiles.getUrl(); //获取文件访问地址
        }else {
            // 获取文件存储到磁盘目录
            file.transferTo(uploadFile);
            // 文件的访问地址
            url = "http://"+ serverIp + ":"+ port + "/file/"+fileUUID;
        }

        // 存储数据库
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size/1024); //文件大小单位kb
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        // 插入文件信息
        fileMapper.insert(saveFile);
//        flushRedis(Constants.FILES_KEY);
        Map<String, String> urlMap = new HashMap<>();
        urlMap.put("url", url);
        return Result.success(urlMap);
//        return Result.success(url);
    }

    @ApiOperation("文件下载")
    @GetMapping("{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws  IOException{
        //根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath +fileUUID);
        //设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileUUID,"UTF-8"));
        response.setContentType("application/octet-stream");

        //读取文件字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }


    /**
     * 通过文件md5查询文件
     * @param md5
     * @return
     */
    private Files getFileByMd5(String md5){
        //查询文件的md5是否已存在
        QueryWrapper<Files> queryWrapper = new QueryWrapper();
        queryWrapper.eq("md5",md5);
        List<Files> FilesList = fileMapper.selectList(queryWrapper);
        return FilesList.size() ==0 ? null : FilesList.get(0);  //只返回一个文件记录
    }

    @PostMapping("/update")
    @ApiOperation("更新文件记录信息")
    public Result update(@RequestBody Files Files) {
        fileMapper.updateById(Files);
//        flushRedis(Constants.FILES_KEY);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    @ApiOperation("获取文件信息")
    public Result getById(@PathVariable Integer id) {
        return Result.success(fileMapper.selectById(id));
    }

    //清除一条缓存，key为要清空的数据
//    @CacheEvict(value="files",key="'frontAll'")
    @DeleteMapping("/{id}")
    @ApiOperation("删除文件记录")
    public Result delete(@PathVariable Integer id) {
        Files Files = fileMapper.selectById(id);
        Files.setIsDelete(true);
        fileMapper.updateById(Files);
//        flushRedis(Constants.FILES_KEY);
        return Result.success();
    }

    @PostMapping("/del/batch")
    @ApiOperation("批量删除文件记录")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        // select * from sys_file where id in (id,id,id...)
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        List<Files> files = fileMapper.selectList(queryWrapper);
        for (Files file : files) {
            file.setIsDelete(true);
            fileMapper.updateById(file);
        }
        return Result.success();
    }

    /**
     * 分页查询接口
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name) {

        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        // 查询未删除的记录
        queryWrapper.eq("is_delete", false);
//        queryWrapper.orderByDesc("id");
//        if (!"".equals(name)) {
//            queryWrapper.like("name", name);
//        }
        queryWrapper.like(Strings.isNotEmpty(name),"name",name);
        return Result.success(fileMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper));
    }
}
