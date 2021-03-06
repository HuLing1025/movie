package com.example.cinema.utils;

import java.io.File;

public class Global {
    // 静态资源存储位置
    public static final String FILEPATH = File.separator +"images";
    //public static final String FILEPATH = "E:/images/";
    // 豆瓣正在热映URL(爬虫入口地址)
    public static final String BASEURL1 = "https://movie.douban.com/cinema/nowplaying/chongqing/";
    // 豆瓣即将上映URL(爬虫入口地址)
    // public static final String BASEURL2 = "https://movie.douban.com/cinema/later/chongqing/";
    // 爬虫定时器参数(每天20点执行一次)
    public static final String CROONERS2SPIDER = "0 21 14 * * ?";
    // 放映管理定时器(每一分钟执行)
    public static final String CROONERS2ROBOT = "0 * * * * ?";
    // 钉钉机器人webhook
    public static final String WEBHOOK = "https://oapi.dingtalk.com/robot/send?access_token=cb3662947261735841724f24219db4aa17c17b39cf7958d03c58ec1f205d45e7";
}
