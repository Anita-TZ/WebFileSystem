package RecursiveFileStructure.service;

import RecursiveFileStructure.module.DBFile;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class SmallHttpHandler implements HttpHandler {
    private Connection conn;
    private Properties properties;
    private static final String Username = "root";
    private static final String Password = "dev12345";
    private static final String DatabaseDriver = "com.mysql.cj.jdbc.Driver";
    private static final String DatabaseURL = "jdbc:mysql://localhost:3306/devdb";
    private ArrayList<DBFile> sql = new ArrayList<>();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String request = null;

        if ("GET".equals(httpExchange.getRequestMethod())) {
            GetRequest(httpExchange);
        }
        backToWeb(httpExchange);
    }

    //deal with string
    private void GetRequest(HttpExchange httpExchange) {
        //prevent blank input
        String request = "";
        if (httpExchange.getRequestURI().toString().split("=").length > 1) {
            request = httpExchange.getRequestURI().toString().split("=")[1];
        }

        if (conn == null) {
            try {
                Class.forName(DatabaseDriver);
                conn = DriverManager.getConnection(DatabaseURL, getProperties());
                System.out.println(conn);//TODO
                getSearchData(conn, request);
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

    private void backToWeb(HttpExchange httpExchange) throws IOException {

        OutputStream outputStream = httpExchange.getResponseBody();

        String htmlResponse = readHTMLFile();

        httpExchange.sendResponseHeaders(200,htmlResponse.length());
        outputStream.write(htmlResponse.getBytes());
        outputStream.flush();
        outputStream.close();
}

//    public DBFile[] getResult() {
//        int len=sql.size();
//        DBFile[] result = new DBFile[len];
//
//        for(int i =0; i<100; i++){
//            if(i%2==0){
//                result[i]=new DBFile("libraries", "C:\\Users\\USER\\IdeaProjects\\FileSystem\\.idea\\libraries", "Folder");
//            }else{
//                result[i]=new DBFile("compiler.xml", "C:\\Users\\USER\\IdeaProjects\\FileSystem\\.idea\\compiler.xml", "File");
//            }
//        }
//        return result;
//    }

    public String createFolder(DBFile element) {

        return "<li style=\"color:#d32b31\">" + element.getPath() + "</li>";
    }

    public String createFile(DBFile element) {
        return "<li style=\"color:#3257ba\">" + element.getPath() + "</li>";
    }


    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", Username);
            properties.setProperty("password", Password);
        }
        return properties;
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

    public String readHTMLFile() {

        String htmlResponse = "";
        //Read data from html file
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader
                    (Paths.get("setUp.html").toString()));
            String line = bufferedReader.readLine();
            ;
            do {
                htmlResponse += line;
                htmlResponse += "\n";
                line = bufferedReader.readLine();

            } while (line != null);
            htmlResponse = htmlResponse.replace("{{body}}", divideFolderAndFile());
            bufferedReader.close();
            System.out.println("done");//TODO
        } catch (IOException e) {
            e.printStackTrace();
        }
        return htmlResponse;
    }

    public String divideFolderAndFile() {
        StringBuilder body = new StringBuilder();
        for (DBFile dbFile : sql) {
            if (dbFile.getType().equals("File")) {
                body.append(createFile(dbFile));
            } else {
                body.append(createFolder(dbFile));
            }
        }
        return body.toString();
    }

}
