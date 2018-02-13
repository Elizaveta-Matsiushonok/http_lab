package by.bsuir.http.server;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class SocketHandler implements Runnable {

    private Socket socket;
    private static Map<String, String> responseTypes = new HashMap<>();
    private Logger logger = Logger.getLogger(HttpServer.class.getName());

    private static final String FILE_PATH = "resources/";
    private static final String ERROR_PAGE_PATH = "resources/404.html";

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

            logger.info("Request is " + requestHeader);

            resource = requestHandler.parseRequest(requestHeader);
            file = new FileInputStream(FILE_PATH + resource);

            writeResponse(writer, responseTypes.get(requestHandler.getResourceType(resource)));

        } catch (FileNotFoundException | UnsupportedOperationException e) {

            file = new FileInputStream(ERROR_PAGE_PATH);
            writer.println("HTTP/1.0 404 Not found");
            writer.println("Content-Type: text/html");
            writer.println("");
        }

        byte[] bytes = new byte[4096];
        int length;
        while ((length = file.read(bytes)) > 0) {
            writer.write(bytes, 0, length);
        }
        writer.flush();
        writer.close();
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
