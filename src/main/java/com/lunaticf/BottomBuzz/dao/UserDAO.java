package com.lunaticf.BottomBuzz.dao;

import com.lunaticf.BottomBuzz.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserDAO {
    String TABLE_NAME = "user";
    String INSERT_FIELDS = " name, password, salt, head_url ";
    String SELECT_FILEDS = " id, name, password, salt, head_url ";

    @Insert({"insert into ",TABLE_NAME, "(", INSERT_FIELDS, ") values (#{name}, #{password}, #{salt}, #{headUrl})"})
    int addUser(User user);

    @Select({"select ", SELECT_FILEDS, " from ", TABLE_NAME, "where id = #{id}"})
    User getUser(@Param("id") int id);

    @Select({"select ", SELECT_FILEDS, " from ", TABLE_NAME, "where name = #{name}"})
    User getUserByName(@Param("name") String name);

    @Update({"update ", TABLE_NAME, " set password = #{password} where id = #{id}"})
    void updatePassword(User user);
}
