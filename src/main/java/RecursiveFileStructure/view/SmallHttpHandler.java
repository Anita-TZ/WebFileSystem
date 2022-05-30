package RecursiveFileStructure.view;

import RecursiveFileStructure.module.DBFile;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class SmallHttpHandler implements HttpHandler {
    private ArrayList<DBFile> sql = new ArrayList<>();
    private OutputStream outputStream;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {


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

        DBSql search = new DBSql();
        search.OpenSQL("search", sql, request);
        //this.sql
        sql.clear();
        sql.addAll(search.getDBfile());

    }

    private void backToWeb(HttpExchange httpExchange) throws IOException {

        OutputStream outputStream = httpExchange.getResponseBody();

        TemplateGenerator template =new TemplateGenerator();
        String htmlResponse = template.readHTMLFile(sql);

        httpExchange.sendResponseHeaders(200, htmlResponse.length());
        outputStream.write(htmlResponse.getBytes());
        outputStream.flush();
        outputStream.close();
    }

}
