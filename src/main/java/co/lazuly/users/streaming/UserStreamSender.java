package co.lazuly.users.streaming;

import co.lazuly.users.resources.requests.NewUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by boot on 26/12/2017.
 */
@Component
@EnableBinding(UserChannels.class)
public class UserStreamSender {
    private final UserChannels source;

    private final static Logger logger = LoggerFactory.getLogger(UserStreamSender.class);

    @Autowired
    public UserStreamSender(final UserChannels source) {
        this.source = source;
    }

    public void send(final NewUserRequest req) {
        logger.debug("Sending Kafka message {}", req);
        source.newUserOutput().send(MessageBuilder.withPayload(req).build());
    }

    public void deleteUser(final String email) {
        logger.debug("Sending Kafka delete {}", email);
        source.deleteUserOutput().send(MessageBuilder.withPayload(email).build());
    }
}
