package com.example.chatfx.db.daoImpl;

import com.example.chatfx.bean.User;

import java.sql.SQLException;

public interface UserDao {
    int insert(User user) throws SQLException;

    int select(User user) throws SQLException;
}
