package com.example.btl_web.filter;

import com.example.btl_web.constant.Constant;
import com.example.btl_web.constant.Constant.*;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.utils.JwtUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JwtRequestFilter implements Filter {
    private JwtUtils jwtUtils = JwtUtils.getInstance();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String url = request.getRequestURI();
        String jwt = getJwtFromCookie(request);
        UserDto user = jwtUtils.parseUserFromJwt(jwt);
        request.setAttribute(Constant.USER_MODEL, user);

        if(url.contains("admin"))
        {
            if(user == null)
            {
                response.sendRedirect(request.getContextPath() + "/login?action=not_login");
            }
            else
            {
                if(user.getRole().equals(Constant.ADMIN))
                {
                    filterChain.doFilter(servletRequest, servletResponse);
                }
                else
                {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        }
        else if(url.startsWith("/api"))
        {
            if(url.equals(Constant.User.USER_CREATE_API) && user == null)
            {
                filterChain.doFilter(servletRequest, servletResponse);
            }
            else
            {
                if(user == null)
                {
                    response.sendRedirect(request.getContextPath() + "/login?action=not_login");
                }
                else
                {
                    filterChain.doFilter(servletRequest, servletResponse);
                }
            }
        }
        else
        {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private String getJwtFromCookie(HttpServletRequest req)
    {
        String jwt = null;
        Cookie[] cookies = req.getCookies();
        if(cookies == null)
            return null;
        for(Cookie cookie: cookies)
        {
            if(cookie.getName().equals(Jwt.JWT_NAME))
            {
                jwt = cookie.getValue();
                break;
            }
        }

        return jwt;
    }
}
