package co.lazuly.users.repositories;

import co.lazuly.users.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by boot on 10/3/17.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findOneByEmail(String email);
    List<User> findBySchoolId(Long schoolId);
    @Query("{schoolId: ?0, $and: [{'roles.code': {$ne: 'teacher'}}, {'roles.code': {$ne: 'parent'}}]}")
    List<User> findNotTeacherAndNotParents(final Long schoolId);

    User findBySchoolIdAndEmail(final Long schoolId, final String email);
}
