package co.lazuly.users.model;

import java.util.List;

/**
 * Created by boot on 24/04/2018.
 */
public class Profile {
    private final String email;
    private final List<String> phones;
    private final String picture;

    Profile() {
        this(null, null, null);
    }

    public Profile(String email, List<String> phones, String picture) {
        this.email = email;
        this.phones = phones;
        this.picture = picture;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getPhones() {
        return phones;
    }

    public String getPicture() {
        return picture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Profile profile = (Profile) o;

        if (email != null ? !email.equals(profile.email) : profile.email != null) return false;
        if (phones != null ? !phones.equals(profile.phones) : profile.phones != null) return false;
        return picture != null ? picture.equals(profile.picture) : profile.picture == null;
    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (phones != null ? phones.hashCode() : 0);
        result = 31 * result + (picture != null ? picture.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "email='" + email + '\'' +
                ", phones=" + phones +
                ", picture='" + picture + '\'' +
                '}';
    }
}
