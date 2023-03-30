package com.system.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.springboot.common.Result;
import com.system.springboot.entity.Role;
import com.system.springboot.service.IRoleService;
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
 * @since 2023-03-30
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Resource
    private IRoleService roleService;
    @ApiOperation(value = "新增或更新数据",notes = "根据id实现数据的更新")
    //新增或修改 @RequestBody将前台的数据映射成User对象
    @PostMapping
    public  Result save(@RequestBody Role role){
        return Result.success(roleService.saveOrUpdate(role));
    }
    //删除数据 @PathVariable表述请求地址必须是（/{id}）
    @DeleteMapping("/{id}")
    @ApiOperation("删除")
    public Result delete(@PathVariable Integer id){
            return Result.success(roleService.removeById(id));
    }
    @PostMapping("/del/batch")
    @ApiOperation("批量删除")
    public Result deleteBatch(@RequestBody List<Integer> ids){
            return Result.success(roleService.removeByIds(ids));
    }
    //查询所有数据
    @GetMapping
    @ApiOperation(value = "获取所有数据")
    public Result findAll(){
            return Result.success(roleService.list());
    }
    //查询单个数据
    @GetMapping("/id")
    @ApiOperation(value = "获取单个数据")
    public Result findAll(@PathVariable Integer id){
        return Result.success(roleService.getById(id));
    }
    //分页查询
    // 路径 /user/page
    // @RequestParam 接受 pageNum=1&pageSize=10
    @GetMapping("/page")
    @ApiOperation("分页/模糊查询")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name){
            IPage<Role> page = new Page<>(pageNum, pageSize);
            //多条件模糊查询
            QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
            queryWrapper.like(Strings.isNotEmpty(name),"name",name);
            queryWrapper.orderByDesc("id");

        return Result.success(roleService.page(page,queryWrapper));
    }
}
