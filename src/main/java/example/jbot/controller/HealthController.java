package example.jbot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class HealthController {

    @RequestMapping(value = "health", method = GET, produces = "application/json")
    public String health() {
        return "UP";
    }

}
