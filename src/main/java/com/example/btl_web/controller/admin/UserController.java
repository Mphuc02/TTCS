package com.example.btl_web.controller.admin;

import com.example.btl_web.configuration.ServiceConfiguration;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.paging.PageRequest;
import com.example.btl_web.paging.Pageable;
import com.example.btl_web.service.UserService;
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
    private UserService userService = ServiceConfiguration.getUserService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService userService = ServiceConfiguration.getUserService();

        StringBuilder pageUrl = new StringBuilder(Admin.USERS_PAGE + "?");

        UserDto searchDto = null;

        String searchName = req.getParameter("searchName");
        if(searchName != null)
        {
            searchDto = new UserDto();
            searchDto.setFullName(searchName);
            pageUrl.append("searchName=" + searchName + "&");
        }
        pageUrl.append("page=");

        long totalItem = userService.countUsers(searchDto);
        Pageable pageable = new PageRequest(req.getParameterMap(), totalItem);

        List<UserDto> dtos = userService.findAll(pageable, searchDto);

        req.setAttribute("users_list", dtos);
        req.setAttribute("pageable", pageable);
        req.setAttribute("categories_page", Admin.CATEGORIES_PAGE);
        req.setAttribute("blogs_page", Admin.BLOGS_PAGE);
        req.setAttribute("users_page", pageUrl.toString());
        req.setAttribute("searchName", searchName);

        RequestDispatcher rd = req.getRequestDispatcher(Admin.USERS_JSP);
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
