package com.zhy.test.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;

import static io.jsonwebtoken.Claims.SUBJECT;

@Slf4j
@Component
public class JwtTokenProvider {

    public static String jwtTokenSecret="englishand";
    public static long tokenExpiredMs=3*60*1000;
    public static final String TOKEN_HEADER="Authorization";
    public static final String TOKEN_PERFIX="Bearer ";


    public void setJwtTokenSecret(String jwtTokenSecret) {
        JwtTokenProvider.jwtTokenSecret = jwtTokenSecret;
    }

    public static Long getTokenExpiredMs() {
        return tokenExpiredMs;
    }

    public void setTokenExpiredMs(Long tokenExpiredMs) {
        JwtTokenProvider.tokenExpiredMs = tokenExpiredMs;
    }

    public static String generateJsonWebToken(UserDetails userDetails) {

        Map<String,Object> map = new HashMap<>();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        Iterator iterator = authorities.iterator();
        String role = "";
        if(iterator.hasNext()){
            role = iterator.next().toString();
        }
        map.put("rol", role);
        String token = Jwts
                .builder()
                .setSubject(SUBJECT)
                .setClaims(map)
                .claim("username", userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiredMs))
                .signWith(SignatureAlgorithm.HS256, jwtTokenSecret).compact();
        return token;
    }

    public static String createJwtToken(String username,String role ){
        Map<String,Object> map = new HashMap<>();
        map.put("rol",role);

        String token =
                Jwts.builder()
                .setSubject(SUBJECT)
                .setClaims(map)
                .claim("username",username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+tokenExpiredMs))
                .signWith(SignatureAlgorithm.HS256,jwtTokenSecret).compact();
        return token;
    }

    public static boolean validateToken(String token){
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

    public static boolean isExpiration(String token){
        Claims claims = Jwts.parser().setSigningKey(jwtTokenSecret)
                .parseClaimsJws(token).getBody();
        return claims.getExpiration().before(new Date());
    }
}
