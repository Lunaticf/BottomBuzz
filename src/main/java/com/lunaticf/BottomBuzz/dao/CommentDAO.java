package com.lunaticf.BottomBuzz.dao;

import com.lunaticf.BottomBuzz.model.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentDAO {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " user_id, content, created_date, entity_id, entity_type, status ";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;


    // 插入一条评论
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
    public int addComment(Comment comment);

    // 选择一个资讯下面的所有评论
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where entity_type=#{entityType} and entity_id=#{entityId} and status = 0 order by id desc "})
    public List<Comment> getCommentsByNewsId(@Param("entityId") int entityId, @Param("entityType") int entityType);


    // 得到一个资讯的comment的数量
    @Select({"select count(id) from ", TABLE_NAME, " where entity_type=#{entityType} and entity_id=#{entityId}"})
    public int getCommentCount(@Param("entityId")int entityId, @Param("entityType") int entityType);

    @Update({"update ", TABLE_NAME , "set status = #{status} where id = #{id}"})
    public int updateCommentStatus(@Param("status") int status, @Param("id") int id);
}
