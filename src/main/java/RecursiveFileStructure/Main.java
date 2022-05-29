package RecursiveFileStructure;


import RecursiveFileStructure.service.DBGet;
import RecursiveFileStructure.service.DBSql;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {
    public static void main(String args[]) {

        String filePath = Paths.get("").toAbsolutePath().toString();
        //Get the file system data
        DBGet first = new DBGet(filePath);
        //Create a text file with file system
        first.createFileTxt();

        DBSql insert = new DBSql();
        //Read the data from txt file
        insert.getTxtData();
        //Insert data into database
        insert.OpenSQL();


        Server web = new Server();


    }


}
