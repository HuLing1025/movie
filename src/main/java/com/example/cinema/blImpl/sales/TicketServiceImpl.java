package com.example.cinema.blImpl.sales;

import com.example.cinema.bl.sales.TicketService;
import com.example.cinema.blImpl.management.hall.HallServiceForBl;
import com.example.cinema.blImpl.management.schedule.MovieServiceForBl;
import com.example.cinema.blImpl.management.schedule.ScheduleServiceForBl;
import com.example.cinema.blImpl.promotion.ActivityServiceForBl;
import com.example.cinema.blImpl.promotion.CouponServiceForBl;
import com.example.cinema.blImpl.promotion.VIPServiceForBl;
import com.example.cinema.data.management.MovieMapper;
import com.example.cinema.data.promotion.CouponMapper;
import com.example.cinema.data.promotion.VIPCardMapper;
import com.example.cinema.data.sales.TicketMapper;
import com.example.cinema.po.*;
import com.example.cinema.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liying on 2019/4/16.
 * Updated by zhihao li on 2019/5/18.
 * Updated by jiayi chen on 2019/5/19.
 */
@Service
public class TicketServiceImpl implements TicketService, TicketServiceForBL {

    @Autowired
    TicketMapper ticketMapper;
    @Autowired
    MovieServiceForBl movieService;
    @Autowired
    CouponServiceForBl couponServiceForBl;
    @Autowired
    VIPCardMapper vipCardMapper;
    @Autowired
    ScheduleServiceForBl scheduleService;
    @Autowired
    HallServiceForBl hallService;
    @Autowired
    CouponServiceForBl couponService;
    @Autowired
    ActivityServiceForBl activityService;
    @Autowired
    VIPServiceForBl vipService;
    @Autowired
    ConsumptionServiceForBL consumptionService;

