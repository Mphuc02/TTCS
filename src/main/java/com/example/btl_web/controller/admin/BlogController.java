package com.example.btl_web.controller.admin;

import com.example.btl_web.configuration.ServiceConfiguration;
import com.example.btl_web.constant.Constant.*;
import com.example.btl_web.dto.BlogDto;
import com.example.btl_web.paging.PageRequest;
import com.example.btl_web.paging.Pageable;
import com.example.btl_web.service.BlogService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = Admin.BLOGS_PAGE)
public class BlogController extends HttpServlet {
    private BlogService blogService = ServiceConfiguration.getBlogService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BlogService blogService = ServiceConfiguration.getBlogService();

        StringBuilder pageUrl = new StringBuilder(Admin.BLOGS_PAGE + "?");

        BlogDto searchDto = null;
        String searchName = req.getParameter("keySearch");
        if(searchName != null)
        {
            searchDto = new BlogDto();
            searchDto.setTitle(searchName);
            pageUrl.append("keySearch=" + searchName + "&");
        }
        pageUrl.append("page=");

        long totalBlog = blogService.countBlogs(searchDto);
        Pageable pageable = new PageRequest(req.getParameterMap(), totalBlog);

        List<BlogDto> blogList = blogService.getAllBlogs(pageable, searchDto);

        //Tìm lượt thích cho mỗi bài viết
        for(BlogDto blog: blogList)
        {
            blog.setLikedUsers(blogService.peopleLikedBlog(blog.getBlogId()));
        }

        req.setAttribute("pageable", pageable);
        req.setAttribute("blogList", blogList);
        req.setAttribute("categories_page", Admin.CATEGORIES_PAGE);
        req.setAttribute("blogs_page", pageUrl.toString());
        req.setAttribute("users_page", Admin.USERS_PAGE);
        req.setAttribute("keySearch", searchName);

        RequestDispatcher rd = req.getRequestDispatcher(Admin.BLOGS_JSP);
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
