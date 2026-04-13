package jwt.sbiyono.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MainController {

    @GetMapping("/")
    public ResponseEntity<String> welcomeMessage(){
        return ResponseEntity.ok("Welcome to the SBI Yono Spoof Banking API");
    }


}
