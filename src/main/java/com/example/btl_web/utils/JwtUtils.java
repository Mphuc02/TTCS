package com.example.btl_web.utils;

import com.example.btl_web.constant.Constant.Jwt;
import com.example.btl_web.dto.UserDto;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Date;

public class JwtUtils {
    private static JwtUtils jwtUtils;
    public static JwtUtils getInstance()
    {
        if(jwtUtils == null)
            jwtUtils = new JwtUtils();
        return jwtUtils;
    }
    public void addAToken(HttpServletResponse resp, UserDto user)
    {
        long oneDayTime = 1000 * 60 * 60 * 5; //Token hết hạn sau 5 tiếng
        //long oneDayTime = 1000 * 60; 1 Phút
        Date timeNow = new Date();
        Date timeExpirate = new Date(timeNow.getTime() + oneDayTime);

        String jwt = Jwts.builder().
                     setSubject(user.getUserId() + ""). //Đặt chủ thể của subject
                     setIssuedAt(timeNow). //Đặt ngày khởi tạo của token
                     setExpiration(timeExpirate). //Token sẽ hết hạn sau 24h
                     claim("role", user.getRole()).
                     signWith(SignatureAlgorithm.HS256, Jwt.SIGNING_KEY).
                     compact();

        //Token sau khi được ký thì sẽ có dạng base64

        //Todo: cho phần addCookie này và phần removeAToken vào 1 hàm dùng chung
        //Thêm vào 1 Cookie
        Cookie jwtCookie = new Cookie(Jwt.JWT_NAME, jwt);
        jwtCookie.setMaxAge(60 * 60 * 24); //Đặt thời gian hết hạn của cookie là 1 tuần
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        resp.addCookie(jwtCookie);
    }

    public void removeAToken(HttpServletResponse response)
    {
        //Ghi đè cookie rỗng vào cookie này
        Cookie jwtCookie = new Cookie(Jwt.JWT_NAME, null);

        //jwtCookie.setMaxAge(-1); Sau khi người dùng đóng chính duyệt sẽ kết thúc ngay lập tức
        jwtCookie.setMaxAge(60 * 60 * 24); //Đặt thời gian hết hạn của cookie là 1 tuần, với thời gian tính là giây
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);
    }

    public UserDto parseUserFromJwt(String jwt)
    {
        if(!validateToken(jwt))
            return null;
        UserDto user = new UserDto();

        Claims claims = getAllClaimsFromToken(jwt);
        Long userId = Long.valueOf(claims.getSubject());
        String role = claims.get("role", String.class);

        //Todo: Thực hiện tìm kiếm người dùng này trong cơ sở dữ liệu

        user.setUserId(userId);
        user.setRole(role);

        return user;
    }

    public boolean validateToken(String token)
    {
        try {
            Jwts.parser().setSigningKey(Jwt.SIGNING_KEY).parseClaimsJws(token);

            return true;
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature: {}" + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: {}" + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: {}" + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: {}" + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: {}" + e.getMessage());
        }

        return false;
    }

    private Claims getAllClaimsFromToken(String token)
    {
        return Jwts.parser().
               setSigningKey(Jwt.SIGNING_KEY).
               parseClaimsJws(token).
               getBody();
    }
}