    @Override
    @Transactional
    public ResponseVO addTicket(TicketForm ticketForm) {
        try {
            TicketWithCouponVO ticketVO = new TicketWithCouponVO();
            List<TicketVO> ticketList = new ArrayList<>();
            double total = 0.0;
            List<CouponVO> couponList;
            List<ActivityVO> activityList;

            int scheduleId = ticketForm.getScheduleId();
            double price = scheduleService.getScheduleItemById(scheduleId).getFare();
            int userId = ticketForm.getUserId();

            for (SeatForm seat : ticketForm.getSeats()) {
                Ticket ticket = new Ticket();
                ticket.setUserId(userId);
                ticket.setScheduleId(scheduleId);
                ticket.setColumnIndex(seat.getColumnIndex());
                ticket.setRowIndex(seat.getRowIndex());
                ticket.setState(0);
                ticket.setCouponId(0);
                ticketMapper.insertTicket(ticket);
                ticketList.add(ticket.getVO());
                total += price;
            }
            couponList = couponService.getCouponByUserAndAmount(userId, total);
            activityList = activityService.selectActivitiesByMovie(scheduleService.getScheduleItemById(scheduleId).getMovieId());
            VIPCard vipCard = vipService.getVIPCardByUserId(userId);
            double discount;
            try {
                String cardType = vipCard.getType();
                discount = vipCardMapper.getDiscountByCardType(cardType);
            } catch (Exception e) {
                discount = 1;
            }
            ticketVO.setTotal(total * discount);
            ticketVO.setTicketVOList(ticketList);
            ticketVO.setCoupons(couponList);
            ticketVO.setActivities(activityList);

            return ResponseVO.buildSuccess(ticketVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("??????");
        }
    }

    @Override
    @Transactional
    public ResponseVO completeTicket(OrderForm orderForm) {
        try {
            List<Integer> id = orderForm.getId();
            double total = orderForm.getTotal();
            int couponId = orderForm.getCouponId();
            int userId = orderForm.getUserId();
            for (int i : id) ticketMapper.updateTicketState(i, 1);
            int movieId = scheduleService.getScheduleItemById(ticketMapper.selectTicketById(id.get(0)).getScheduleId()).getMovieId();
            List<ActivityVO> activities = activityService.selectActivitiesByMovie(movieId);
            for (ActivityVO activity : activities) {
                Coupon coupon = activity.getCoupon();
                couponService.insertCouponUser(coupon.getId(), userId);
            }
            double discountAmount = 0.0;
            if (couponId != 0)
                discountAmount = couponService.getCouponById(couponId).getDiscountAmount();
            if (couponId != 0)
                couponService.deleteCouponUser(couponId, userId);
            //??????????????????
            consumptionService.insertConsumptionItem(userId, total, discountAmount, 1, id, couponId);
            if (couponId != 0) {
                for (int i = 0; i < id.size(); i++) {
                    ticketMapper.addCouponId(id.get(i), couponId);
                }
                couponService.deleteCouponUser(couponId, userId);
            }

            return ResponseVO.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("??????");
        }
    }

    @Override
    public ResponseVO getByScheduleAndUserId(int scheduleId, int userId) {
        try {
            List<Ticket> tickets = ticketMapper.selectTicketsBySchedule(scheduleId);
            ScheduleItem schedule = scheduleService.getScheduleItemById(scheduleId);
            Hall hall = hallService.getHallById(schedule.getHallId());
            int[][] seats = hall.getSeatsLayout();
            tickets.stream().forEach(ticket -> {
                //2?????????????????????3????????????????????????
                if(ticket.getUserId()==userId&&ticket.getState()==0){
                    //?????????????????????????????????????????????????????????????????????????????????
                    ticketMapper.deleteTicket(ticket.getId());
                    seats[ticket.getRowIndex()][ticket.getColumnIndex()] = 3;//??????????????????????????????????????????
                }
                else{
                    seats[ticket.getRowIndex()][ticket.getColumnIndex()] = 2;//??????????????????
                }
            });
            ScheduleWithSeatVO scheduleWithSeatVO = new ScheduleWithSeatVO();
            scheduleWithSeatVO.setScheduleItem(schedule);
            scheduleWithSeatVO.setSeats(seats);
            return ResponseVO.buildSuccess(scheduleWithSeatVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("??????");
        }
    }

    @Override
    public ResponseVO getTicketByUser(int userId) {
        try {
            List<Ticket> tickets = ticketMapper.selectTicketByUser(userId);
            for (int i = 0; i < tickets.size(); i++) {
                Movie movie = movieService.getMovieById(scheduleService.getScheduleItemById(tickets.get(i).getScheduleId()).getMovieId());
                tickets.get(i).setMovie(movie);
            }
            List<TicketWithScheduleVO> ticketVOList = new ArrayList<>();
            List<TicketsInOnePaymentVO> paymentsVOList = new ArrayList<>();
            ArrayList timeList = new ArrayList<>();
            tickets.stream().forEach(ticket -> {
                if (ticket.getState() == 1) {
                    TicketWithScheduleVO ticketWithScheduleVO = new TicketWithScheduleVO(ticket, scheduleService.getScheduleItemById(ticket.getScheduleId()));
                    ticketWithScheduleVO.setTime(ticket.getTime());
                    ticketWithScheduleVO.setMovie(ticket.getMovie());
                    ticketVOList.add(ticketWithScheduleVO);
                }
            });
            for (int i = 0; i < ticketVOList.size(); i++) {
                String currentSeat = String.valueOf(ticketVOList.get(i).getRowIndex() + 1) + "???" + String.valueOf(ticketVOList.get(i).getColumnIndex() + 1) + "???";
                if (!timeList.contains(ticketVOList.get(i).getTime())) {
                    timeList.add(ticketVOList.get(i).getTime());
                    TicketsInOnePaymentVO newPayment = new TicketsInOnePaymentVO();
                    newPayment.setId(ticketVOList.get(i).getId());
                    newPayment.setSchedule(ticketVOList.get(i).getSchedule());
                    newPayment.setState(ticketVOList.get(i).getState());
                    newPayment.setTime(ticketVOList.get(i).getTime());
                    newPayment.setUserId(ticketVOList.get(i).getUserId());
                    newPayment.setSeatsStr(currentSeat);
                    newPayment.setMovie(ticketVOList.get(i).getMovie());

                    List<Integer> idList = new ArrayList<>();
                    idList.add(ticketVOList.get(i).getId());
                    newPayment.setTicketIds(idList);
                    paymentsVOList.add(newPayment);
                } else {
                    int index = timeList.indexOf(ticketVOList.get(i).getTime());
                    paymentsVOList.get(index).setSeatsStr(paymentsVOList.get(index).getSeatsStr() + "   " + currentSeat);
                    List<Integer> idList = paymentsVOList.get(index).getTicketIds();
                    idList.add(ticketVOList.get(i).getId());
                    paymentsVOList.get(index).setTicketIds(idList);
                }
            }
            return ResponseVO.buildSuccess(paymentsVOList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("??????");
        }
    }

    @Override
    @Transactional
    public ResponseVO completeByVIPCard(OrderForm orderForm) {
        try {
            List<Integer> id = orderForm.getId();
            int couponId = orderForm.getCouponId();
            int userId = orderForm.getUserId();
            double total = orderForm.getTotal();
            if (total > vipService.getBalance(userId))
                return ResponseVO.buildFailure("????????????");
            double discountAmount = 0.0;
            if (couponId != 0)
                discountAmount = couponService.getCouponById(couponId).getDiscountAmount();
            //???????????????????????????
            consumptionService.insertConsumptionItem(userId, total, discountAmount, 0, id, couponId);
            total = total - discountAmount;
            if (vipService.updateVIPCardBalance(userId, total)) {
                for (int i : id) ticketMapper.updateTicketState(i, 1);
                int movieId = scheduleService.getScheduleItemById(ticketMapper.selectTicketById(id.get(0)).getScheduleId()).getMovieId();
                List<ActivityVO> activities = activityService.selectActivitiesByMovie(movieId);
                for (ActivityVO activity : activities) {
                    Coupon coupon = activity.getCoupon();
                    couponService.insertCouponUser(coupon.getId(), userId);
                }
                if (couponId != 0) {
                    for (int i = 0; i < id.size(); i++) {
                        ticketMapper.addCouponId(id.get(i), couponId);
                    }
                    couponService.deleteCouponUser(couponId, userId);
                }
                return ResponseVO.buildSuccess();
            } else
                return ResponseVO.buildFailure("??????");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("??????");
        }
    }

    @Override
    public ResponseVO cancelAllTickets(String ticketIdStr) {
        try {
            String[] temp = ticketIdStr.split(",");
            List<Integer> id = new ArrayList<>();
            for (int i = 0; i < temp.length; i ++) {
                id.add(Integer.parseInt(temp[i]));
            }

            //????????????????????????
            List<Ticket> tickets = new ArrayList<>();
            for (int i = 0; i < id.size(); i++) {
                tickets.add(ticketMapper.selectTicketById(id.get(i)));
            }
            double orderFare = 0.0;
            Ticket singleTicket = tickets.get(0);
            ScheduleItem scheduleItem = scheduleService.getScheduleItemById(singleTicket.getScheduleId());
            orderFare += id.size() * scheduleItem.getFare();
            int couponId = singleTicket.getCouponId();
            if (couponId != 0) {
                orderFare -= couponService.getCouponById(singleTicket.getCouponId()).getDiscountAmount();
            }
            System.out.println("fare:" + orderFare);

            //???????????????????????????coupon?????????
            long movieTime = scheduleService.getScheduleItemById(tickets.get(0).getScheduleId()).getStartTime().getTime();
            long currentTime = System.currentTimeMillis();
            System.out.println(movieTime);
            System.out.println(currentTime);
            double timeInterval = (movieTime - currentTime) / 3600000;
            // ??????m,100%?????????n,0%??????????????????????????????????????????????????? m ??? n ???????????????????????????
            int m = ticketMapper.getAllRefundTime();
            int n = ticketMapper.getPartRefundTime();
            if (timeInterval < m && timeInterval > n) {
                orderFare *= (1.0 / (m - n)) * (timeInterval - n);
            }
            if (timeInterval <= n) {
                orderFare = 0;
            }
            if (orderFare != 0) {
                orderFare = Double.parseDouble(String.format("%.2f", orderFare));
                Coupon paybackCoupon = new Coupon();
                paybackCoupon.setName("????????????");
                paybackCoupon.setDescription("????????????");
                paybackCoupon.setStartTime(new Timestamp(System.currentTimeMillis()));
                paybackCoupon.setEndTime(Timestamp.valueOf("2029-12-31 11:59:59"));
                paybackCoupon.setTargetAmount(orderFare);
                paybackCoupon.setDiscountAmount(orderFare);
                couponServiceForBl.insertCoupon(paybackCoupon);
                couponService.insertCouponUser(paybackCoupon.getId(), singleTicket.getUserId());
            }

            //???????????????????????????
            for (int i = 0; i < tickets.size(); i++) {
                ticketMapper.updateTicketState(tickets.get(i).getId(), 2);
            }
            return ResponseVO.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("??????");
        }

    }

    @Override
    public ResponseVO getTicketsByOrderTime(int userId, String orderTime) {
        try {
//            System.out.println(orderTime);
//            2019-06-02 12:19:03.0  ???  2019-06-02T12:19:03:000+0800
//            System.out.println(orderTime);
            String orderTimeFormat = orderTime.replace("T", " ").substring(0, 21);
            List<Ticket> tickets = ticketMapper.selectTicketByUser(userId);
            List<TicketWithScheduleVO> ticketVOList = new ArrayList<>();
            tickets.stream().forEach(ticket -> {
//                System.out.println(ticket.getTime().toString());
                if (ticket.getTime().toString().equals(orderTimeFormat)) {
                    ticketVOList.add(new TicketWithScheduleVO(ticket, scheduleService.getScheduleItemById(ticket.getScheduleId())));
                }
            });
            return ResponseVO.buildSuccess(ticketVOList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("??????");
        }
    }


    //??????????????????
    @Override
    public ResponseVO setAllRefundTime(RefundTimeForm refundTimeForm) {
        try {
            if(refundTimeForm.getHour() < 0){
                return ResponseVO.buildFailure("??????????????????0");
            }
            ticketMapper.setAllRefundTime(refundTimeForm.getHour());
            return ResponseVO.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("error");
        }
    }

    @Override
    public ResponseVO setPartRefundTime(RefundTimeForm refundTimeForm) {
        try {
            if(refundTimeForm.getHour() < 0){
                return ResponseVO.buildFailure("??????????????????0");
            }
            ticketMapper.setPartRefundTime(refundTimeForm.getHour());
            return ResponseVO.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("error");
        }
    }

    @Override
    public Ticket getTicketById(int i) {
        return ticketMapper.selectTicketById(i);
    }

    @Override
    public ResponseVO getAllRefundTime() {
        try {
            return ResponseVO.buildSuccess(ticketMapper.getAllRefundTime());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("error");
        }
    }

    @Override
    public ResponseVO getPartRefundTime() {
        try {
            return ResponseVO.buildSuccess(ticketMapper.getPartRefundTime());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("error");
        }
    }



    //?????????????????????
    private List<TicketVO> ticketListToTicketVOList(List<Ticket> ticketList) {
        List<TicketVO> ticketVOList = new ArrayList<>();
        for (Ticket ticket : ticketList) {
            ticketVOList.add(ticket.getVO());
        }

        return ticketVOList;
    }


}
