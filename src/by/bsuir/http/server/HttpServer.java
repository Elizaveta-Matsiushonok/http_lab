package by.bsuir.http.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class HttpServer {

    public static Logger logger = Logger.getLogger(HttpServer.class.getName());


    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(8081);

        logger.info("HTTP server is waiting for clients...");

        while (true) {
            Socket socket = serverSocket.accept();

            logger.info("New client was connected...");
            Thread serverThread = new Thread(new SocketHandler(socket));
            serverThread.start();
        }
    }
}
