package com.example.controller;

import com.example.common.Result;
import com.example.entity.Article;
import com.example.service.ArticleService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//负责接收前端传过来的数据
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;
    /*
    get:查询操作
    post:新增操作
    put:修改操作
    delete:删除操作
     */

    /*
    新增数据
     */
    @PostMapping("/add")//@RequestBody把前端传来的JSON字符串映射出JAVA对象,或者数组
    public Result add(@RequestBody Article article){
        articleService.add(article);
        return Result.success();//新增数据无需返回数据
    }

    /*
    更新数据
     */
    @PutMapping("/update")//@RequestBody把前端传来的JSON字符串映射出JAVA对象,或者数组
    public Result update(@RequestBody Article article){
        articleService.update(article);
        return Result.success();//新增数据无需返回数据
    }

    /*
    删除单个数据
     */
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id){
        articleService.deleteById(id);
        return Result.success();//新增数据无需返回数据
    }

    /*
    批量删除数据
     */
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){//需要用@RequestBody接收因为是个数组
        articleService.deleteBatch(ids);
        return Result.success();//新增数据无需返回数据
    }

    /*
    查询单个数据
 */
    @GetMapping("/selectById/{id}")//路径传参
    public Result selectById(@PathVariable Integer id){
        Article article= articleService.selectById(id);
        return Result.success(article);
    }

    //查询所有数据 同时也是定义一个接口
    @GetMapping("/selectAll")
    public Result selectAll(Article article){
        List<Article> list= articleService.selectAll(article);
    return Result.success(list);
    }

    /*
    分页数据的查询
    pageNum: 当前页码
    pageSize: 每页的个数
 */
    @GetMapping("/selectPage")//路径传参
    public Result selectPage(Article article,//这些参数是从前端传过来的
                            @RequestParam(defaultValue = "1") Integer pageNum,
                            @RequestParam(defaultValue = "10") Integer pageSize){
        //service mapper sql都走过一遍之后传回来pageInfo给前端
        PageInfo<Article> pageInfo= articleService.selectPage(article,pageNum,pageSize);
        return Result.success(pageInfo);
    }


}
