package com.zhy.security.jwt;

import com.zhy.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtils {

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_ID = "id";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String CLAIM_KEY_ROLES = "roles";

    @Value("${jwt.token.secret}")
    private String secret;
    @Value("${jwt.token.expiration}")
    private int expiration;//过期时间，单位为秒，

    public String getUsernameFromToken(String token){
        String username = getClaimsFromToken(token).getSubject();
        return username;
    }

    public Date getCreateDateFromToken(String token){
        Date created ;
        final Claims claims = getClaimsFromToken(token);
        created = (Date) claims.get(CLAIM_KEY_CREATED);
        return created;
    }

    public Date getExpirationDateFromToken(String token){
        final Claims claims = getClaimsFromToken(token);
        Date expiration = claims.getExpiration();
        return expiration;
    }

    private Date generateExpirationDate(){
        return new Date(System.currentTimeMillis()+expiration*1000);
    }

    private boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(User userDetails){
        Map<String,Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME,userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED,new Date());
        claims.put(CLAIM_KEY_ID,userDetails.getId());
        claims.put(CLAIM_KEY_ROLES,userDetails.getAuthorities());
        return generateToken(claims);
    }

    public String generateToken(Map<String,Object> claims){
        return Jwts.builder().setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }

    public boolean canTokenBeRefreshed(String token){
        return !isTokenExpired(token);
    }

    public String refreshToken(String token){
        final Claims claims = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED,new Date());
        String refreshToken = generateToken(claims);
        return refreshToken;
    }

    public boolean validateToken(String token, UserDetails userDetails){
        User user = (User) userDetails;
        final String username = getUsernameFromToken(token);
        return (username.equals(user.getUsername()) && isTokenExpired(token)==false);
    }

    private Claims getClaimsFromToken(String token){
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims;
    }
}
