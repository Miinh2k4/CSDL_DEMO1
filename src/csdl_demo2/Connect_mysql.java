/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package csdl_demo2;
import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.*;

/**
 *
 * @author Minh
 */
public class Connect_mysql {
    public Connection getConnection(String dbURL, String userName, String password)

        {

            Connection conn = null;

            try {

                Class.forName("com.mysql.jdbc.Driver");

                conn = DriverManager.getConnection(dbURL, userName, password);

                System.out.println("Connect Successfully!");

                } catch (Exception ex) {

                    System.out.println("Connect Failure!");

                    ex.printStackTrace();

                  }

        return conn;

    }
}