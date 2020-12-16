package com.zll.security.common.utils;

import com.zll.security.common.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JwtTokenUtils {


    /**
     * 生成足够的安全随机密钥，以适合符合规范的签名
     */
    private static final byte[] API_KEY_SECRET_BYTES = DatatypeConverter.parseBase64Binary(SecurityConstants.JWT_SECRET_KEY);
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(API_KEY_SECRET_BYTES);


    /**
     * @param userId
     * @param username
     * @param roles
     * @param isRememberMe
     * @return
     */
    public static String createToken(String userId,String username,  List<String> roles, boolean isRememberMe) {
        long expiration = isRememberMe ? SecurityConstants.EXPIRATION_REMEMBER : SecurityConstants.EXPIRATION;
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);
        String tokenPrefix = Jwts.builder()
                .setHeaderParam("type", SecurityConstants.TOKEN_TYPE)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .claim(SecurityConstants.ROLE_CLAIMS, String.join(",", roles))
                .setId(userId)
                .setSubject(username)
                .setIssuer("SnailClimb")
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .compact();
        return SecurityConstants.TOKEN_PREFIX + tokenPrefix; // 添加 token 前缀 "Bearer ";
    }


    /**
     * @param token
     * @return
     */
    public static String getId(String token) {
        Claims claims = getClaims(token);
        return claims.getId();
    }

    /**
     * @param token
     * @return
     */
    public static UsernamePasswordAuthenticationToken getAuthentication(String token) {
        Claims claims = getClaims(token);
        List<SimpleGrantedAuthority> authorities = getAuthorities(claims);
        String userName = claims.getSubject();
        return new UsernamePasswordAuthenticationToken(userName, token, authorities);
    }

    private static List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        String role = (String) claims.get(SecurityConstants.ROLE_CLAIMS);
        return Arrays.stream(role.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private static Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
