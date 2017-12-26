package co.lazuly.users.streaming;

import co.lazuly.users.resources.requests.NewUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by boot on 26/12/2017.
 */
@Component
@EnableBinding(Source.class)
public class NewUserStreamSender {
    private final Source source;

    private final static Logger logger = LoggerFactory.getLogger(NewUserStreamSender.class);

    @Autowired
    public NewUserStreamSender(final Source source) {
        this.source = source;
    }

    public void send(final NewUserRequest req) {
        logger.debug("Sending Kafka message {}", req);
        source.output().send(MessageBuilder.withPayload(req).build());
    }
}
