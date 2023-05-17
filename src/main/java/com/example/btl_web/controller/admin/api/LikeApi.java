package com.example.btl_web.controller.admin.api;

import com.example.btl_web.configuration.ServiceConfiguration;
import com.example.btl_web.constant.Constant;
import com.example.btl_web.constant.Constant.*;
import com.example.btl_web.dto.BlogDto;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.service.UserBlogService;
import com.example.btl_web.service.UserService;
import com.example.btl_web.utils.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

@WebServlet(urlPatterns = User.USER_LIKE_API)
public class LikeApi extends HttpServlet {
    private UserService userService = ServiceConfiguration.getUserService();
    private UserBlogService userBlogService = ServiceConfiguration.getUserBlogService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        UserDto user = (UserDto) req.getAttribute(Constant.USER_MODEL);


        ObjectMapper mapper = new ObjectMapper();
        String[] errors = new String[1];
        String timevalid = userService.checkLastAction(user.getUserId());
        if(timevalid == null)
        {
            BlogDto likedBlog = HttpUtils.of(req.getReader()).toModel(BlogDto.class);

            boolean statusLiked = userBlogService.likeThisBlog(likedBlog.getBlogId(), user.getUserId());

            if(statusLiked)
            {
                resp.getOutputStream().write(mapper.writeValueAsBytes(Collections.singletonMap("message", "Bạn đã thích bài viết này!")));
                return;
            }
            else
            {
                resp.getOutputStream().write(mapper.writeValueAsBytes(Collections.singletonMap("errors", "Bạn phải đăng nhập thì mới có thể like bài viết này!")));
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }
        errors[0] = timevalid;
        resp.getOutputStream().write(mapper.writeValueAsBytes(Collections.singletonMap("errors", errors)));
        resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        BlogDto likedBlog = HttpUtils.of(req.getReader()).toModel(BlogDto.class);

        UserDto user = (UserDto) req.getAttribute(Constant.USER_MODEL);
        boolean statusLiked = userBlogService.removeLikeThisBlog(likedBlog.getBlogId(), user.getUserId());

        ObjectMapper mapper = new ObjectMapper();
        if(statusLiked)
        {
            resp.getOutputStream().write(mapper.writeValueAsBytes(Collections.singletonMap("message", "Đã bỏ thích bài viết này!")));
        }
        else
        {
            resp.getOutputStream().write(mapper.writeValueAsBytes(Collections.singletonMap("errors", "Bạn phải đăng nhập thì mới có thể like bài viết này!")));
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
