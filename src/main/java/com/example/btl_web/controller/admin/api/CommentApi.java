package com.example.btl_web.controller.admin.api;

import com.example.btl_web.configuration.ServiceConfiguration;
import com.example.btl_web.constant.Constant;
import com.example.btl_web.dto.CommentDto;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.service.UserBlogService;
import com.example.btl_web.utils.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

@WebServlet(urlPatterns = Constant.User.USER_COMMENT_API)
public class CommentApi extends HttpServlet {
    private UserBlogService userBlogService = ServiceConfiguration.getUserBlogService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        CommentDto comment = HttpUtils.of(req.getReader()).toModel(CommentDto.class);
        UserDto userComment = (UserDto) req.getAttribute(Constant.USER_MODEL);
        comment.setUserComment(userComment);

        String errors[] = new String[2];
        boolean validComment = userBlogService.validComment(comment, errors);

        ObjectMapper mapper = new ObjectMapper();
        if(validComment)
        {
            boolean saveCommentStatus = userBlogService.saveComment(comment);
            if(saveCommentStatus)
                resp.getOutputStream().write(mapper.writeValueAsBytes(Collections.singletonMap("messages", "Comment thành công!")));
            else
            {
                resp.getOutputStream().write(mapper.writeValueAsBytes(Collections.singletonMap("errors", "Không thể gửi comment")));
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        else
        {
            resp.getOutputStream().write(mapper.writeValueAsBytes(Collections.singletonMap("errors", errors)));
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
