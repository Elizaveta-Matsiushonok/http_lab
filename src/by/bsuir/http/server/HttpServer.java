package by.bsuir.http.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(8081);
        System.out.println("HTTP server is running...");

        while (true) {
            Socket socket = serverSocket.accept();
            Thread serverThread = new Thread(new SocketHandler(socket));
            serverThread.start();
        }
    }
}
