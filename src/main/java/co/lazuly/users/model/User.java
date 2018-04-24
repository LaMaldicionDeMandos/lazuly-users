package co.lazuly.users.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

/**
 * Created by boot on 25/12/2017.
 */
@Document(collection = "profiles")
public class User {

    public static final String OWNER = "owner";
    @Id
    private final String email;

    @Indexed(name = "school_index")
    @NotNull(message = "Field school cant be null")
    private final Long schoolId;

    private final String firstName;
    private final String lastName;
    private final String jobTitle;
    private final List<Role> roles;
    private Profile profile;

    public User() {
        this.schoolId = null;
        this.email = null;
        this.firstName = null;
        this.lastName = null;
        this.jobTitle = null;
        this.roles = null;
        this.profile = null;
    }

    public User(final Long schoolId, final String email, final String firstName, final String lastName,
                final String jobTitle, final List<Role> roles) {
        this.schoolId = schoolId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = isNull(jobTitle) ? "" : jobTitle;
        this.roles = roles;
        this.profile = null;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public boolean owner() {
        return roles.stream().anyMatch((role) -> role.getCode().equals(OWNER));
    }

    public Profile updateProfile(final Profile newProfile) {
        final Profile profile = isNull(this.profile) ? newProfile : new Profile(
                isNull(newProfile.getEmail()) ? this.profile.getEmail() : newProfile.getEmail(),
                isNull(newProfile.getPhones()) ? this.profile.getPhones() : newProfile.getPhones(),
                isNull(newProfile.getPicture()) ? this.profile.getPicture() : newProfile.getPicture());
        this.setProfile(profile);
        return profile;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (schoolId != null ? !schoolId.equals(user.schoolId) : user.schoolId != null) return false;
        if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) return false;
        if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) return false;
        if (jobTitle != null ? !jobTitle.equals(user.jobTitle) : user.jobTitle != null) return false;
        return roles != null ? roles.equals(user.roles) : user.roles == null;
    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (schoolId != null ? schoolId.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (jobTitle != null ? jobTitle.hashCode() : 0);
        result = 31 * result + (roles != null ? roles.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", schoolId=" + schoolId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", roles=" + roles +
                '}';
    }
}
