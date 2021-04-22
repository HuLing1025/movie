package com.example.cinema.vo;

import java.sql.Timestamp;

/**
 * @author zhihao li
 * @date 2019/5/18 12:15 AM
 */
public class CouponVO {
    /**
     * 优惠券id
     */
    private int id;
    /**
     * 优惠券描述
     */
    private String description;
    /**
     * 优惠券名称
     */
    private String name;
    /**
     * 优惠券使用门槛
     */
    private double targetAmount;
    /**
     * 优惠券优惠金额
     */
    private double discountAmount;
    /**
     * 可用时间
     */
    private Timestamp startTime;
    /**
     * 失效时间
     */
    private Timestamp endTime;

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
}
