package com.example.btl_web.service;

import com.example.btl_web.dto.BlogDto;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.paging.Pageable;

import java.util.List;

public interface BlogService {
    List<BlogDto> getAllBlogs(Pageable pageable, BlogDto dto);
    BlogDto getOneById(Long blogId);
    long countBlogs(BlogDto blogDto);
    Long save(BlogDto blog);
    Long update(BlogDto blog);
    boolean validCreateBlog(String[] errors, BlogDto blog);
    boolean validUpdateBlog(String[] errors, BlogDto blog);
    boolean checkUserLikedBlog(BlogDto blog, Long userId);
}
