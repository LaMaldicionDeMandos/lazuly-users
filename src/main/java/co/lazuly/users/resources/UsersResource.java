package co.lazuly.users.resources;

import co.lazuly.users.model.User;
import co.lazuly.users.resources.requests.NewUserRequest;
import co.lazuly.users.security.Role;
import co.lazuly.users.services.UserService;
import co.lazuly.users.utils.UserTokenUtils;
import com.google.common.collect.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.ResponseEntity.status;

/**
 * Created by boot on 7/8/17.
 */
@RestController
@RequestMapping(value = "/")
public class UsersResource {
    private static final Logger logger = LoggerFactory.getLogger(UsersResource.class);

    private final static String USER_CRUD_PERMISSION = "user_crud";

    @Autowired
    UserTokenUtils utils;

    @Autowired
    UserService service;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<User> newUser(final OAuth2Authentication user, @RequestBody final NewUserRequest req) {
        logger.info("Looking up data for {}", user.getPrincipal());
        Long schoolId = utils.getSchoolId(user);
        Collection<Role> roles = utils.getRoles(user);

        if (!Iterables.any(roles, (role) -> role.hasPermissionName(USER_CRUD_PERMISSION))) {
            return status(UNAUTHORIZED).build();
        }

        User newUser = service.create(schoolId, req.getEmail(), req.getFirstName(), req.getLastName(), req.getRoles());

        return status(CREATED).body(newUser);
    }
}
