package com.example.btl_web.controller.admin.edit;

import com.example.btl_web.dto.CategoryDto;
import com.example.btl_web.service.CategoryService;
import com.example.btl_web.service.impl.CategoryServiceImpl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import com.example.btl_web.constant.Constant.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = Admin.CATEGORIES_PAGE + Admin.EDIT + "/*")
public class EditCategoryController extends HttpServlet {
    private CategoryService categoryService = CategoryServiceImpl.getInstance();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String categoryIdStr = req.getPathInfo().split("/")[1];
        Long categoryId = Long.parseLong(categoryIdStr);

        CategoryDto categoryDto = new CategoryDto();
        if(categoryId != -1)
        {
            categoryDto.setCategoryId(categoryId);
            categoryDto = categoryService.findOneBy(categoryDto);
        }

        req.setAttribute("category", categoryDto);
        req.setAttribute("users", Admin.USERS_PAGE);
        req.setAttribute("blogs", Admin.BLOGS_PAGE);
        req.setAttribute("categories", Admin.CATEGORIES_PAGE);

        RequestDispatcher rd = req.getRequestDispatcher(Admin.CATEGORY_EDIT_JSP);
        rd.forward(req, resp);
    }
}
