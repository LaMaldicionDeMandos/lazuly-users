package co.lazuly.users.restclients;

import co.lazuly.users.model.Role;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by boot on 15/12/2017.
 */
@FeignClient("lazuly-auth")
public interface RolesFeignClient {
    @RequestMapping(method= RequestMethod.GET, value="/roles", consumes="application/json")
    List<Role> get(@RequestParam Map<String, ?> codes, @RequestHeader("X-Authorization-Secret") String secret);
}
