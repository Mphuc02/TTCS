package com.example.btl_web.service;

import com.example.btl_web.dto.BlogDto;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.paging.Pageable;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface BlogService {
    List<BlogDto> getAllBlogs(Pageable pageable, BlogDto dto);
    public List<UserDto> peopleLikedBlog(Long blogId);
    BlogDto getOne(BlogDto searchBlog);
    long countBlogs(BlogDto blogDto);
    Long save(BlogDto blog);
    Long update(BlogDto blog);
    boolean validCreateBlog(String[] messages, BlogDto blog);
    boolean validUpdateBlog(String[] messages, BlogDto blog, Long userId);
    boolean checkUserLikedBlog(BlogDto blog, Long userId);
    void addPTagContent(BlogDto blog);
    void removePTagContent(BlogDto blog);
}
