package example.jbot.service;

import example.jbot.util.PrettyPrinter;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.text.MessageFormat;

import static example.jbot.model.Environments.getUrlForEnvironment;
import static java.text.MessageFormat.format;

@Service
public class ServiceRequestProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRequestProcessor.class);

    public String processRequest(String request) {
        HttpEntity entity = new HttpEntity(createHeaders("admin", "secret"));
        RestTemplate restTemplate = new RestTemplate();
        String serviceRequest = getUrl(request);
        if (serviceRequest != null) {
            LOGGER.info("Service request url: {}", serviceRequest);
            ResponseEntity<String> response = restTemplate.exchange(serviceRequest, HttpMethod.GET, entity, String.class);
            return PrettyPrinter.formatXML(response.getBody());
        }
        return "Please enter valid input. \"<env> <serviceRequestId>\" or \"internal <env> <serviceRequestId>\" or <env> livenodes";
    }

    private String getUrl(String request) {
        String[] inputs = request.split(" ");
        String url = getUrlForEnvironment(inputs[inputs.length - 2]);
        if (request.contains("internal")) {
            return format("{0}sfm/internal/serviceRequest/{1}", url, inputs[inputs.length - 1]);
        } else if (request.contains("livenodes")) {
            return MessageFormat.format("{0}sm/admin/livenodes", url, inputs[inputs.length - 1]);
        } else if (request.contains("serviceRequest")) {
            return MessageFormat.format("{0}sfm/serviceRequest/{1}", url, inputs[inputs.length - 1]);
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
