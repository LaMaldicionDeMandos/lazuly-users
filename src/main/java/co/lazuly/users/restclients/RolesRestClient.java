package co.lazuly.users.restclients;

import co.lazuly.users.model.Role;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by boot on 10/19/17.
 */
@Component
public class RolesRestClient {
    private final static Logger log = LoggerFactory.getLogger(RolesRestClient.class);

    @Autowired
    RolesFeignClient client;

    @Value("${app.secret}")
    String secret;

    public List<Role> get(final List<String> roles) {
        log.info("Get Roles with secret {}", secret);
        Map<String, List<String>> codes = Maps.newHashMap();
        codes.put("codes", roles);
        return client.get(codes, secret);
    }

}
