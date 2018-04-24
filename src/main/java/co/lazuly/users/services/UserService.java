package co.lazuly.users.services;

import co.lazuly.users.model.Profile;
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
                       final String jobTitle, final List<Role> roles) {
        User user = new User(schoolId, email, firstName, lastName, jobTitle, roles);
        user = repo.save(user);
        return user;
    }

    public User get(String email) {
        return repo.findOneByEmail(email);
    }

    public List<User> getSchoolUsers(Long schoolId) {
        return repo.findBySchoolId(schoolId);
    }

    public List getOnlySchoolAdminUser(Long schoolId) {
        return repo.findNotTeacherAndNotParents(schoolId);
    }

    public List getTeachers(Long schoolId) {
        return repo.findTeachers(schoolId);
    }

    public User updateProfile(final Long schoolId, final String email, final Profile profile) {
        User user = repo.findBySchoolIdAndEmail(schoolId, email);
        if (!isNull(user)) {
            user.updateProfile(profile);
            repo.save(user);
        }
        return user;
    }

    public User change(final Long schoolId, final User user) {
        User old = repo.findBySchoolIdAndEmail(schoolId, user.getEmail());
        if (!isNull(old)) {
            repo.save(user);
        }
        return old;
    }

    public User delete(final Long schoolId, final String email) {
        User user = repo.findBySchoolIdAndEmail(schoolId, email);
        if (!isNull(user) && !user.owner()) {
            repo.delete(user);
            return user;
        }
        return null;
    }
}
