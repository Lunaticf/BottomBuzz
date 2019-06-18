package com.lunaticf.BottomBuzz.dao;


import com.lunaticf.BottomBuzz.model.News;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface NewsDAO {
    String TABLE_NAME = "news";
    String INSERT_FIELDS = " title, link, image, like_count, comment_count, created_date, user_id ";
    String SELECT_FILEDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into " + TABLE_NAME + "(" + INSERT_FIELDS + ") values (#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
    int addNews(News news);

    // select 新闻
    List<News> selectByUserIdAndOffset(@Param("userId") int userId,@Param("offset") int offset,
                                       @Param("limit") int limit);

    // select 新闻 by Id
    @Select({"select ", SELECT_FILEDS, " from  ", TABLE_NAME, " where id = #{newsId}"})
    News selectById(int newsId);

    // 更新新闻的评论数
    @Update({"update ", TABLE_NAME, " set comment_count = #{commentCount} where id = #{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);
}
