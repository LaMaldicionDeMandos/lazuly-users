package co.lazuly.users.security;

import com.google.common.collect.Iterables;

import java.util.Set;

import static com.google.common.collect.Iterables.any;

/**
 * Created by boot on 12/12/2017.
 */
public class Role {
    private final String code;

    private final String name;

    private Set<Permission> permissions;

    public Role() {
        code = null;
        name = null;
        permissions = null;
    }

    public Role(final String code, final String name, final Set<Permission> permissions) {
        this.code = code;
        this.name = name;
        this.permissions = permissions;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public boolean hasPermissionName(final String name) {
        return any(permissions, (permission) -> permission.getName().equals(name));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        if (code != null ? !code.equals(role.code) : role.code != null) return false;
        return name != null ? name.equals(role.name) : role.name == null;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Role{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", permissions=" + permissions +
                '}';
    }
}
