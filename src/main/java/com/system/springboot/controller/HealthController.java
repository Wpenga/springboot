package com.system.springboot.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.springboot.common.Constants;
import com.system.springboot.common.Result;
import com.system.springboot.entity.Health;
import com.system.springboot.mapper.HealthMapper;
import com.system.springboot.service.IHealthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 吴泽鹏
 * @since 2023-04-14
 */
@RestController
@RequestMapping("/health")
@Api(tags = "健康表操作")
public class HealthController {
    @Resource
    private IHealthService healthService;
    @Resource
    private HealthMapper healthMapper;
    @GetMapping("/get/{username}")
    @ApiOperation("关联查询获取记录")
    public Result selectAllStudentAndRecord(@PathVariable String username) {
        return Result.success(healthService.selectAllStudentAndRecord(username));
    }
    @ApiOperation(value = "新增",notes = "新增数据，id自增")
    //新增或修改 @RequestBody将前台的数据映射成User对象
    @PostMapping
    public  Result save(@RequestBody Health health){
//        DateTime currentDate  = new DateTime();
//        // 比较当前日期与打卡日期是否一致
//        if (DateUtil.isSameDay(currentDate, health.getPunchDate())) {
            return Result.success(healthService.save(health));
//        }else {
//            return Result.error(Constants.CODE_600,"当天已提交，无需重复提交");
//        }
    }
    @PutMapping
    @ApiOperation(value = "更新数据",notes = "根据id实现数据的更新")
    public  Result update(@RequestBody Health health){
        return Result.success(healthService.updateById(health));
    }
    //删除数据 @PathVariable表述请求地址必须是（/{id}）
    @DeleteMapping("/{id}")
    @ApiOperation("删除")
    public Result delete(@PathVariable Integer id){
            return Result.success(healthService.removeById(id));
    }
    @PostMapping("/del/batch")
    @ApiOperation("批量删除")
    public Result deleteBatch(@RequestBody List<Integer> ids){
            return Result.success(healthService.removeByIds(ids));
    }
    //查询所有数据
    @GetMapping()
    @ApiOperation(value = "获取所有数据")
    public Result findAll(){
            return Result.success(healthService.list());
    }
    //查询单个数据
    @GetMapping("/{id}")
    @ApiOperation(value = "获取单个数据")
    public Result findAll(@PathVariable Integer id){
        return Result.success(healthService.getById(id));
    }
    //分页查询
    // 路径 /user/page
    // @RequestParam 接受 pageNum=1&pageSize=10
    @GetMapping("/page")
    @ApiOperation("分页/模糊查询")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize){
            IPage<Health> page = new Page<>(pageNum, pageSize);

            QueryWrapper<Health> queryWrapper = new QueryWrapper<>();
        //多条件模糊查询
        queryWrapper.orderByDesc("id");
        return Result.success(healthService.page(page,queryWrapper));
    }
    //查询单个数据 根据用户名
    @GetMapping("/username/{username}")
    @ApiOperation(value = "根据用户名获取数据")
    public Result findOne(@PathVariable String username){
        return  Result.success(healthMapper.getHealthByUser(username));
    }
    //查询单个数据 根据用户名
    @GetMapping("/username1/{username}")
    @ApiOperation(value = "根据用户名获取昵称")
    public Result findHealthAndNickname(@PathVariable String username){
        return  Result.success(healthMapper.findHealthAndNickname(username));
    }
}
