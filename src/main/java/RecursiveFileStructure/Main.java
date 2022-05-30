package RecursiveFileStructure;


import RecursiveFileStructure.view.FileManagement;

import java.nio.file.Paths;

public class Main {
    public static void main(String args[]) {

        String filePath = Paths.get("").toAbsolutePath().toString();
        //Get the file system data
        FileManagement first = new FileManagement(filePath);
        //Create a text file with file system
        first.createFileTxt();
        first.getTxtData();
        new Server();
    }

}
