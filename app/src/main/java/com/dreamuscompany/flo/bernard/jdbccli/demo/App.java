package com.dreamuscompany.flo.bernard.jdbccli.demo;

import software.aws.rds.jdbc.mysql.shading.com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class App {

    public static void main(String[] args) throws Exception {

        System.out.println("start");

        var dataSource = createDataSource();

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()
        ) {
            connection.setAutoCommit(false);

            System.out.println("connected");

            var stdinReader = new BufferedReader(new InputStreamReader(System.in));

            boolean cont;
            do {
                cont = readEvalPrint(stdinReader, statement);
            } while (cont);
        }
    }

    private static boolean readEvalPrint(BufferedReader stdinReader, Statement statement) throws Exception {

        var line = stdinReader.readLine();
        if (line == null) {
            return false;
        }

        if (line.isBlank()) {
            return true;
        }

        try {

            boolean hasResultSet = statement.execute(line);
            if (hasResultSet) {
                printResultSet(statement);
            } else {
                System.out.println("updated: " + statement.getUpdateCount());
            }

        } catch (SQLException e) {
            System.out.println("error: " + e.getMessage());
        }

        return true;
    }

    private static void printResultSet(Statement statement) throws Exception {

        try (ResultSet resultSet = statement.getResultSet()) {
            var columnCount = resultSet.getMetaData().getColumnCount();

            while (resultSet.next()) {
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    System.out.print(resultSet.getObject(columnIndex) + "\t");
                }
                System.out.println();
            }
        }

        System.out.println("-- end --");
    }

    private static DataSource createDataSource() {

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setURL(url);
        mysqlDataSource.setUser(user);
        mysqlDataSource.setPassword(password);

        return mysqlDataSource;
    }
}
