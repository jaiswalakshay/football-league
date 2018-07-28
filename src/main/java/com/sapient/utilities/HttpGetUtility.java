package com.sapient.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 *
 */
@Component
public class HttpGetUtility {

    private final static Logger log = LoggerFactory.getLogger(HttpGetUtility.class);
    private final static ObjectMapper mapper = new ObjectMapper();

    /**
     * @param uri
     * @return
     */
    public ArrayNode getResult(final String uri) {
        ArrayNode response = null;
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(uri);
        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                log.error("Method failed: " + method.getStatusLine());
            }

            // Read the response body.
            byte[] responseBody = method.getResponseBody();

            response = mapper.readValue(new String(responseBody), ArrayNode.class);

        } catch (HttpException e) {
            log.error("Fatal protocol violation: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Release the connection.
            method.releaseConnection();
        }
        return response;
    }
}
