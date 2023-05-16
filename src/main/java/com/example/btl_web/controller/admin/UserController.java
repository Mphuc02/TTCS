package com.example.btl_web.controller.admin;

import com.example.btl_web.dto.UserDto;
import com.example.btl_web.paging.PageRequest;
import com.example.btl_web.paging.Pageable;
import com.example.btl_web.service.UserService;
import com.example.btl_web.service.impl.UserServiceimpl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import com.example.btl_web.constant.Constant.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = Admin.USERS_PAGE)
public class UserController extends HttpServlet {
    private UserService userService = UserServiceimpl.getInstance();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long totalItem = userService.countUsers();
        Pageable pageable = new PageRequest(req.getParameterMap(), totalItem);

        List<UserDto> dtos = userService.findAll(pageable, null);

        StringBuilder pageUrl = new StringBuilder(Admin.USERS_PAGE);
        pageUrl.append("?page=");

        req.setAttribute("users_list", dtos);
        req.setAttribute("pageable", pageable);
        req.setAttribute("categories_page", Admin.CATEGORIES_PAGE);
        req.setAttribute("blogs_page", Admin.BLOGS_PAGE);
        req.setAttribute("users_page", pageUrl.toString());

        RequestDispatcher rd = req.getRequestDispatcher(Admin.USERS_JSP);
        rd.forward(req,resp);
    }
}
