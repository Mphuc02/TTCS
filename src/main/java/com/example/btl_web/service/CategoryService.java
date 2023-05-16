package com.example.btl_web.service;

import com.example.btl_web.dto.CategoryDto;
import com.example.btl_web.paging.Pageable;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> findAll(Pageable pageable, CategoryDto dto);
    List<CategoryDto> findAllCategoryOfBlog(Long blogCategoryId, Integer status);
    CategoryDto findOneBy(CategoryDto dto);
    long countCategories();
    Long save(CategoryDto categoryDto);
    Long update(CategoryDto categoryDto);
    Long saveCategoriesOfBlog(Long blogId, List<CategoryDto> categories);
    Long delete(Long categoryId);
    boolean validCategoryCreate(CategoryDto category, String[] error);
    boolean validCategoryUpdate(CategoryDto category, String[] error);
}
