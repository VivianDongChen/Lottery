package com.itheima.prize.api.action;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.prize.commons.db.entity.CardGame;
import com.itheima.prize.commons.db.entity.CardProductDto;
import com.itheima.prize.commons.db.entity.ViewCardUserHit;
import com.itheima.prize.commons.db.mapper.CardGameMapper;
import com.itheima.prize.commons.db.mapper.GameLoadMapper;
import com.itheima.prize.commons.db.mapper.ViewCardUserHitMapper;
import com.itheima.prize.commons.db.service.CardGameService;
import com.itheima.prize.commons.db.service.GameLoadService;
import com.itheima.prize.commons.db.service.ViewCardUserHitService;
import com.itheima.prize.commons.utils.ApiResult;
import com.itheima.prize.commons.utils.PageBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/game")
@Api(tags = {"活动模块"})
public class GameController {
    @Autowired
    private GameLoadService loadService;
    @Autowired
    private CardGameService gameService;
    @Autowired
    private ViewCardUserHitService hitService;

    @GetMapping("/list/{status}/{curpage}/{limit}")
    @ApiOperation(value = "活动列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="status",value = "活动状态（-1=全部，0=未开始，1=进行中，2=已结束）",example = "-1",required = true),
            @ApiImplicitParam(name = "curpage",value = "第几页",defaultValue = "1",dataType = "int", example = "1",required = true),
            @ApiImplicitParam(name = "limit",value = "每页条数",defaultValue = "10",dataType = "int",example = "3",required = true)
    })
    public ApiResult<PageBean<CardGame>> list(@PathVariable int status,@PathVariable int curpage,@PathVariable int limit) {
        //TODO

        // 设置分页参数
        Page<CardGame> page = new Page<>(curpage, limit);

        // 设置查询条件
        QueryWrapper<CardGame> queryWrapper = new QueryWrapper<>();
        if (status != -1) {
            queryWrapper.eq("status", status);
        }

        // 调用 service 执行分页查询
        Page<CardGame> resultPage = gameService.page(page, queryWrapper);

        // 使用 PageBean 对象包装分页结果
        PageBean<CardGame> pageBean = new PageBean<>(resultPage);

        // 构建返回结果
        ApiResult<PageBean<CardGame>> result = new ApiResult<>(1, "成功", pageBean);

        return result;
    }

    @GetMapping("/info/{gameid}")
    @ApiOperation(value = "活动信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="gameid",value = "活动id",example = "1",required = true)
    })
    public ApiResult<CardGame> info(@PathVariable int gameid) {
        //TODO

        // 通过 gameid 查找活动信息
        CardGame cardGame = gameService.getById(gameid);

        // 如果未找到活动，返回错误信息
        if (cardGame == null) {
            return new ApiResult<>(0, "活动未找到", null);
        }

        // 构建返回结果
        ApiResult<CardGame> result = new ApiResult<>(1, "成功", cardGame);

        // 设置格式化后的当前时间
        result.setNow(new Date());

        return result;
    }

    @GetMapping("/products/{gameid}")
    @ApiOperation(value = "奖品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="gameid",value = "活动id",example = "1",required = true)
    })
    public ApiResult<List<CardProductDto>> products(@PathVariable int gameid) {
        //TODO

        // 使用 loadService 通过 gameid 查找活动相关的奖品信息
        List<CardProductDto> productList = loadService.getByGameId(gameid);

        // 如果未找到奖品信息，返回错误信息
        if (productList == null || productList.isEmpty()) {
            return new ApiResult<>(0, "未找到相关奖品信息", null);
        }

        // 构建返回结果
        ApiResult<List<CardProductDto>> result = new ApiResult<>(1, "成功", productList);

        // 设置当前时间
        result.setNow(new Date());

        return result;
    }

    @GetMapping("/hit/{gameid}/{curpage}/{limit}")
    @ApiOperation(value = "中奖列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="gameid",value = "活动id",dataType = "int",example = "1",required = true),
            @ApiImplicitParam(name = "curpage",value = "第几页",defaultValue = "1",dataType = "int", example = "1",required = true),
            @ApiImplicitParam(name = "limit",value = "每页条数",defaultValue = "10",dataType = "int",example = "3",required = true)
    })
    public ApiResult<PageBean<ViewCardUserHit>> hit(@PathVariable int gameid,@PathVariable int curpage,@PathVariable int limit) {
        //TODO

        // 设置分页参数
        Page<ViewCardUserHit> page = new Page<>(curpage, limit);

        // 设置查询条件
        QueryWrapper<ViewCardUserHit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("gameid", gameid);

        // 执行分页查询
        Page<ViewCardUserHit> resultPage = hitService.page(page, queryWrapper);

        // 使用 PageBean 对象包装分页结果
        PageBean<ViewCardUserHit> pageBean = new PageBean<>(resultPage);

        // 构建返回结果
        ApiResult<PageBean<ViewCardUserHit>> result = new ApiResult<>(1, "成功", pageBean);

        return result;
    }


}