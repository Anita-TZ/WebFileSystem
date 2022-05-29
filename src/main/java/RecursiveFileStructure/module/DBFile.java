package RecursiveFileStructure.module;

public class DBFile {

    private final String filePath;
    private final String fileName;
    private final String fileType;
    private int id;

    public DBFile(String name, String path, String type){

        this.fileName=name;
        this.fileType=type;
        this.filePath=path;

    }

    public String getName(){
        return fileName;
    }
    public String getType(){
        return fileType;
    }
    public String getPath(){
        return filePath;
    }

    public void setID(int id){
        this.id=id;
    }
    public int getID(){
        return id;
    }
}
