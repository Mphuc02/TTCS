package com.example.btl_web.controller.admin;

import com.example.btl_web.constant.Constant.*;
import com.example.btl_web.dto.BlogDto;
import com.example.btl_web.paging.PageRequest;
import com.example.btl_web.paging.Pageable;
import com.example.btl_web.service.BlogService;
import com.example.btl_web.service.impl.BlogServiceImpl;
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
    private BlogService blogService = BlogServiceImpl.getInstance();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long totalBlog = blogService.countBlogs(null);
        Pageable pageable = new PageRequest(req.getParameterMap(), totalBlog);

        List<BlogDto> blogList = blogService.getAllBlogs(pageable, null);

        StringBuilder pageUrl = new StringBuilder(Admin.BLOGS_PAGE);
        pageUrl.append("?page=");

        req.setAttribute("pageable", pageable);
        req.setAttribute("blogList", blogList);
        req.setAttribute("categories_page", Admin.CATEGORIES_PAGE);
        req.setAttribute("blogs_page", pageUrl.toString());
        req.setAttribute("users_page", Admin.USERS_PAGE);
        RequestDispatcher rd = req.getRequestDispatcher(Admin.BLOGS_JSP);
        rd.forward(req, resp);
    }
}
