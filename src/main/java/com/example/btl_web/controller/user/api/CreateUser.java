package com.example.btl_web.controller.user.api;

import com.example.btl_web.constant.Constant.*;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.service.UserService;
import com.example.btl_web.service.impl.UserServiceimpl;
import com.example.btl_web.utils.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

@WebServlet(urlPatterns = User.USER_CREATE_API)
public class CreateUser extends HttpServlet {
    private UserService userService = UserServiceimpl.getInstance();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        UserDto user = HttpUtils.of(req.getReader()).toModel(UserDto.class);

        String[] errors = new String[4];
        boolean valid = userService.validateSignUp(user, errors);
        ObjectMapper mapper = new ObjectMapper();
        if(!valid)
        {
            resp.getOutputStream().write(mapper.writeValueAsBytes(Collections.singletonMap("errors", errors)));
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        else
        {
            Long status = userService.saveUser(user);
            if(status != null)
                resp.getOutputStream().write(mapper.writeValueAsBytes(Collections.singletonMap("messages", "Đăng ký tài khoản thành công!")));
        }
    }
}
