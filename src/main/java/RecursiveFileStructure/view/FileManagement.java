package RecursiveFileStructure.view;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import RecursiveFileStructure.module.DBFile;

public class FileManagement {
    private static String pathe = "";
    ArrayList<DBFile> fileList = new ArrayList<>();

    public FileManagement(String path) {

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

    public void getTxtData() {

        //Primary key automatically generated
        int id = 1;
        fileList.clear();

        //Read data from txt
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader
                    (Paths.get("fileStructure.txt").toString()));
            String line = bufferedReader.readLine();
            do {
                String path = line.split("\t")[0];
                String type= line.split("\t")[1];
                String name = path.substring(path.lastIndexOf('\\') + 1);
                fileList.add(new DBFile(name,path, type));
                fileList.get(id-1).setID(id);
                id++;
                line = bufferedReader.readLine();
            } while (line != null);
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        DBSql create = new DBSql();
        create.OpenSQL("create", fileList, null);
    }
}
