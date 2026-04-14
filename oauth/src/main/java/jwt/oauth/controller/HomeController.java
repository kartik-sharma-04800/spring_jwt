package jwt.oauth.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "Welcome! Please <a href='/oauth2/authorization/github'>Login with GitHub</a>";
    }

    @GetMapping("/home")
    public Map<String, Object> home(@AuthenticationPrincipal OAuth2User principal) {
        return Map.of(
            "message", "Successfully logged in with GitHub!",
            "user", principal.getAttributes()
        );
    }

    @GetMapping("/user")
    public OAuth2User user(@AuthenticationPrincipal OAuth2User principal) {
        return principal;
    }
}
