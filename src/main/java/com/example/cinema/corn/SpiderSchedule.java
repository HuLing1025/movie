package com.example.cinema.corn;

import com.example.cinema.bl.management.MovieService;
import com.example.cinema.utils.*;
import com.example.cinema.vo.MovieForm;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SpiderSchedule {
    @Autowired
    MovieService movieService;

    @Autowired
    DDRobot ddRobot;

    @Scheduled(cron = Global.CROONERS2SPIDER)
    private void spiderWork() throws InterruptedException {
        ddRobot.post(JsonTool.getMessJson("开始爬取: " + Global.BASEURL1));
        // 获取正在热映页面
        Document doc1 = HttpClientDownPage.sendGet(Global.BASEURL1);
        List<MovieForm> films = null;
        if(doc1 == null){
            ddRobot.post(JsonTool.getMessJson("-------主页面为空,可能原因:反爬虫机制------"));
            System.out.println("-------主页面为空,可能原因:反爬虫机制------");
        }else{
            // 页面元素过滤条件
            List<String> filters1 = new ArrayList<>();
            filters1.add("list-item");
            // 获取电影数据
            films = Analyticaldata.analytical(doc1, Global.BASEURL1, filters1);
            // 更新数(存在时)
            int update = 0;
            // 失败数
            int error = 0;
            for (MovieForm film : films) {
                if (movieService.movieExist(film) > 0) {
                    // update
                    movieService.updateMovie(film);
                    ++update;
                }else{
                    // insert
                    movieService.addMovie(film);
                }
            }
            // 发送钉钉群消息
            ddRobot.post(JsonTool.getMessJson("\n爬取总量: " + films.size() + "\n更新数: "  + update + "\n新增数: " + (films.size() - update - error)));
        }
    }
}
