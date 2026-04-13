package jwt.s_jwt.controller;

import jwt.s_jwt.entity.ERole;
import jwt.s_jwt.entity.Role;
import jwt.s_jwt.entity.User;
import jwt.s_jwt.repository.RoleRepository;
import jwt.s_jwt.repository.UserRepository;
import jwt.s_jwt.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private RoleRepository roleRepository;


    @Autowired

    public AuthenticationController(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.roleRepository = roleRepository;
    }



    @PostMapping("/signin")
    public String authenticateUser(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtUtil.generateToken(userDetails.getUsername());
    }

    @PostMapping("/signup")
    public String registerUser(@RequestBody User user, @RequestParam(defaultValue = "user") String role) {
        if(userRepository.existsByUsername(user.getUsername())) {
            return "User Already exists";
        }

        Set<Role> roles = new HashSet<>();
        if(role.equalsIgnoreCase("admin")) {
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role ADMIN is not found."));
            roles.add(adminRole);
        } else {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role USER is not found."));
            roles.add(userRole);
        }

        final User newUser = new User(null, user.getUsername(), passwordEncoder.encode(user.getPassword()), roles);
        userRepository.save(newUser);
        return "User registered Successfully with " + role.toUpperCase() + " role!";
    }









}
