package RecursiveFileStructure.view;

import RecursiveFileStructure.module.DBFile;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class DBSql {
    private Connection conn;
    private Properties properties;
    private static final String Username = "root";
    private static final String Password = "dev12345";
    private static final String DatabaseDriver = "com.mysql.cj.jdbc.Driver";
    private static final String DatabaseURL = "jdbc:mysql://localhost:3306/devdb";
    private ArrayList<DBFile> sql = new ArrayList<>();

    public void OpenSQL(String behavior, ArrayList<DBFile> In, String request) {

        sql.clear();
        sql.addAll(In);

        if (conn == null) {
            try {
                Class.forName(DatabaseDriver);
                conn = DriverManager.getConnection(DatabaseURL, getConfiguration());
                if (behavior.equals("create")) {
                    InitialDB();
                    InsertData();
                } else if (behavior.equals("search")) {
                    getSearchData(conn, request);
                }

                //disconnection
                conn.close();
                conn = null;
            } catch (SQLException ex) {
                // handle any errors
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private Properties getConfiguration() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", Username);
            properties.setProperty("password", Password);
        }
        return properties;
    }

    private void InitialDB() {

        //Avoid old data in the database
        String drop = "DROP TABLE IF EXISTS fileSystem;";

        String create = "CREATE TABLE fileSystem (id int PRIMARY KEY," +
                "  name varchar(100) NOT NULL," +
                "  path varchar(6000) NOT NULL," +
                "  type ENUM ('Folder', 'File')" +
                ");";

        try {
            PreparedStatement statement = conn.prepareStatement(drop);
            statement.execute();
            statement = conn.prepareStatement(create);
            statement.execute();

        } catch (SQLException wrong) {
            System.out.println(wrong.getMessage());
        }
    }

    public void InsertData() {
        String SQL = "INSERT INTO fileSystem(id,name,path, type) " + "VALUES(?,?,?,?)";

        try {
            PreparedStatement statement = conn.prepareStatement(SQL);
            int num = 0;
            for (DBFile values : sql) {
                statement.setInt(1, values.getID());
                statement.setString(2, values.getName());
                statement.setString(3, values.getPath());
                statement.setString(4, values.getType());
                statement.addBatch();
                num++;
                //Batch Input at 100 data or achieve th size of In
                if (num == sql.size() || num % 100 == 0) {
                    statement.executeBatch();
                }
            }
            statement.close();
        } catch (SQLException wrong) {
            System.out.println(wrong.getMessage());
        }

    }

    private void getSearchData(Connection conn, String request) {
        String SQL = "SELECT name,path, type from fileSystem" + " WHERE name LIKE ?";
        try {
            PreparedStatement input = conn.prepareStatement(SQL);

            //need to get all the file name contains the word
            input.setString(1, "%" + request + "%");
            ResultSet result = input.executeQuery();
            //Get the result of SQL
            sql.clear();
            while (result.next()) {
                sql.add(new DBFile(result.getString("name"),
                        result.getString("path"), result.getString("type")));
            }

            //prevent connection disabled
            result.close();
            input.close();
        } catch (SQLException wrong) {
            System.out.println(wrong.getMessage());
        }
    }

    public ArrayList<DBFile> getDBfile() {
        return sql;
    }
}
