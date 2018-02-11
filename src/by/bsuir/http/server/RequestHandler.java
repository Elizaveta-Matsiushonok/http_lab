package by.bsuir.http.server;

public class RequestHandler {

    public String parseRequest(String requestHeader){
        if(requestHeader == null){
            throw new UnsupportedOperationException("Trying to process not GET request.");
        }
        String[] request = requestHeader.split("\\s");
        String requestType = request[0];
        String resource = request[1];

        if(!"GET".equals(requestType)){
            throw new UnsupportedOperationException("Trying to process not GET request.");
        }

        if (!resource.equals("/")) {
            resource = resource.substring(1);
        }
        else {
            resource = "index.html";
        }
        return resource;
    }

    public String getResourceType(String resource){
        String[] splitedResource = resource.split("\\.");

        if(splitedResource.length > 1){
            return splitedResource[1];
        }
        else {
            return "html";
        }

    }

}
