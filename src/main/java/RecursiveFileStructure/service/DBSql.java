package RecursiveFileStructure.service;

import RecursiveFileStructure.module.DBFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class DBSql {
    ArrayList<DBFile> In = new ArrayList<>();
    private Connection conn;
    private Properties properties;
    private static final String Username = "root";
    private static final String Password = "dev12345";
    private static final String DatabaseDriver = "com.mysql.cj.jdbc.Driver";
    private static final String DatabaseURL = "jdbc:mysql://localhost:3306/devdb";


    public void getTxtData() {

        //Primary key automatically generated
        int id = 1;

        //Read data from txt
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader
                    (Paths.get("fileStructure.txt").toString()));
            String line = bufferedReader.readLine();
            do {
                String path = line.split("\t")[0];
                String type= line.split("\t")[1];
                String name = path.substring(path.lastIndexOf('\\') + 1);
                In.add(new DBFile(name,path, type));
                In.get(id-1).setID(id);
                id++;
                line = bufferedReader.readLine();
            } while (line != null);
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void OpenSQL() {
        if (conn == null) {
            try {
                Class.forName(DatabaseDriver);
                conn = DriverManager.getConnection(DatabaseURL, getProperties());
                InitialDB();
                InsertData();
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

    public void InsertData() {
        String SQL = "INSERT INTO fileSystem(id,name,path, type) " + "VALUES(?,?,?,?)";

        try {
            PreparedStatement statement = conn.prepareStatement(SQL);
            int num = 0;
            for (DBFile values : In) {
                statement.setInt(1, values.getID());
                statement.setString(2, values.getName());
                statement.setString(3, values.getPath());
                statement.setString(4, values.getType());
                statement.addBatch();
                num++;
                //Batch Input at 100 data or achieve th size of In
                if (num == In.size() || num % 100 == 0) {
                    statement.executeBatch();
                }
            }
            statement.close();
        } catch (SQLException wrong) {
            System.out.println(wrong.getMessage());
        }

    }

    private Properties getProperties() {
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
                "  type ENUM ('Folder', 'File')"+
                ");";

        try {
            PreparedStatement statement = conn.prepareStatement(drop);
            statement.execute();
            statement= conn.prepareStatement(create);
            statement.execute();

        } catch (SQLException wrong) {
            System.out.println(wrong.getMessage());
        }
    }
}
