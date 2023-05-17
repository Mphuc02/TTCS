package com.example.btl_web.dto;
import com.example.btl_web.configuration.ServiceConfiguration;
import com.example.btl_web.service.CategoryService;
import com.example.btl_web.service.impl.CategoryServiceImpl;
import com.example.btl_web.utils.BytePartUtils;
import jakarta.servlet.http.Part;

import java.util.*;

public class BlogDto {
    //Các thuộc tính của model
    private Long blogId;
    private String title;
    private String content;
    private String imageTitle;
    private String createdAt;
    private UserDto user;
    private Part imageTitleData;
    private Integer status;
    List<CategoryDto> categories;
    List<UserDto> likedUsers;
    List<CommentDto> comments;
    //Thuộc tính dùng để tìm kiếm
    private String categorySearch;
    //Các hàm
    public BlogDto()
    {

    }
    public boolean checkUserLikedBlog(Long userId)
    {
        if(likedUsers == null)
            return false;
        for(UserDto user: likedUsers)
        {
            if(user.getUserId() == userId)
                return true;
        }
        return false;
    }
    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getTitle() {
        return title;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createAt) {
        this.createdAt = createAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public void setCategories(Long[] categoryIds) {
        this.categories = new ArrayList<>();
        CategoryService categoryService = ServiceConfiguration.getCategoryService();
        for(Long categoryId: categoryIds)
        {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setCategoryId(categoryId);
            this.categories.add(categoryService.findOneBy(categoryDto));
        }
    }
    public void addACategory(CategoryDto categoryDto)
    {
        if(this.categories == null)
            this.categories = new ArrayList<>();
        this.categories.add(categoryDto);
    }

    public Part getImageTitleData() {
        return imageTitleData;
    }

    public void setImageTitleData(String base64Image ) {
        byte[] imageData = Base64.getDecoder().decode(base64Image);

        this.imageTitleData = new BytePartUtils(imageData, "1");
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setImageTitleData(Part imageTitleData) {
        this.imageTitleData = imageTitleData;
    }

    public String getCategorySearch() {
        return categorySearch;
    }

    public void setCategorySearch(String categorySearch) {
        this.categorySearch = categorySearch;
    }

    public List<CategoryDto> getCategories() {
        return categories;
    }

    public List<UserDto> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(List<UserDto> likedUsers) {
        this.likedUsers = likedUsers;
    }

    public void setCategoriesList(List<CategoryDto> categories) {
        this.categories = categories;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }
}
