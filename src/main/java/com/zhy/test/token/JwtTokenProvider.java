package com.zhy.test.token;

import com.zhy.test.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenProvider {

    private static String jwtTokenSecret;
    private static Long tokenExpiredMs;
    public static final String TOKEN_HEADER="Authorization";
    public static final String TOKEN_PERFIX="Bearer ";

    @Value("${jwtTokenSecret}")
    public void setJwtTokenSecret(String jwtTokenSecret) {
        this.jwtTokenSecret = jwtTokenSecret;
    }

    public Long getTokenExpiredMs() {
        return tokenExpiredMs;
    }

    @Value("${tokenExpiredMs}")
    public void setTokenExpiredMs(Long tokenExpiredMs) {
        this.tokenExpiredMs = tokenExpiredMs;
    }

    public String generateJsonWebToken(UserDetails userDetails) {

        Map<String,Object> map = new HashMap<>();
        map.put("rol", "rol");
        String token = Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .setClaims(map)
                .claim("username", userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiredMs))
                .signWith(SignatureAlgorithm.HS256, jwtTokenSecret).compact();
        return token;
    }

    public String createJwtToken(String username,String role ){
        log.info("********************************************");
        log.info(tokenExpiredMs.toString());
        log.info(jwtTokenSecret);
        log.info("********************************************");
        Map<String,Object> map = new HashMap<>();
        map.put("rol",role);

        String token =
                Jwts.builder()
                .setSubject(username)
                .setClaims(map)
                .claim("username",username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+tokenExpiredMs))
                .signWith(SignatureAlgorithm.HS256,jwtTokenSecret).compact();
        return token;
    }

    public boolean validateToken(String token){
        String VALIDATE_FAILED="validate_failed: ";
        try{
            Jwts.parser().setSigningKey(jwtTokenSecret)
                    .parseClaimsJws(token);
            return true;
        }catch (Exception e){
            log.info(VALIDATE_FAILED+e.getMessage());
            return false;
        }
    }

    public static String getUsername(String token){
        Claims claims = Jwts.parser().setSigningKey(jwtTokenSecret)
                .parseClaimsJws(token).getBody();
        return claims.get("username").toString();
    }

    public static String getUserRole(String token){
        Claims claims = Jwts.parser().setSigningKey(jwtTokenSecret)
                .parseClaimsJws(token).getBody();
        return claims.get("rol").toString();
    }

    public boolean isExpiration(String token){
        Claims claims = Jwts.parser().setSigningKey(jwtTokenSecret)
                .parseClaimsJws(token).getBody();
        return claims.getExpiration().before(new Date());
    }
}
