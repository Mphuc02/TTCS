package com.example.btl_web.controller;

import com.example.btl_web.constant.Constant;
import com.example.btl_web.constant.Constant.*;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.service.UserService;
import com.example.btl_web.service.impl.UserServiceimpl;
import com.example.btl_web.utils.JwtUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet {
    private UserService userService = UserServiceimpl.getInstance();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher(Constant.LOGIN_JSP);
        String messageType = "alert";

        String action = req.getParameter(User.ACTION);
        if(action != null)
        {
            if(action.equals(User.ACTION_NOT_LOGIN))
            {
                req.setAttribute("message", "Bạn chưa đăng nhập!");
            }
            else if (action.equals(User.ACTION_NOT_PERMISSION))
            {
                req.setAttribute("message", "Bạn không có quyền truy cập địa chỉ này!");
            }
            else if(action.equals(User.ACTION_LOG_OUT))
            {
                req.setAttribute("message", "Đăng xuất thành công!");
                //Thực hiện xoá cookie này
                JwtUtils.getInstance().removeAToken(resp);
            }
            if(action.equals("sign-up-success"))
            {
                req.setAttribute("message", "Đăng ký thành công, vui lòng đăng nhập để tiếp tục");
                messageType = "notice";
            }
            req.setAttribute("display_flex", "display__flex");
            req.setAttribute("message_type", messageType);
        }

        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("userName");
        String passWord = req.getParameter("passWord");

        UserDto userDto = userService.login(userName, passWord);

        if(userDto != null )
        {
            if(userDto.getStatus() == 1)
            {
                //SessionUtils session = SessionUtils.getInstance();
                //session.putValue(req, Constant.USER_MODEL, userDto);

                JwtUtils.getInstance().addAToken(resp, userDto);

                if(userDto.getRole().equals(Constant.USER))
                {
                    resp.sendRedirect(req.getContextPath() + "/");
                    return;
                }
                else if(userDto.getRole().equals(Constant.ADMIN))
                {
                    resp.sendRedirect(req.getContextPath() + "/");
                    return;
                }
            }
            else
            {
                req.setAttribute("message", "Tài khoản này đã bị khoá! Vui lòng liên hệ admin");
            }
        }
        else
        {
            req.setAttribute("message", "Tài khoản mật khẩu không chính xác!");
        }
        RequestDispatcher rd = req.getRequestDispatcher(Constant.LOGIN_JSP);
        req.setAttribute("display_flex", "display__flex");
        req.setAttribute("message_type", "alert");
        rd.forward(req, resp);
    }
}
