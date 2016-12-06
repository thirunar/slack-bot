package example.jbot.slack;

import com.google.common.base.Splitter;
import example.jbot.service.ServiceRequestProcessor;
import me.ramswaroop.jbot.core.slack.Bot;
import me.ramswaroop.jbot.core.slack.Controller;
import me.ramswaroop.jbot.core.slack.EventType;
import me.ramswaroop.jbot.core.slack.models.Event;
import me.ramswaroop.jbot.core.slack.models.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketSession;

import static example.jbot.util.PrettyPrinter.formatCode;
import static example.jbot.util.PrettyPrinter.formatXML;

@Component
public class SlackBot extends Bot {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlackBot.class);

    @Value("${slackBotToken}")
    private String slackToken;

    private RestTemplate restTemplate;

    @Autowired
    private ServiceRequestProcessor requestProcessor;

    @Override
    public String getSlackToken() {
        return slackToken;
    }

    @Override
    public Bot getSlackBot() {
        return this;
    }

    @Controller(events = {EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE})
    public void onReceiveDM(final WebSocketSession session, final Event event) {
        LOGGER.info("Received: {}", event.getText());

        String response = requestProcessor.processRequest(event.getText());
        Iterable<String> splittedStrings = Splitter.fixedLength(4000).split(response);

        for (String splittedString : splittedStrings) {
            reply(session, event, new Message(formatCode(splittedString)));
        }
        stopConversation(event);
    }

}