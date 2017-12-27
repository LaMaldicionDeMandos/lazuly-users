package co.lazuly.users.repositories;

import co.lazuly.users.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by boot on 10/3/17.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    List<User> findBySchoolId(Long schoolId);
}
