package com.example.cinema.controller.sales;

import com.example.cinema.bl.sales.TicketService;
import com.example.cinema.vo.OrderForm;
import com.example.cinema.vo.RefundTimeForm;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.TicketForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@Api(tags = "sales")
@RestController
@RequestMapping("/ticket")
public class TicketController {
    @Autowired
    TicketService ticketService;

    @ApiOperation(value = "完成购票(会员卡)")
    @PostMapping("/vip/buy")
    public ResponseVO buyTicketByVIPCard(@RequestBody OrderForm orderForm) {
        return ticketService.completeByVIPCard(orderForm);
    }

    @ApiOperation(value = "购票开始(占座)")
    @PostMapping("/lockSeat")
    public ResponseVO lockSeat(@RequestBody TicketForm ticketForm) {
        return ticketService.addTicket(ticketForm);
    }

    @ApiOperation(value = "完成购票(不使用会员卡)")
    @PostMapping("/buy")
    public ResponseVO buyTicket(@RequestBody OrderForm orderForm) {
        return ticketService.completeTicket(orderForm);
    }

    @ApiOperation(value = "获取电影票详情")
    @GetMapping("/get/{userId}")
    public ResponseVO getTicketByUserId(@PathVariable int userId) {
        return ticketService.getTicketByUser(userId);
    }

    @ApiOperation(value = "根据时间获取电影票详情")
    @GetMapping("/buy/payment/{userId}/{orderTime}")
    public ResponseVO getTicketsInOnePayment(@PathVariable int userId, @PathVariable String orderTime) {
        return ticketService.getTicketsByOrderTime(userId, orderTime);
    }

    @ApiOperation(value = "获得该场次的被锁座位和场次信息")
    @GetMapping("/get/occupiedSeats")
    public ResponseVO getOccupiedSeats(@RequestParam int scheduleId, @RequestParam int userId){
        return ticketService.getByScheduleAndUserId(scheduleId, userId);
    }

    @ApiOperation(value = "取消锁座（只有状态是‘锁定中’的可以取消)")
    @GetMapping("/cancel")
    public ResponseVO cancelAllTickets(@RequestParam String idStr) {
        return ticketService.cancelAllTickets(idStr);
    }

    @ApiOperation(value = "全额退款")
    @RequestMapping(value = "/refund/all/set", method = RequestMethod.POST)
    public ResponseVO setAllRefundTime(@RequestBody RefundTimeForm refundTimeForm){
        return ticketService.setAllRefundTime(refundTimeForm);
    }

    @ApiOperation(value = "获取退款策略列表")
    @RequestMapping(value = "/refund/all", method = RequestMethod.GET)
    public ResponseVO getAllRefundTime(){
        return ticketService.getAllRefundTime();
    }

    @ApiOperation(value = "部分退款")
    @RequestMapping(value = "/refund/part/set", method = RequestMethod.POST)
    public ResponseVO setPartRefundTime(@RequestBody RefundTimeForm refundTimeForm){
        return ticketService.setPartRefundTime(refundTimeForm);
    }

    @ApiOperation(value = "部分退款策略")
    @RequestMapping(value = "/refund/part", method = RequestMethod.GET)
    public ResponseVO getPartRefundTime(){
        return ticketService.getPartRefundTime();
    }



}

