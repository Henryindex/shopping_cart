package com.henry.dao;

import com.henry.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE id = #{userId}")
    User selectById(Integer userId);

    @Select("SELECT * FROM user WHERE username=#{username} AND password=#{password}")
    User selectByUsernameAndPassword(String username, String password);

    @Update("UPDATE user SET username=#{username}, password=#{password}, cart_id=#{cartId}, " +
            "name=#{name}, phone=#{phone}, email=#{email} " +
            "WHERE id=#{id}")
    int updateUser(User user);

    @Select("SELECT * FROM user WHERE username=#{username}")
    User selectByUsername(String username);

    @Insert("INSERT INTO user(name, username, password, email, phone) " +
            "VALUES(#{name}, #{username}, #{password}, #{email}, #{phone})")
    int insert(User user);

    @Select("SELECT * FROM user WHERE email=#{email}")
    User selectByEmail(String email);
}
