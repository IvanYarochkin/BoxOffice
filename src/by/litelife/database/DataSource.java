package by.litelife.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by John on 16.04.2017.
 */
public class DataSource {

    private static final String url = "jdbc:mysql://localhost:3306/boxoffice";



    public Connection createConnection() {

        Properties properties=new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","root");
        properties.setProperty("useUnicode","true");
        properties.setProperty("characterEncoding","UTF-8");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            return DriverManager.getConnection(url, properties);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}