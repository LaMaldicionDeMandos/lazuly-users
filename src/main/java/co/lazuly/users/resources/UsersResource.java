package co.lazuly.users.resources;

import co.lazuly.users.model.User;
import co.lazuly.users.resources.requests.NewUserRequest;
import co.lazuly.users.restclients.RolesRestClient;
import co.lazuly.users.security.Role;
import co.lazuly.users.services.UserService;
import co.lazuly.users.streaming.NewUser;
import co.lazuly.users.streaming.NewUserStreamSender;
import co.lazuly.users.utils.UserTokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Iterables.any;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
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

    @Autowired
    RolesRestClient rolesRestClient;

    @Autowired
    NewUserStreamSender sender;

    private boolean isUserCrudAuthorized(OAuth2Authentication user) {
        Collection<Role> ownerRoles = utils.getRoles(user);
        return any(ownerRoles, (role) -> role.hasPermissionName(USER_CRUD_PERMISSION));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<User> newUser(final OAuth2Authentication user, @RequestBody final NewUserRequest req) {
        logger.info("Looking up data for {}", user.getPrincipal());
        Long schoolId = utils.getSchoolId(user);

        if (!isUserCrudAuthorized(user)) {
            return status(UNAUTHORIZED).build();
        }

        try {
            List<co.lazuly.users.model.Role> roles = rolesRestClient.get(req.getRoles());

            User newUser = service.create(schoolId, req.getEmail(), req.getFirstName(), req.getLastName(), roles);

            sender.send(new NewUser(req.getFirstName(), req.getLastName(), req.getEmail(), req.getRoles(), schoolId));

            return status(CREATED).body(newUser);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping
    public ResponseEntity<List<User>> get(final OAuth2Authentication owner) {
        Long schoolId = utils.getSchoolId(owner);

        if (!isUserCrudAuthorized(owner)) {
            return status(UNAUTHORIZED).build();
        }

        try {
            List<User> users = service.getSchoolUsers(schoolId);
            return ResponseEntity.ok(users);
        } catch(Exception e) {
            logger.info(e.getMessage());
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }
}
