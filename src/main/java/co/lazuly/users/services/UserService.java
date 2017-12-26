package co.lazuly.users.services;

import co.lazuly.users.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by boot on 25/12/2017.
 */
@Service
public class UserService {

    public User create(final Long schoolId, final String email, final String firstName, final String lastName,
                       final List<String> roles) {
        User user = new User(schoolId, email, firstName, lastName, roles);
        return user;
    }
}
