package com.example.btl_web.controller.user;

import com.example.btl_web.configuration.ServiceConfiguration;
import com.example.btl_web.constant.Constant;
import com.example.btl_web.constant.Constant.*;
import com.example.btl_web.dto.BlogDto;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.service.BlogService;
import com.example.btl_web.service.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = User.USER_DETAIL_PAGE)
public class UserDetailController extends HttpServlet {
    private BlogService blogService = ServiceConfiguration.getBlogService();
    private UserService userService = ServiceConfiguration.getUserService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userIdStr = req.getPathInfo().split("/")[1];
        if(userIdStr == null)
        {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Long userId = Long.parseLong(userIdStr);
        UserDto searchDto = userService.findOneById(userId);
        if(searchDto == null)
        {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        BlogDto blogsOfUser = new BlogDto();
        blogsOfUser.setUser((UserDto) req.getAttribute(Constant.USER_MODEL));
        List<BlogDto> listBlogsOfUser = blogService.getAllBlogs(null, blogsOfUser);
        for(BlogDto blog: listBlogsOfUser)
        {
            blog.setLikedUsers(blogService.peopleLikedBlog(blog.getBlogId()));
        }

        req.setAttribute("blogs", listBlogsOfUser);

        req.setAttribute(Constant.USER, searchDto);
        RequestDispatcher rd = req.getRequestDispatcher(Constant.User.USER_DETAIL_JSP);
        rd.forward(req, resp);
    }
}