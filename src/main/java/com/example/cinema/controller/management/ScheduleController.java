package com.example.cinema.controller.management;

import com.example.cinema.bl.management.ScheduleService;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.ScheduleBatchDeleteForm;
import com.example.cinema.vo.ScheduleForm;
import com.example.cinema.vo.ScheduleViewForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api(tags = "management接口")
@RestController
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation(value = "添加排片")
    @RequestMapping(value = "/schedule/add", method = RequestMethod.POST)
    public ResponseVO addSchedule(@RequestBody ScheduleForm scheduleForm){
        return scheduleService.addSchedule(scheduleForm);
    }

    @ApiOperation(value = "修改排片")
    @RequestMapping(value = "/schedule/update", method = RequestMethod.POST)
    public ResponseVO updateSchedule(@RequestBody ScheduleForm scheduleForm){
        return scheduleService.updateSchedule(scheduleForm);
    }

    @ApiOperation(value = "7天排片计划")
    @RequestMapping(value = "/schedule/search", method = RequestMethod.GET)
    public ResponseVO searchSchedule(@RequestParam int hallId, @RequestParam Date startDate){
        //这里传递startDate参数时，前端传的是用/分隔的时间，例如startDate=2019/04/12
        return scheduleService.searchScheduleSevenDays(hallId, startDate);
    }

    @ApiOperation(value = "排片信息")
    @RequestMapping(value = "/schedule/search/audience", method = RequestMethod.GET)
    public ResponseVO searchAudienceSchedule(@RequestParam int movieId){
        return scheduleService.searchAudienceSchedule(movieId);
    }

    @ApiOperation(value = "设置排片信息可见天数")
    @RequestMapping(value = "/schedule/view/set", method = RequestMethod.POST)
    public ResponseVO setScheduleView(@RequestBody  ScheduleViewForm scheduleViewForm){
        return scheduleService.setScheduleView(scheduleViewForm);
    }

    @ApiOperation(value = "获取排片信息可见天数")
    @RequestMapping(value = "/schedule/view", method = RequestMethod.GET)
    public ResponseVO getScheduleView(){
        return scheduleService.getScheduleView();
    }

    @ApiOperation(value = "批量删除排片信息")
    @RequestMapping(value = "/schedule/delete/batch", method = RequestMethod.DELETE)
    public ResponseVO deleteBatchOfSchedule(@RequestBody ScheduleBatchDeleteForm scheduleBatchDeleteForm){
        return scheduleService.deleteBatchOfSchedule(scheduleBatchDeleteForm);
    }

    @ApiOperation(value = "根据ID获取排片信息")
    @RequestMapping(value = "/schedule/{id}", method = RequestMethod.GET)
    public ResponseVO getScheduleById(@PathVariable int id){
        return scheduleService.getScheduleById(id);
    }
}
