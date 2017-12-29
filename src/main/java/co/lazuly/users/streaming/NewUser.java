package co.lazuly.users.streaming;

import co.lazuly.users.resources.requests.NewUserRequest;

import java.util.List;

/**
 * Created by boot on 26/12/2017.
 */
public class NewUser extends NewUserRequest {
    private final Long schoolId;

    public NewUser() {
        super();
        this.schoolId = null;
    }

    public NewUser(String firstName, String lastName, String email, List<String> roles, Long schoolId) {
        super(firstName, lastName, email, "", roles);
        this.schoolId = schoolId;
    }

    public Long getSchoolId() {
        return schoolId;
    }
}
