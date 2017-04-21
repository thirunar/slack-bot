package example.jbot.service;

import example.jbot.util.PrettyPrinter;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.text.MessageFormat;

import static example.jbot.model.Environments.getEnvironmentNames;
import static example.jbot.model.Environments.getUrlForEnvironment;
import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static java.util.Arrays.equals;

@Service
public class ServiceRequestProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRequestProcessor.class);

    public String processRequest(String request) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = createHeaders("admin", "secret");
        String url = getUrl(request);
        if (url != null) {
            if (request.contains("livenodes")) {
                HttpEntity entity = new HttpEntity(headers);
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                return response.getBody();
            } else if (request.contains("refresh")) {
                HttpEntity entity = new HttpEntity(headers);
                ResponseEntity response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                return response.getStatusCode().name();
            } else {
                headers.setAccept(asList(MediaType.APPLICATION_XML));
                HttpEntity entity = new HttpEntity(headers);
                try {
                    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                    return PrettyPrinter.formatXML(response.getBody());
                } catch (Exception e) {
                    return "Sorry!! Error processing the request!!!";
                }
            }
        }
        return "";

    }


    private String getUrl(String request) {
        String[] inputs = request.split(" ");
        String url = getUrlForEnvironment(inputs[inputs.length - 2]);
        if (request.contains("livenodes")) {
            return format("{0}livenodes", url);
        } else if (request.contains("internal")) {
            return format("{0}/serviceRequest/{1}", url, inputs[inputs.length - 1]);
        } else if (request.contains("refresh")) {
            return format("{0}bus/refresh", url);
        } else if (getEnvironmentNames().contains(inputs[inputs.length - 2]) && inputs.length == 2) {
            return MessageFormat.format("{0}/{1}", url, inputs[inputs.length - 1]);
        }
        return null;
    }

    private HttpHeaders createHeaders(final String username, final String password) {
        return new HttpHeaders() {
            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(
                        auth.getBytes(Charset.forName("US-ASCII")));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
            }
        };
    }
}
