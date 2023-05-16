package com.example.btl_web.service;

import com.example.btl_web.dto.UserDto;
import com.example.btl_web.paging.Pageable;

import java.util.List;

public interface UserService{
    List<UserDto> findAll(Pageable pageable, UserDto dto);
    List<UserDto> findAllInclude(Pageable pageable, UserDto dto);
    UserDto findOneById(Long userId);
    long countUsers();
    UserDto login(String userName, String passWord);
    int signUp(String userName, String passWord, String passWord_2, String email);
    Long saveUser(UserDto userDto);
    Long updateUser(UserDto dto);
    boolean validateSignUp(UserDto user, String[] errors);
    boolean validUpdate(UserDto user, String[] errors);
    boolean updateLastAction(UserDto user);
    Long checkLastAction(Long userId);
}