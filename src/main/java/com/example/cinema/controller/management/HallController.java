package com.example.cinema.controller.management;

import com.example.cinema.bl.management.HallService;
import com.example.cinema.vo.HallForm;
import com.example.cinema.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "management接口")
@RestController
public class HallController {
    @Autowired
    private HallService hallService;

    @ApiOperation(value = "影厅列表")
    @GetMapping("/hall/all")
    public ResponseVO searchAllHall(){
        return hallService.searchAllHall();
    }

    @ApiOperation(value = "添加影厅")
    @PostMapping("/hall/add")
    public ResponseVO addHall(@RequestBody HallForm hallForm){
        return hallService.addHall(hallForm);
    }

    @ApiOperation(value = "更新影厅")
    @PostMapping("/hall/update")
    public ResponseVO updateHall(@RequestBody HallForm hallForm){
        return hallService.updateHall(hallForm);
    }

    @ApiOperation(value = "删除影厅")
    @PostMapping(value = "/hall/delete/{hallId}")
    public ResponseVO deleteHall(@PathVariable("hallId") int hallId){
        return hallService.deleteHall(hallId);
    }
}
