package RecursiveFileStructure.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import RecursiveFileStructure.module.DBFile;

public class DBGet {
    private static String pathe = "";
    ArrayList<DBFile> fileList = new ArrayList<>();

    public DBGet(String path) {

        pathe = path;
        File files = new File(path);
        File[] data = files.listFiles();
        //there is no file
        if (data == null) {
            return;
        }
        //get all the information
        for (File a : data) {
            getFileInformation(a);
        }
    }

    private void getFileInformation(File file) {

        if (file.isDirectory()) {
            fileList.add(new DBFile(file.getName(), file.getAbsolutePath(), "Folder"));
            File[] sub = file.listFiles();
            if (sub != null) {
                for (File subFile : sub) {
                    getFileInformation(subFile);
                }
            }
        } else {
            fileList.add(new DBFile(file.getName(), file.getAbsolutePath(), "File"));
        }
    }

    public void createFileTxt() {
        File structure = new File(pathe + File.separator + "fileStructure.txt");
        try {
            structure.createNewFile();
            StringBuilder writeIn = new StringBuilder();

            //Store all the information as a string
            for (DBFile file : fileList) {
                writeIn.append(file.getPath());
                writeIn.append("\t");
                writeIn.append(file.getType());
                writeIn.append("\n");
            }

            //Write into txt file
            FileWriter fileWrite = new FileWriter(structure, false);
            fileWrite.write(writeIn.toString());
            fileWrite.flush();
            fileWrite.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}
