package com.example.demo.auth;


import com.example.demo.auth.dto.*;
import com.example.demo.entities.User;
import com.example.demo.repos.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

  private final UserRepository users;
  private final PasswordEncoder encoder;
  private final JwtService jwt;

  public AuthController(UserRepository users, PasswordEncoder encoder, JwtService jwt) {
    this.users = users; this.encoder = encoder; this.jwt = jwt;
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public AuthResponse register(@RequestBody @Valid RegisterRequest req) {
    if (users.existsByEmail(req.email())) {
      throw new IllegalArgumentException("Email already in use");
    }
    var u = User.builder()
      .id(UUID.randomUUID())
      .email(req.email().toLowerCase())
      .passwordHash(encoder.encode(req.password()))
      .role("USER")
      .createdAt(OffsetDateTime.now())
      .build();
    users.save(u);
    return new AuthResponse(jwt.generate(u.getId().toString(), Set.of("USER")), u.getId());
  }

  @PostMapping("/login")
  public AuthResponse login(@RequestBody @Valid LoginRequest req) {
    var u = users.findByEmail(req.email().toLowerCase())
      .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
      
    if (!encoder.matches(req.password(), u.getPasswordHash())) {
      throw new IllegalArgumentException("Invalid credentials");
    }
    return new AuthResponse(jwt.generate(u.getId().toString(), Set.of(u.getRole())), u.getId());
  }

  

  @GetMapping("/profile")
public String getProfile() {
    // testa
    return "This is a protected endpoint! You are authenticated.";
}

}
