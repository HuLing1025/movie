package com.example.cinema.controller.statistics;

import com.example.cinema.bl.statistics.StatisticsService;
import com.example.cinema.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api(tags = "statistics")
@RestController
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    @ApiOperation(value = "获取某天各影片排片率数据")
    @RequestMapping(value = "statistics/scheduleRate", method = RequestMethod.GET)
    public ResponseVO getScheduleRateByDate(@RequestParam(required = false) Date date){
        //此处date为非必填参数，若不填则默认为当天
        return statisticsService.getScheduleRateByDate(date);
    }

    @ApiOperation(value = "获取所有电影的累计票房", notes = "降序,包含已下架电影")
    @RequestMapping(value = "statistics/boxOffice/total", method = RequestMethod.GET)
    public ResponseVO getTotalBoxOffice(){
        return statisticsService.getTotalBoxOffice();
    }

    @ApiOperation(value = "过去7天客单价", notes = "客单价:(某天的客单价=当天观众购票所花金额/购票人次数)")
    @RequestMapping(value = "statistics/audience/price", method = RequestMethod.GET)
    public ResponseVO getAudiencePrice(){
        return statisticsService.getAudiencePriceSevenDays();
    }

    @ApiOperation(value = "上座率", notes = "获取所有电影某天的上座率 上座率参考公式：" +
            "假设某影城设有n 个电影厅、m 个座位数，相对上座率=观众人次÷放映场次÷m÷n×100% 此方法中运用到的相应的操作数据库的接口和实现请自行定义和实现，" +
            "相应的结果需要自己定义一个VO类返回给前端")
    @RequestMapping(value = "statistics/PlacingRate", method = RequestMethod.GET)
    public ResponseVO getMoviePlacingRateByDate(@RequestParam(required = false) Date date){
        return statisticsService.getMoviePlacingRateByDate(date);
    }

    @ApiOperation(value = "获取最近最受欢迎电影", notes = "获取最近days天内，最受欢迎的movieNum个电影(可以简单理解为最近days内票房越高的电影越受欢迎)" +
            " 此方法中运用到的相应的操作数据库的接口和实现请自行定义和实现，相应的结果需要自己定义一个VO类返回给前端")
    @RequestMapping(value = "statistics/popular/movie", method = RequestMethod.GET)
    public ResponseVO getPopularMovies(@RequestParam int days, @RequestParam int movieNum){
        return statisticsService.getPopularMovies(days, movieNum);
    }
}
