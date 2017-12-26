package co.lazuly.users.services;

import co.lazuly.users.model.Role;
import co.lazuly.users.model.User;
import co.lazuly.users.repositories.UserRepository;
import co.lazuly.users.streaming.NewUserStreamSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by boot on 25/12/2017.
 */
@Service
public class UserService {

    private final UserRepository repo;

    @Autowired
    public UserService(final UserRepository repo) {
        this.repo = repo;
    }

    public User create(final Long schoolId, final String email, final String firstName, final String lastName,
                       final List<Role> roles) {
        User user = new User(schoolId, email, firstName, lastName, roles);
        user = repo.save(user);
        return user;
    }
}
