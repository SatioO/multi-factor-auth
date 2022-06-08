package com.ifsg.multifactorauth.security;

import com.ifsg.multifactorauth.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    private final JwtConfig jwtConfig;

    public JwtTokenUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String getUsernameFromToken(String token) throws NoSuchAlgorithmException, InvalidKeySpecException  {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) throws NoSuchAlgorithmException, InvalidKeySpecException  {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return Jwts.parserBuilder().setSigningKey(SecurityConfig.getPublicKey(jwtConfig.getSecret())).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) throws NoSuchAlgorithmException, InvalidKeySpecException  {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
