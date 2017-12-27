package co.lazuly.users.services;

import co.lazuly.users.model.Role;
import co.lazuly.users.model.User;
import co.lazuly.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

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

    public User get(String email) {
        return repo.findOneByEmail(email);
    }

    public List<User> getSchoolUsers(Long schoolId) {
        return repo.findBySchoolId(schoolId);
    }

    public User change(final Long schoolId, final User user) {
        User old = repo.findBySchoolIdAndEmail(schoolId, user.getEmail());
        if (!isNull(old)) {
            repo.save(user);
        }
        return old;
    }
}
