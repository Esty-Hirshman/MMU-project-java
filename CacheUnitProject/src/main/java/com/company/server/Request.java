package main.java.com.company.server;

import java.io.Serializable;
import java.util.Map;

/**
 * Will be transmitted from the client through all server components
 * Until saving / retrieving / deleting the data in the memory unit
 * thadata will be in Json object -
 * {"headers":
 *  {"action":"UPDATE"},
 *      "body":[{"dataModelId":1L,
 *                "content":"Some String Data"}]
 *  }
 * @param <T>
 */
public class Request<T> implements Serializable {
    private Map<String,String> headers;
    private T body;
    public Request(Map<String, String> headers, T body) {
        this.headers = headers;
        this.body = body;
    }

    /**
     * get data header
     * @return header
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * get data body
     * @return body
     */
    public T getBody() {
        return this.body;
    }

    /**
     * set data header
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * set data body
     */
    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Request{" +
                "headers:" + headers +
                ", body:" + body +
                '}';
    }
}
