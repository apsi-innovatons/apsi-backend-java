package pl.innowacja.config.security;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.innowacja.repositories.UserRepository;

import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

  private final String jwtSecret = Base64.getEncoder().encodeToString("SECRET123123123123123".getBytes());
  private final UserRepository userRepository;

  public String getUserId(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(jwtSecret)
        .parseClaimsJws(token)
        .getBody();

    return claims.getSubject().split(",")[0];
  }

  public String getUsername(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(jwtSecret)
        .parseClaimsJws(token)
        .getBody();

    var userId = Integer.valueOf(claims.get("unique_name").toString());
    var user = userRepository.findById(userId)
        .orElseThrow(() -> new UsernameNotFoundException(String.format("User with id %d does not exist.", userId)));

    return user.getUsername();
  }

  public Date getExpirationDate(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(jwtSecret)
        .parseClaimsJws(token)
        .getBody();

    return claims.getExpiration();
  }

  public boolean validate(String token) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
      return true;
    } catch (SignatureException ex) {
      log.error("Invalid JWT signature - {}", ex.getMessage());
    } catch (MalformedJwtException ex) {
      log.error("Invalid JWT token - {}", ex.getMessage());
    } catch (ExpiredJwtException ex) {
      log.error("Expired JWT token - {}", ex.getMessage());
    } catch (UnsupportedJwtException ex) {
      log.error("Unsupported JWT token - {}", ex.getMessage());
    } catch (IllegalArgumentException ex) {
      log.error("JWT claims string is empty - {}", ex.getMessage());
    }
    return false;
  }

}
