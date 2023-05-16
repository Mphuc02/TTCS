package com.example.btl_web.mapper.impl;

import com.example.btl_web.mapper.RowMapper;
import com.example.btl_web.model.Category;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryMapperImpl implements RowMapper {
    @Override
    public Category mapper(ResultSet resultSet) {
        Category category = new Category();

        try {
            category.setCategoryId(resultSet.getLong("category_id"));
            category.setName(resultSet.getString("name"));
            category.setUserId(resultSet.getLong("user_id"));
            category.setCreatedAt(resultSet.getLong("created_at"));
            category.setStatus(resultSet.getInt("status"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }
}
