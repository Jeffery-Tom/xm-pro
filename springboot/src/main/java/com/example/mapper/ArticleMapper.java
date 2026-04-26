package com.example.mapper;

import com.example.entity.Article;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ArticleMapper {
    List<Article> selectAll(Article article);

    //简单语句可以直接在mapper里用注解写,遇到比较复杂的建议在xml里写语句
    @Select("select * from `article` where id = #{id}")
    Article selectById(Integer id);

    void insert(Article article);

    void updateById(Article article);

    @Delete("delete from `article` where id = #{id}")
    void deleteById(Integer id);
}
