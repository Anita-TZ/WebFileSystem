package RecursiveFileStructure.view;

import RecursiveFileStructure.module.DBFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class TemplateGenerator {
    private ArrayList<DBFile> sql = new ArrayList<>();


    public String readHTMLFile(ArrayList<DBFile> in) {

        sql.clear();
        sql.addAll(in);

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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return htmlResponse;
    }

    public String divideFolderAndFile() {
        StringBuilder body = new StringBuilder();
        if (!sql.isEmpty()) {
            for (DBFile dbFile : sql) {
                if (dbFile.getType().equals("File")) {
                    body.append(createFile(dbFile));
                } else {
                    body.append(createFolder(dbFile));
                }
            }
        } else {
            body.append("Not Found");
        }
        return body.toString();
    }

    public String createFolder(DBFile element) {
        return "<li style=\"color:#d32b31\">" + element.getPath() + "</li>";
    }

    public String createFile(DBFile element) {
        return "<li style=\"color:#3257ba\">" + element.getPath() + "</li>";
    }
}
