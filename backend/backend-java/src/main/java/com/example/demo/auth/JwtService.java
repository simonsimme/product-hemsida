package com.example.demo.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Service
public class JwtService {
  private final Algorithm algo;
  private final long expiryMinutes;

  public JwtService(@Value("${app.jwt.secret}") String secret,
                    @Value("${app.jwt.expiryMinutes}") long expiryMinutes) {
    this.algo = Algorithm.HMAC256(secret);
    this.expiryMinutes = expiryMinutes;
  }

  public String generate(String subject, Set<String> roles) {
    Instant now = Instant.now();
    return JWT.create()
              .withSubject(subject)
              .withArrayClaim("roles", roles.toArray(new String[0]))
              .withIssuedAt(now)
              .withExpiresAt(now.plus(expiryMinutes, ChronoUnit.MINUTES))
              .sign(algo);
}

  public DecodedJWT verify(String token) {
    return JWT.require(algo).build().verify(token);
  }
}
