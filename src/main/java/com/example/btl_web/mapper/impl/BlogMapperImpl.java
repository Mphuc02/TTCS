package com.example.btl_web.mapper.impl;

import com.example.btl_web.dto.BlogDto;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.mapper.RowMapper;
import com.example.btl_web.model.Blog;
import com.example.btl_web.service.UserBlogService;
import com.example.btl_web.service.UserService;
import com.example.btl_web.service.impl.UserBlogServiceImpl;
import com.example.btl_web.service.impl.UserServiceimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BlogMapperImpl implements RowMapper {
    private UserService userService = UserServiceimpl.getInstance();
    private UserBlogService userBlogService = UserBlogServiceImpl.getInstance();
    @Override
    public Object mapper(ResultSet resultSet) {
        Blog blog = new Blog();

        try {
            blog.setBlogId(resultSet.getLong("blog_id"));
            blog.setTitle(resultSet.getString("title"));
            blog.setContent(resultSet.getString("content"));
            blog.setImageTitle(resultSet.getString("image_title"));
            blog.setCreatedAt(resultSet.getLong("created_at"));
            blog.setStatus(resultSet.getInt("status"));

            //Tìm người user viết blog này
            UserDto userOfBlog = new UserDto();
            userOfBlog.setUserId(resultSet.getLong("user_id"));
            List<UserDto> userDtos = userService.findAll(null, userOfBlog);
            UserDto user = userDtos.isEmpty() ? null: userDtos.get(0);
            blog.setUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return blog;
    }
}
