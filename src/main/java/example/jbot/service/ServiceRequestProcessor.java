package example.jbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static example.jbot.model.Environments.getUrlForEnvironment;

@Service
public class ServiceRequestProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRequestProcessor.class);

    public String processRequest(String request) {
        String[] inputs = request.split(" ");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_XML_VALUE);
        HttpEntity entity = new HttpEntity(headers);
        RestTemplate restTemplate = new RestTemplate();
        String url = getUrlForEnvironment(inputs[inputs.length - 2]);
        String serviceRequest = url + "sfm/serviceRequest/" + inputs[inputs.length - 1];
        LOGGER.info("Service request url: {}", serviceRequest);
        ResponseEntity<String> response = restTemplate.exchange(serviceRequest, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }
}
