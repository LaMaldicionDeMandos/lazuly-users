package co.lazuly.users.utils;

import co.lazuly.users.security.Role;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.List;


/**
 * Created by boot on 25/12/2017.
 */
@Component
public class UserTokenUtils {
    private final TypeToken<List<Role>> typeToken = new TypeToken<List<Role>>(List.class) { };

    private final Gson gson = new Gson();

    @Value("${app.security.clientkey}")
    private String clientKey;

    private Claims claims(final OAuth2Authentication user) throws UnsupportedEncodingException {
        OAuth2AuthenticationDetails detail = (OAuth2AuthenticationDetails)user.getDetails();
        String token = detail.getTokenValue();
        Claims claims = Jwts.parser().setSigningKey(clientKey.getBytes("UTF-8"))
                .parseClaimsJws(token).getBody();
        return claims;
    }

    public Long getSchoolId(OAuth2Authentication user) {
        try{
            return Long.valueOf(claims(user).get("school_id").toString());
        } catch (Exception e){
            return null;
        }
    }

    public List<Role> getRoles(OAuth2Authentication user) {
        try {
            String roles = claims(user).get("roles", List.class).toString();
            return gson.fromJson(roles, typeToken.getType());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
