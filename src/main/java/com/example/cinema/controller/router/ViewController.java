package com.example.cinema.controller.router;

import com.example.cinema.vo.UserForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

@Api(tags = "router接口")
@Controller
public class ViewController {
    @ApiOperation(value = "首页index")
    @GetMapping("/index")
    public String getIndex() {
        return "index";
    }

    @ApiOperation(value = "注册页面")
    @GetMapping( "/signUp")
    public String getSignUp() {
        return "signUp";
    }

    @ApiOperation(value = "售卖情况页面")
    @GetMapping("/admin/sale/detail")
    public String getAdminMovieBuy(@RequestParam int id) {
        return "adminMovieBuy";
    }

    @ApiOperation(value = "买票页面")
    @GetMapping("/admin/sale/detail/buy")
    public String getAdminLockSeat(@RequestParam int id) {
        return "adminLockSeat";
    }

    @ApiOperation(value = "电影管理页面")
    @GetMapping("/admin/movie/manage")
    public String getAdminMovieManage() {
        return "adminMovieManage";
    }

    @ApiOperation(value = "排片管理页面")
    @GetMapping("/admin/session/manage")
    public String getAdminSessionManage() {
        return "adminScheduleManage";
    }

    @ApiOperation(value = "影院管理页面")
    @GetMapping("/admin/cinema/manage")
    public String getAdminCinemaManage() {
        return "adminCinemaManage";
    }

    @ApiOperation(value = "活动管理页面")
    @GetMapping("/admin/promotion/manage")
    public String getAdminPromotionManage() {
        return "adminPromotionManage";
    }

    @ApiOperation(value = "会员管理页面")
    @GetMapping("/admin/member/manage")
    public String getAdminMemberManage(){
        return "adminMemberManage";
    }

    @ApiOperation(value = "影院统计页面")
    @GetMapping("/admin/cinema/statistic")
    public String getAdminCinemaStatistic() {
        return "adminCinemaStatistic";
    }

    @ApiOperation(value = "电影详情页面(根据用户会员等级返回)")
    @GetMapping("/admin/movieDetail")
    public String getAdminMovieDetail(@RequestParam int id, HttpServletRequest httpServletRequest) {
        HttpSession session=httpServletRequest.getSession();
        UserForm userForm = (UserForm) session.getAttribute("user");
        int level = userForm.getPrivilegeLevel();
        if (level == 0) {
            return "adminMovieDetail";
        } else if (level == 2) {
            return "adminMovieBuy";
        } else {
            return "userNewHome";
        }
    }

    @ApiOperation(value = "用户管理页面")
    @GetMapping("/admin/account/manage")
    public String getAdminAccountManage() {
        return "adminAccountManage";
    }

    @ApiOperation(value = "用户详情页面")
    @GetMapping("/admin/account/detail")
    public String getAccountDetail(@RequestParam int id) {
        return "adminAccountDetail";
    }

    @ApiOperation(value = "礼券赠送页面")
    @GetMapping("/admin/account/coupon/gift")
    public String getAdminCouponGift(@RequestParam int id) {
        return "adminCouponGift";
    }

    @ApiOperation(value = "首页home")
    @GetMapping("/user/home")
    public String getUserHome() {
        return "userNewHome";
    }

    @ApiOperation(value = "购票页面")
    @GetMapping("/user/buy")
    public String getUserBuy() {
        return "userNewBuy";
    }

    @ApiOperation(value = "订单详情页面")
    @GetMapping("/user/buy/payment")
    public String getUserPayment(@RequestParam String orderTime) {
        return "userPayment";
    }

    @ApiOperation(value = "用户消费记录页面")
    @GetMapping("/user/consumption")
    public String getUserConsumption() {
        return "userNewConsumption";
    }

    @ApiOperation(value = "电影详情页面")
    @GetMapping("/user/movieDetail")
    public String getUserMovieDetail(@RequestParam int id) {
        return "userMovieDetail";
    }

    @ApiOperation(value = "使用优惠券购票")
    @GetMapping("/user/consumptionDetail")
    public String getConsumptionDetail(@RequestParam int id) {
        return "userConsumptionDetail";
    }

    @ApiOperation(value = "购票")
    @GetMapping("/user/movieDetail/buy")
    public String getUserMovieBuy(@RequestParam int id) {
        return "userMovieBuy";
    }

    @ApiOperation(value = "选择电影")
    @GetMapping("/user/movie")
    public String getUserMovie() {
        return "userNewMovie";
    }

    @ApiOperation(value = "购买会员卡")
    @GetMapping("/user/member")
    public String getUserMember() {
        return "userNewMember";
    }
}
