package by.bsuir.http.server;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class SocketHandler implements Runnable {

    private Socket socket;
    private static Map<String, String> responseTypes = new HashMap<>();

    public SocketHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            processRequest();
        } catch (IOException e) {
            throw new RuntimeException("Unexpected error.", e);
        }
    }
    private void processRequest() throws IOException {
        RequestHandler requestHandler = new RequestHandler();
        BufferedReader reader = null;
        PrintStream writer = null;
        InputStream file;
        String requestHeader = null;
        String resource= null;

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintStream(socket.getOutputStream());
            requestHeader = reader.readLine();
            resource = requestHandler.parseRequest(requestHeader);

            file = new FileInputStream("resources/" + resource);

            writeResponse(writer, responseTypes.get(requestHandler.getResourceType(resource)));

        } catch (FileNotFoundException | UnsupportedOperationException e) {
            file = new FileInputStream("resources/404.html");
            writer.println("HTTP/1.0 404 Not found");
            writer.println("Content-Type: text/html");
            writer.println("");
        }

        byte[] bytes = new byte[4096];
        int n;
        while ((n = file.read(bytes)) > 0) {
            writer.write(bytes, 0, n);
        }
        writer.close();
        writer.flush();
        socket.close();
    }

    private static void writeResponse(PrintStream writer, String responseType) {
        writer.println("HTTP/1.0 200 OK");
        writer.println("Content-Type: " + responseType);
        writer.println("");
    }

    static {
        responseTypes.put("html", "text/html");
        responseTypes.put("css", "text/css");
        responseTypes.put("png", "image/png");
        responseTypes.put("jpg", "image/jpg");
        responseTypes.put("ico", "image/x-icon");
    }
}
