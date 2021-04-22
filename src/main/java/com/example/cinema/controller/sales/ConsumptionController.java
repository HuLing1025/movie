package com.example.cinema.controller.sales;

import com.example.cinema.bl.sales.ConsumptionService;
import com.example.cinema.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "sales")
@RestController
@RequestMapping("/consumption")
public class ConsumptionController {

    @Autowired
    ConsumptionService consumptionService;

    @ApiOperation(value = "用户消费记录")
    @GetMapping("/{userId}")
    public ResponseVO getConsumptionByUserId(@PathVariable int userId){
        return consumptionService.getConsumptionByUserId(userId);
    }

    @ApiOperation(value = "消费记录详情")
    @GetMapping("/get/{consumptionId}")
    public ResponseVO getConsumptionDetails(@PathVariable("consumptionId") int consumptionId){
        return consumptionService.getConsumptionDetails(consumptionId);
    }
}
