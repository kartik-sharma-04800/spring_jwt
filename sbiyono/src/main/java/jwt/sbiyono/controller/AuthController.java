package jwt.sbiyono.controller;

import jakarta.validation.Valid;
import jwt.sbiyono.dto.ApiResponse;
import jwt.sbiyono.dto.LoginRequest;
import jwt.sbiyono.dto.RegisterRequest;
import jwt.sbiyono.security.JwtUtil;
import jwt.sbiyono.service.BankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private BankingService bankingService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        ApiResponse response = bankingService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        ApiResponse authResponse = bankingService.authenticate(request);
        
        if (authResponse.isSuccess()) {
            String token = jwtUtil.generateToken(request.getUsername());
            return ResponseEntity.ok(new ApiResponse(true, "Login successful", token));
        }
        
        return ResponseEntity.ok(authResponse);
    }
}
