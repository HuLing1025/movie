package com.example.cinema.controller.promotion;

import com.example.cinema.bl.promotion.VIPService;
import com.example.cinema.vo.VIPCardForm;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.VIPInfoForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "promotion接口")
@RestController()
@RequestMapping("/vip")
public class VIPCardController {
    @Autowired
    VIPService vipService;

    @ApiOperation(value = "添加会员卡")
    @PostMapping("/add")
    public ResponseVO addVIP(@RequestParam int userId, @RequestParam String type){
        return vipService.addVIPCard(userId,type);
    }

    @ApiOperation(value = "获取用户会员卡")
    @GetMapping("{userId}/get")
    public ResponseVO getVIP(@PathVariable int userId){
        return vipService.getCardByUserId(userId);
    }

    @ApiOperation(value = "获取用户会员卡详情")
    @GetMapping("{userId}/getVIPInfo")
    public ResponseVO getVIPInfo(@PathVariable int userId){
        return vipService.getVIPInfo(userId);
    }

    @ApiOperation(value = "会员卡充值")
    @PostMapping("/charge")
    public ResponseVO charge(@RequestBody VIPCardForm vipCardForm){
        return vipService.charge(vipCardForm);
    }

    @ApiOperation(value = "获取全部会员卡详情")
    @GetMapping("/getAllVIPInfo")
    public ResponseVO getAllVIPInfo(){
        return vipService.getAllVIPInfo();
    }

    @ApiOperation(value = "发布会员卡")
    @PostMapping("/publish")
    public ResponseVO publishVIPCard(@RequestBody VIPInfoForm vipInfoForm){
        return vipService.publishVIPCard(vipInfoForm);
    }

    @ApiOperation(value = "调整会员卡")
    @PostMapping("/modify")
    public ResponseVO modifyVIPCard(@RequestBody VIPInfoForm vipInfoForm){
        return vipService.modifyVIPCard(vipInfoForm);
    }
}
