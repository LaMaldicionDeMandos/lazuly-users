package co.lazuly.users.resources;

import co.lazuly.users.model.User;
import co.lazuly.users.resources.requests.NewUserRequest;
import co.lazuly.users.restclients.RolesRestClient;
import co.lazuly.users.security.Role;
import co.lazuly.users.services.UserService;
import co.lazuly.users.streaming.NewUser;
import co.lazuly.users.streaming.UserStreamSender;
import co.lazuly.users.utils.UserTokenUtils;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Iterables.any;
import static java.util.Objects.isNull;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

/**
 * Created by boot on 7/8/17.
 */
@RestController
@RequestMapping(value = "/")
public class UsersResource {
    private static final Logger logger = LoggerFactory.getLogger(UsersResource.class);

    private final static String USER_CRUD_PERMISSION = "user_crud";
    private final static String OWNER = "owner";

    @Autowired
    UserTokenUtils utils;

    @Autowired
    UserService service;

    @Autowired
    RolesRestClient rolesRestClient;

    @Autowired
    UserStreamSender sender;

    private boolean isUserCrudAuthorized(OAuth2Authentication user) {
        Collection<Role> ownerRoles = utils.getRoles(user);
        return any(ownerRoles, (role) -> role.hasPermissionName(USER_CRUD_PERMISSION));
    }

    private boolean isRolesChanged(User newUser, User oldUser) {
        return !newUser.getRoles().equals(oldUser.getRoles());
    }

    private boolean isWellDefine(final Long schoolId, final String email, final User user, final User old) {
        return email.equals(user.getEmail()) &&
                schoolId.equals(user.getSchoolId()) &&
                user.owner() == old.owner();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<User> newUser(final OAuth2Authentication user, @RequestBody final NewUserRequest req) {
        logger.info("Looking up data for {}", user.getPrincipal());

        if (!isUserCrudAuthorized(user)) {
            return status(UNAUTHORIZED).build();
        }

        Long schoolId = utils.getSchoolId(user);

        try {
            List<co.lazuly.users.model.Role> roles = rolesRestClient.get(req.getRoles());

            User newUser = service.create(schoolId, req.getEmail(), req.getFirstName(), req.getLastName(),
                    req.getJobTitle(), roles);

            sender.send(new NewUser(req.getFirstName(), req.getLastName(), req.getEmail(), req.getRoles(), schoolId));

            return status(CREATED).body(newUser);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return status(INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping
    public ResponseEntity<List<User>> get(final OAuth2Authentication owner) {
        if (!isUserCrudAuthorized(owner)) {
            return status(UNAUTHORIZED).build();
        }

        Long schoolId = utils.getSchoolId(owner);

        try {
            List<User> users = service.getSchoolUsers(schoolId);
            return ok(users);
        } catch(Exception e) {
            logger.info(e.getMessage());
            return status(INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "{email}", method = RequestMethod.PUT)
    public ResponseEntity<User> change(final OAuth2Authentication owner, @PathVariable final String email,
                                       @RequestBody User user) {
        logger.info("Looking up data for {}", owner.getPrincipal());
        if (!isUserCrudAuthorized(owner)) return status(UNAUTHORIZED).build();

        Long schoolId = utils.getSchoolId(owner);

        User oldUser = service.get(email);

        if (isNull(oldUser) || !isWellDefine(schoolId, email, user, oldUser)) return badRequest().build();

        try {
            oldUser = service.change(schoolId, user);

            if (isNull(oldUser)) return badRequest().build();

            if (isRolesChanged(user, oldUser)) {
                sender.changeRoles(email, Lists.transform(user.getRoles(), co.lazuly.users.model.Role::getCode));
            }

            return ok(user);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return status(INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "{email}", method = RequestMethod.DELETE)
    public ResponseEntity<User> delete(final OAuth2Authentication owner, @PathVariable final String email) {
        logger.info("Looking up data for {}", owner.getPrincipal());
        if (!isUserCrudAuthorized(owner)) return status(UNAUTHORIZED).build();

        Long schoolId = utils.getSchoolId(owner);

        try {
            User user = service.delete(schoolId, email);

            if (isNull(user)) return badRequest().build();

            sender.deleteUser(email);

            return ok(user);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return status(INTERNAL_SERVER_ERROR).build();
        }
    }
}
