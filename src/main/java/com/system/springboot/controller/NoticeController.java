package com.system.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.springboot.common.Constants;
import com.system.springboot.common.Result;
import com.system.springboot.common.RoleEnum;
import com.system.springboot.entity.Notice;
import com.system.springboot.service.INoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 吴泽鹏
 * @since 2023-04-12
 */
/*
 *   通知类操作
 */
@RestController
@RequestMapping("/notice")
@Api(tags = "通知类操作")
public class NoticeController {
    @Resource
    private INoticeService noticeService;
    @ApiOperation(value = "新增或更新数据",notes = "根据id实现数据的更新/新增")
    //新增或修改 @RequestBody将前台的数据映射成User对象
    @PostMapping
    public  Result save(@RequestBody Notice notice){
        return Result.success(noticeService.saveOrUpdate(notice));
    }
    @PutMapping
    @ApiOperation(value = "更新数据",notes = "根据id实现数据的更新")
    public  Result update(@RequestBody Notice notice){
        return Result.success(noticeService.updateById(notice));
    }
    //删除数据 @PathVariable表述请求地址必须是（/{id}）
    @DeleteMapping("/{id}")
    @ApiOperation("删除")
    public Result delete(@PathVariable Integer id){
            return Result.success(noticeService.removeById(id));
    }
    @PostMapping("/del/batch")
    @ApiOperation("批量删除")
    public Result deleteBatch(@RequestBody List<Integer> ids){
            return Result.success(noticeService.removeByIds(ids));
    }
    //查询所有数据
    @GetMapping
    @ApiOperation(value = "获取所有数据")
    public Result findAll(){
            return Result.success(noticeService.list());
    }
    //查询单个数据
    @GetMapping("/{id}")
    @ApiOperation(value = "获取单个数据")
    public Result findAll(@PathVariable Integer id){
        return Result.success(noticeService.getById(id));
    }
    //分页查询
    // 路径 /user/page
    // @RequestParam 接受 pageNum=1&pageSize=10
    @GetMapping("/page")
    @ApiOperation("分页/模糊查询")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String noticeTitle,
                           @RequestParam(defaultValue = "") String createBy){
        IPage<Notice> page = new Page<>(pageNum, pageSize);

        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(Strings.isNotEmpty(noticeTitle),"notice_title", noticeTitle);
        queryWrapper.like(Strings.isNotEmpty(createBy),"create_by", createBy);
        //多条件模糊查询
//        queryWrapper.orderByDesc("id");
        return  Result.success(noticeService.page(page,queryWrapper));
//        return noticeService.page(page,queryWrapper);
    }

    @ApiOperation("获取发布的通知")
    @GetMapping("/open")
    public Result getOpen(@RequestParam(defaultValue = "") String username){
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status","1");
//        queryWrapper.eq(Strings.isNotEmpty(username),"receiver",username);
        return Result.success(noticeService.list(queryWrapper));
    }
}
