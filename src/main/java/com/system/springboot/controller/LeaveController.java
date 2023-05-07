package com.system.springboot.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.system.springboot.service.ILeaveService;
import com.system.springboot.entity.Leave;
import com.system.springboot.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import javax.annotation.Resource;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 学生请假表 前端控制器
 * </p>
 *
 * @author 吴泽鹏
 * @since 2023-04-18
 */
@Api(tags = "请假表操作")
@RestController
@RequestMapping("/leave")
public class LeaveController {
    @Resource
    private ILeaveService leaveService;
    @ApiOperation(value = "新增或更新数据",notes = "根据id实现数据的更新")
    //新增或修改 @RequestBody将前台的数据映射成User对象
    @PostMapping
    public  Result save(@RequestBody Leave leave){
        return Result.success(leaveService.saveOrUpdate(leave));
    }
    //删除数据 @PathVariable表述请求地址必须是（/{id}）
    @DeleteMapping("/{id}")
    @ApiOperation("删除")
    public Result delete(@PathVariable Integer id){
            return Result.success(leaveService.removeById(id));
    }
    @PostMapping("/del/batch")
    @ApiOperation("批量删除")
    public Result deleteBatch(@RequestBody List<Integer> ids){
            return Result.success(leaveService.removeByIds(ids));
    }
    //查询所有数据
    @GetMapping
    @ApiOperation(value = "获取所有数据")
    public Result findAll(){
//            return Result.success(leaveService.list());
        return  Result.success(leaveService.selectAllLeaveAndUsername());
    }
    //查询单个数据
    @GetMapping("/{id}")
    @ApiOperation(value = "获取单个数据")
    public Result findAll(@PathVariable Integer id){
        return Result.success(leaveService.getById(id));
    }
    //分页查询
    // 路径 /user/page
    // @RequestParam 接受 pageNum=1&pageSize=10
    @GetMapping("/page")
    @ApiOperation("分页/模糊查询")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String username,
                           @RequestParam(defaultValue = "") String  nickname){
        return Result.success(leaveService.findPage(new Page<>(pageNum, pageSize),username,nickname));
    }
    //查询单个数据 根据用户名
    @GetMapping("/username/{username}")
    @ApiOperation(value = "根据用户名获取数据")
    public Result findOne(@PathVariable String username) {
        QueryWrapper<Leave> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return Result.success(leaveService.list(queryWrapper));
    }
    //更新审批状态
    @PutMapping("/allow")
    @ApiOperation(value = "根据用户名获取数据")
    public Result findOne(@PathVariable Leave leave) {
        return Result.success(leaveService.updateById(leave));
    }
}
