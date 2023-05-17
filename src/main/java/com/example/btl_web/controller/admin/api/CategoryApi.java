package com.example.btl_web.controller.admin.api;

import com.example.btl_web.configuration.ServiceConfiguration;
import com.example.btl_web.constant.Constant;
import com.example.btl_web.dto.CategoryDto;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.service.CategoryService;
import com.example.btl_web.utils.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import com.example.btl_web.constant.Constant.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

@WebServlet(urlPatterns = Admin.CATEGORY_API)
public class CategoryApi extends HttpServlet {
    private CategoryService categoryService = ServiceConfiguration.getCategoryService();
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

        String errors[] = new String[1];

        CategoryDto category = HttpUtils.of(req.getReader()).toModel(CategoryDto.class);
        UserDto user = (UserDto) req.getAttribute(Constant.USER_MODEL);
        category.setUserId(user.getUserId());

        boolean validCategory = false;
        String successMessage = "";
        if(req.getMethod().equals(Request.POST_METHOD))
        {
            validCategory = categoryService.validCategoryCreate(category, errors, user.getUserId());
            successMessage = "Thêm thể loại thành công!";
        }
        else if(req.getMethod().equals(Request.PUT_METHOD) || req.getMethod().equals(Request.DELETE_METHOD))
        {
            validCategory = categoryService.validCategoryUpdate(category, errors, user.getUserId());
            successMessage = "Cập nhật thành công!";
        }
        ObjectMapper mapper = new ObjectMapper();
        if(validCategory) {
            Long status = null;
            if(req.getMethod().equals(Request.POST_METHOD))
            {
                status = categoryService.save(category);
            }
            else if(req.getMethod().equals(Request.PUT_METHOD) || req.getMethod().equals(Request.DELETE_METHOD))
            {
                status = categoryService.update(category);
            }
            if (status != null) {
                resp.getOutputStream().write(mapper.writeValueAsBytes(Collections.singletonMap("messages", successMessage)));
                resp.setStatus(HttpServletResponse.SC_OK);
                return;
            }
        }
        resp.getOutputStream().write(mapper.writeValueAsBytes(Collections.singletonMap("errors", errors)));
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
