package com.example.cinema.controller.promotion;

import com.example.cinema.bl.promotion.CouponService;
import com.example.cinema.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "promotion接口")
@RestController
@RequestMapping("/coupon")
public class CouponController {
    @Autowired
    CouponService couponService;

    @ApiOperation(value = "根据UID获取优惠券")
    @GetMapping("{userId}/get")
    public ResponseVO getCoupons(@PathVariable int userId){
        return couponService.getCouponsByUser(userId);
    }

    @ApiOperation(value = "获取优惠券列表")
    @GetMapping("/valid")
    public ResponseVO getAllValidCoupons() {
        return couponService.getValidCouponList();
    }

    @ApiOperation(value = "领取优惠券")
    @GetMapping("/issue")
    public ResponseVO issueCoupon(@RequestParam int couponId, @RequestParam int userId) {
        return couponService.issueCoupon(couponId, userId);
    }
}
