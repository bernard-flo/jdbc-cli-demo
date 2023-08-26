package com.dreamuscompany.flo.bernard.jdbccli.demo;

import software.aws.rds.jdbc.mysql.shading.com.mysql.cj.jdbc.MysqlDataSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class App {
    public static void main(String[] args) {

        String url = "jdbc:mysql://<HOST>:<PORT>/<DB>>";
        String user = "<USER>";
        String password = "<PASSWORD>";

        System.out.println("start");

        try {
            MysqlDataSource mysqlDataSource = new MysqlDataSource();
            mysqlDataSource.setURL(url);
            mysqlDataSource.setUser(user);
            mysqlDataSource.setPassword(password);


            try (Connection connection = mysqlDataSource.getConnection();
                 Statement statement = connection.createStatement()
            ) {
                connection.setAutoCommit(false);
                System.out.println("connected");

                var reader = new BufferedReader(new InputStreamReader(System.in) );

                while (true) {
                    try {
                        String line = reader.readLine();
                        if (line == null || line.isBlank()) {
                            continue;
                        }

                        boolean selected = statement.execute(line);
                        if (selected) {
                            ResultSet resultSet = statement.getResultSet();
                            while (resultSet.next()) {
                                System.out.println(resultSet.getString(1));
                            }
                            System.out.println("Done");
                        } else {
                            System.out.println(statement.getUpdateCount());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
