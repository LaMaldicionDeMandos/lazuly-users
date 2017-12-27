package co.lazuly.users.streaming;

import co.lazuly.users.model.Role;
import co.lazuly.users.services.UserService;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import java.util.List;

import static java.util.Objects.isNull;

/**
 * Created by boot on 26/12/2017.
 */
@EnableBinding(UserChannels.class)
public class NewOwnerStream {

    @Autowired
    UserService service;

    private final Logger logger = LoggerFactory.getLogger(NewOwnerStream.class);
    @StreamListener(UserChannels.NEW_OWNER_INPUT)
    public void newOwner(final NewUser req) {
        logger.info("Receiving new owner {}.", req);
        try {
            //TODO sacar, es una negrada, pasa que sino estarian llamandose muchas veces seguidas
            List<Role> roles = Lists.transform(req.getRoles(), (role) -> new Role(role, "Dueño"));
            service.create(req.getSchoolId(), req.getEmail(), req.getFirstName(), req.getLastName(), roles);
        } catch (Exception e) {
            logger.info("Error saving new owner from stream");
        }
    }

}
