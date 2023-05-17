package com.example.btl_web.controller.admin.api;

import com.example.btl_web.configuration.ServiceConfiguration;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.service.UserService;
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
@WebServlet(urlPatterns = Admin.USER_API)
public class UserApi extends HttpServlet {
    private UserService userService = ServiceConfiguration.getUserService();

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
        UserDto user = HttpUtils.of(req.getReader()).toModel(UserDto.class);

        String errors[] = new String[4];
        boolean validStatus = userService.validUpdate(user, errors);
        ObjectMapper mapper = new ObjectMapper();

        if(validStatus)
        {
            Long status = userService.updateUser(user);
            if(status != null)
            {
                resp.getOutputStream().write(mapper.writeValueAsBytes(Collections.singletonMap("messages", "Cập nhật thành công!")));
                return;
            }
        }
        resp.getOutputStream().write(mapper.writeValueAsBytes(Collections.singletonMap("errors", errors)));
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
