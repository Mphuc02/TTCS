package com.example.btl_web.dto;

import java.util.ArrayList;
import java.util.List;

public class UserDto {
    private Long userId;
    private String userName;
    private String passWord;
    private String re_password;
    private String email;
    private String role;
    private String address;
    private String phone;
    private String fullName;
    private String registeredAt;
    private Integer status;
    private Long lastAction;
    private List<BlogDto> blogs;
    private List<CommentDto> comments;
    private List<BlogDto> likedBlog;
    public void addAnLikedBlog(BlogDto blog){
        if(likedBlog == null)
            likedBlog = new ArrayList<>();
        likedBlog.add(blog);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(String registeredAt) {
        this.registeredAt = registeredAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRe_password() {
        return re_password;
    }

    public void setRe_password(String re_password) {
        this.re_password = re_password;
    }

    public List<BlogDto> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<BlogDto> blogs) {
        this.blogs = blogs;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

    public List<BlogDto> getLikedBlog() {
        return likedBlog;
    }

    public void setLikedBlog(List<BlogDto> likedBlog) {
        this.likedBlog = likedBlog;
    }

    public Long getLastAction() {
        return lastAction;
    }

    public void setLastAction(Long lastAction) {
        this.lastAction = lastAction;
    }
}
