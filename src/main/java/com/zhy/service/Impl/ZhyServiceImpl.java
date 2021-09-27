package com.zhy.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ZhyServiceImpl {

    @Autowired
    private DataSource readDataSource;

    Connection connection = null;
    PreparedStatement ps = null;

    public void testAutoConfiguration(){
         try {
             connection = readDataSource.getConnection();
             ps = connection.prepareStatement("select username from user where id=?");
             ps.setString(1,"1");
             ResultSet resultSet = ps.executeQuery();
             while (resultSet.next()){
                 String result = resultSet.getString(1);
                 System.out.println("结果是："+result);
             }

         } catch (SQLException throwables) {
             throwables.printStackTrace();
         }finally {
             if (ps!=null){
                 try {
                     ps.close();
                 } catch (SQLException throwables) {
                     throwables.printStackTrace();
                 }
             }
             if (connection!=null){
                 try {
                     connection.close();
                 } catch (SQLException throwables) {
                     throwables.printStackTrace();
                 }
             }
         }
         System.out.println("这是我通过importSelector导入进来的service");
    }
}
