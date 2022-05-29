package RecursiveFileStructure;

import RecursiveFileStructure.service.SmallHttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {

    public Server(){
        startHTTPServer();
    }

    public void startHTTPServer() {

        HttpServer server = null;
        try {
            //0 is used for back logging.
            server = HttpServer.create(new InetSocketAddress("localhost", 8888), 0);
            server.createContext("/search", new SmallHttpHandler());
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
            server.setExecutor(threadPoolExecutor);
            server.start();
            System.out.println("Server listening on port " + 8888);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        assert server != null;
//        server.createContext("/search", new SmallHttpHandler());
//        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
//        server.setExecutor(threadPoolExecutor);
//        server.start();
        //TODO deal with connection close
    }
}

//.info(" Server started on port 8888");
//對應的URL , 根據 相對應的動作
//file -帶參數

