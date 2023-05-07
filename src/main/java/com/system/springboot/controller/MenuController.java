package com.system.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.springboot.common.Constants;
import com.system.springboot.common.Result;
import com.system.springboot.entity.Dict;
import com.system.springboot.entity.Menu;
import com.system.springboot.mapper.DictMapper;
import com.system.springboot.service.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/menu")
@Api(tags = "菜单类操作")
public class MenuController {
    @Resource
    private IMenuService menuService;
    @Resource
    private DictMapper dictMapper;

    @ApiOperation(value = "新增或更新数据",notes = "根据id实现数据的更新")
    //新增或修改 @RequestBody将前台的数据映射成User对象
    @PostMapping
    public  Result save(@RequestBody Menu menu){
        return Result.success(menuService.saveOrUpdate(menu));
    }
    //删除数据 @PathVariable表述请求地址必须是（/{id}）
    @DeleteMapping("/{id}")
    @ApiOperation("删除")
    public Result delete(@PathVariable Integer id){
            return Result.success(menuService.removeById(id));
    }
    @PostMapping("/del/batch")
    @ApiOperation("批量删除")
    public Result deleteBatch(@RequestBody List<Integer> ids){
            return Result.success(menuService.removeByIds(ids));
    }

    @GetMapping("/ids")
    @ApiOperation("菜单id数组")
    public Result findAllIds() {
        return Result.success(menuService.list().stream().map(Menu::getId));
    }
    //查询所有数据
    @GetMapping
    @ApiOperation(value = "获取所有数据")
    public Result findAll(@RequestParam String name){
        return Result.success(menuService.findMenus(name));
    }

    //查询单个数据
    @GetMapping("/id")
    @ApiOperation(value = "获取单个数据")
    public Result findAll(@PathVariable Integer id){
        return Result.success(menuService.getById(id));
    }
    //分页查询
    // @RequestParam 接受 pageNum=1&pageSize=10
    @GetMapping("/page")
    @ApiOperation("分页/模糊查询")
    public Result findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize){
        IPage<Menu> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",name);
        //多条件模糊查询
//        queryWrapper.orderByDesc("id");
        return Result.success(menuService.page(page,queryWrapper));
    }

    @GetMapping("/icons")
    @ApiOperation("获取图标信息")
    public Result getIcons(){
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", Constants.DICT_TYPE_ICON);
        return Result.success(dictMapper.selectList(queryWrapper));
    }
}
