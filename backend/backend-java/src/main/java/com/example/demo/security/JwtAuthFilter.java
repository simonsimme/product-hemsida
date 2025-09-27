package com.example.demo.security;


import com.example.demo.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthFilter extends OncePerRequestFilter {
  private final JwtService jwtService;
  public JwtAuthFilter(JwtService jwtService) { this.jwtService = jwtService; }

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws ServletException, IOException {
    String header = req.getHeader("Authorization");
    if (header != null && header.startsWith("Bearer ")) {
      String token = header.substring(7);
      try {
        var decoded = jwtService.verify(token);
        var roles = Arrays.stream(decoded.getClaim("roles").asArray(String.class))
                  .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                  .collect(Collectors.toList());

        var auth = new UsernamePasswordAuthenticationToken(decoded.getSubject(), null, roles);
    System.out.println("JwtAuthFilter: authorities=" + roles);
        SecurityContextHolder.getContext().setAuthentication(auth);

      } catch (Exception ignored) { /* invalid token -> proceed unauthenticated */ }
    }
    chain.doFilter(req, res);
  }
}
