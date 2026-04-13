package jwt.s_jwt.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class MainController {

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the API!";
    }

    @GetMapping("/user")
    public String userAccess() {
        return "User content with JWT";
    }

    @GetMapping("/special")
    public String specialAccess() {
        return "Special content with JWT";
    }

    @GetMapping("/admin")
    public String adminAccess() {
        return "Admin content - Only accessible by ADMIN role";
    }


}
