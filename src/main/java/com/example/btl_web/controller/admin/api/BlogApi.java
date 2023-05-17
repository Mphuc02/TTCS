package com.example.btl_web.controller.admin.api;

import com.example.btl_web.configuration.ServiceConfiguration;
import com.example.btl_web.constant.Constant;
import com.example.btl_web.constant.Constant.*;
import com.example.btl_web.dto.BlogDto;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.service.BlogService;
import com.example.btl_web.utils.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

@WebServlet(urlPatterns = Admin.BLOGS_API)
public class BlogApi extends HttpServlet {
    BlogService blogService = ServiceConfiguration.getBlogService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        solveApi(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        solveApi(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        solveApi(req, resp);
    }

    private void solveApi(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        UserDto user = (UserDto) req.getAttribute(Constant.USER_MODEL);

        BlogDto blog = HttpUtils.of(req.getReader()).toModel(BlogDto.class);
        blog.setUser(user);
        String[] errors = new String[4];

        String requestMethod = req.getMethod();
        boolean valid = false;
        if(requestMethod.equals(Request.POST_METHOD))
            valid = blogService.validCreateBlog(errors, blog);
        else if(requestMethod.equals(Request.PUT_METHOD) || requestMethod.equals(Request.DELETE_METHOD))
            valid = blogService.validUpdateBlog(errors, blog, user.getUserId() );

        ObjectMapper mapper = new ObjectMapper();

        if(valid)
        {
            blog.setUser(user);
            String message = "";
            Long blogId = null;
            if(requestMethod.equals(Request.POST_METHOD))
            {
                blogId = blogService.save(blog);
                message = "Tạo bài viết thaành công!";
            }
            else if(requestMethod.equals(Request.PUT_METHOD) || requestMethod.equals(Request.DELETE_METHOD))
            {
                blogId = blogService.update(blog);
                message = "Chỉnh sửa bài viết thành công!";
            }
            if(blogId != null)
            {
                resp.getOutputStream().write(mapper.writeValueAsBytes(Collections.singletonMap("messages", message)));
                return;
            }
        }
        resp.getOutputStream().write(mapper.writeValueAsBytes(Collections.singletonMap("errors", errors)));
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
