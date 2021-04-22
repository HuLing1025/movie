package com.example.cinema.controller.management;

import com.example.cinema.bl.statistics.MovieLikeService;
import com.example.cinema.vo.MovieBatchOffForm;
import com.example.cinema.vo.MovieForm;
import com.example.cinema.bl.management.MovieService;
import com.example.cinema.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "management接口")
@RestController
public class MovieController {

    @Autowired
    private MovieService movieService;
    @Autowired
    private MovieLikeService movieLikeService;

    @ApiOperation(value = "添加电影")
    @RequestMapping(value = "/movie/add", method = RequestMethod.POST)
    public ResponseVO addMovie(@RequestBody MovieForm addMovieForm){
        return movieService.addMovie(addMovieForm);
    }

    @ApiOperation(value = "ID和UID搜索电影")
    @RequestMapping(value = "/movie/{id}/{userId}", method = RequestMethod.GET)
    public ResponseVO searchOneMovieByIdAndUserId(@PathVariable int id, @PathVariable int userId){
        return movieService.searchOneMovieByIdAndUserId(id, userId);
    }

    @ApiOperation(value = "电影列表(全部)")
    @RequestMapping(value = "/movie/all", method = RequestMethod.GET)
    public ResponseVO searchAllMovie(){
        //返回结果中包括已经下架的电影
        return movieService.searchAllMovie();
    }

    @ApiOperation(value = "电影列表(在映中)")
    @RequestMapping(value = "/movie/all/exclude/off", method = RequestMethod.GET)
    public ResponseVO searchOtherMoviesExcludeOff(){
        //返回结果中不包括已经下架的电影
        return movieService.searchOtherMoviesExcludeOff();
    }

    @ApiOperation(value = "想看电影")
    @RequestMapping(value = "/movie/{movieId}/like", method = RequestMethod.POST)
    public ResponseVO likeMovie(@PathVariable int movieId,@RequestParam int userId){
        return movieLikeService.likeMovie(userId,movieId);
    }

    @ApiOperation(value = "取消想看电影")
    @RequestMapping(value = "/movie/{movieId}/unlike", method = RequestMethod.POST)
    public ResponseVO unlikeMovie(@PathVariable int movieId,@RequestParam int userId){
        return movieLikeService.unLikeMovie(userId,movieId);
    }

    @ApiOperation(value = "想看电影人数")
    @RequestMapping(value = "/movie/{movieId}/like/count", method = RequestMethod.GET)
    public ResponseVO getMovieLikeCounts(@PathVariable int movieId){
        return movieLikeService.getCountOfLikes(movieId);
    }

    @ApiOperation(value = "想看电影人数(每日)")
    @RequestMapping(value = "/movie/{movieId}/like/date", method = RequestMethod.GET)
    public ResponseVO getMovieLikeCountByDate(@PathVariable int movieId){
        return movieLikeService.getLikeNumsGroupByDate(movieId);
    }

    @ApiOperation(value = "关键词搜索电影")
    @RequestMapping(value = "/movie/search",method = RequestMethod.GET)
    public ResponseVO getMovieByKeyword(@RequestParam String keyword){
        return movieService.getMovieByKeyword(keyword);
    }

    @ApiOperation(value = "批量下架电影")
    @RequestMapping(value = "/movie/off/batch",method = RequestMethod.POST)
    public ResponseVO pullOfBatchOfMovie(@RequestBody MovieBatchOffForm movieBatchOffForm){
        return movieService.pullOfBatchOfMovie(movieBatchOffForm);
    }

    @ApiOperation(value = "更新电影")
    @RequestMapping(value = "/movie/update",method = RequestMethod.POST)
    public ResponseVO updateMovie(@RequestBody MovieForm updateMovieForm){
        return movieService.updateMovie(updateMovieForm);
    }
}
