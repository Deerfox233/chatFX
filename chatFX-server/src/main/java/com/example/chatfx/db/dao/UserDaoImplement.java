package com.example.chatfx.db.dao;

import com.example.chatfx.bean.User;
import com.example.chatfx.db.DBConnection;
import com.example.chatfx.db.daoImpl.UserDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImplement implements UserDao {
    private static String SQL_INSERT = "INSERT INTO `user` VALUES(?, ?)";
    private static String SQL_SELECT = "SELECT `password` FROM `user` WHERE `name`=?";
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;

    @Override
    public int insert(User user) throws SQLException {
        preparedStatement = DBConnection.getConnection().prepareStatement(SQL_INSERT);
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.executeUpdate();
        DBConnection.close(preparedStatement);

        return 0;
    }

    @Override
    public int select(User user) throws SQLException {
        int state;
        preparedStatement = DBConnection.getConnection().prepareStatement(SQL_SELECT);
        preparedStatement.setString(1, user.getName());
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {     //用户存在
            if (resultSet.getString("password").equals(user.getPassword())) {
                //密码正确
                state = 0;
            } else {
                //密码错误
                state = 1;
            }
        } else {         //用户不存在
            state = -1;
        }
        DBConnection.close(resultSet, preparedStatement);
        return state;
    }
}
