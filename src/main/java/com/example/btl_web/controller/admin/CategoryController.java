package com.example.btl_web.controller.admin;

import com.example.btl_web.configuration.ServiceConfiguration;
import com.example.btl_web.dto.CategoryDto;
import com.example.btl_web.paging.PageRequest;
import com.example.btl_web.paging.Pageable;
import com.example.btl_web.service.CategoryService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.btl_web.constant.Constant.*;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = Admin.CATEGORIES_PAGE)
public class CategoryController extends HttpServlet {
    private CategoryService categoryService = ServiceConfiguration.getCategoryService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CategoryService categoryService = ServiceConfiguration.getCategoryService();

        StringBuilder pageUrl = new StringBuilder(Admin.CATEGORIES_PAGE + "?");

        CategoryDto searchDto = null;
        String searchName = req.getParameter("search-category");
        if(searchName != null)
        {
            searchDto = new CategoryDto();
            searchDto.setName(searchName);
            pageUrl.append("search-category=" + searchName + "&");
        }
        pageUrl.append("page=");

        long totalCategories = categoryService.countCategories(searchDto);
        Pageable pageable = new PageRequest(req.getParameterMap(), totalCategories );
        req.setAttribute("pageable", pageable);
        List<CategoryDto> categoryDtos = categoryService.findAll(pageable, searchDto);

        req.setAttribute("list", categoryDtos);
        req.setAttribute("categories_page", pageUrl.toString());
        req.setAttribute("blogs_page", Admin.BLOGS_PAGE);
        req.setAttribute("users_page", Admin.USERS_PAGE);
        req.setAttribute("keySearch", searchName);

        RequestDispatcher rd = req.getRequestDispatcher(Admin.CATEGORIES_JSP);
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
