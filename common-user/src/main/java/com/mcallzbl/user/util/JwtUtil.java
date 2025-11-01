package com.mcallzbl.user.util;

import com.mcallzbl.common.BusinessException;
import com.mcallzbl.user.pojo.dto.TokenInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
/**
 * @author mcallzbl
 * @version 1.0
 * @since 2025/11/1
 */
@Component
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${jwt.secret:secure-secret-key-for-json-web-token-authentication}")
    private String secret;

    @Value("${jwt.issuer:lingnite}")
    private String issuer;

    @Getter
    @Value("${jwt.expiration:3600000}") // Access Token过期时间
    private long jwtExpiration;

    @Getter
    @Value("${jwt.refresh.expiration:604800000}") // Refresh Token过期时间
    private long refreshExpiration;

    /**
     * 生成访问令牌信息（Access Token Info）
     * 调用方自行构建claims，返回token和过期时间
     *
     * @param subject 主题（通常是用户ID或标识符）
     * @param claims 自定义声明
     * @return TokenInfo对象
     */
    public TokenInfo generateAccessToken(String subject, Map<String, Object> claims) {
        if (claims == null) {
            claims = new java.util.HashMap<>();
        }

        // 添加标准声明
        claims.put("jti", UUID.randomUUID().toString());
        claims.put("iat", Instant.now().getEpochSecond());
        claims.put("type", "access_token");
        val expiration = System.currentTimeMillis() + jwtExpiration;
        String token = Jwts.builder()
                .subject(subject)
                .issuer(issuer)
                .claims(claims)
                .expiration(new Date(expiration))
                .signWith(getSigningKey())
                .compact();

        return TokenInfo.builder()
                .token(token)
                .expiration(expiration)
                .expiresIn(getAccessTokenExpirationSeconds())
                .type("access_token")
                .build();
    }

    /**
     * 生成刷新令牌信息（Refresh Token Info）
     * 调用方自行构建claims，返回token和过期时间
     *
     * @param subject 主题（通常是用户ID或标识符）
     * @param claims 自定义声明
     * @return TokenInfo对象
     */
    public TokenInfo generateRefreshToken(String subject, Map<String, Object> claims) {
        if (claims == null) {
            claims = new java.util.HashMap<>();
        }

        val expiration = System.currentTimeMillis() + refreshExpiration;
        // 添加标准声明
        claims.put("jti", UUID.randomUUID().toString());
        claims.put("iat", Instant.now().getEpochSecond());
        claims.put("type", "refresh_token");

        String token = Jwts.builder()
                .subject(subject)
                .issuer(issuer)
                .claims(claims)
                .expiration(new Date(expiration))
                .signWith(getSigningKey())
                .compact();

        return TokenInfo.builder()
                .token(token)
                .expiration(expiration)
                .expiresIn(getRefreshTokenExpirationSeconds())
                .type("refresh_token")
                .build();
    }

    /**
     * 生成自定义令牌
     * 完全自定义的JWT生成方法
     *
     * @param subject 主题
     * @param claims 自定义声明
     * @param expiration 过期时间（毫秒）
     * @param issuer 发行者（可选）
     * @return JWT令牌
     */
    public String generateToken(String subject, Map<String, Object> claims, long expiration, String issuer) {
        if (claims == null) {
            claims = new java.util.HashMap<>();
        }

        JwtBuilder builder = Jwts.builder()
                .subject(subject)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey());

        if (issuer != null && !issuer.trim().isEmpty()) {
            builder.issuer(issuer);
        } else {
            builder.issuer(this.issuer);
        }

        return builder.compact();
    }

//    /**
//     * 生成OIDC ID Token
//     * 调用方自行构建用户信息claims
//     *
//     * @param subject 用户标识符（OpenID）
//     * @param clientId 客户端ID（应用ID）
//     * @param userClaims 用户信息声明
//     * @param nonce 随机数（可选）
//     * @return OIDC标准的JWT ID Token
//     */
//    public String generateIdToken(String subject, String clientId, Map<String, Object> userClaims, String nonce) {
//        Map<String, Object> claims = new java.util.HashMap<>();
//        long now = Instant.now().getEpochSecond();
//
//        // OIDC标准声明
//        claims.put("iss", issuer); // 发行者
//        claims.put("sub", subject); // 用户标识符（OpenID）
//        claims.put("aud", clientId); // 受众（客户端ID）
//        claims.put("exp", now + getAccessTokenExpirationSeconds()); // 过期时间
//        claims.put("iat", now); // 签发时间
//        claims.put("auth_time", now); // 认证时间
//        claims.put("jti", UUID.randomUUID().toString()); // JWT ID
//
//        // 可选声明
//        if (nonce != null && !nonce.trim().isEmpty()) {
//            claims.put("nonce", nonce);
//        }
//
//        // 合并用户信息声明
//        if (userClaims != null) {
//            claims.putAll(userClaims);
//        }
//
//        return Jwts.builder()
//                .subject(subject) // sub字段
//                .issuer(issuer) // iss字段
//                .audience().add(clientId).and() // aud字段
//                .claims(claims)
//                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
//                .signWith(getSigningKey())
//                .compact();
//    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 获取访问令牌过期时间（小时）
     *
     * @return 过期小时数
     */
    public long getExpirationHour() {
        return jwtExpiration / 3600000;
    }

    /**
     * 获取访问令牌过期时间（秒）
     *
     * @return 过期秒数
     */
    public long getAccessTokenExpirationSeconds() {
        return jwtExpiration / 1000;
    }

    /**
     * 获取刷新令牌过期时间（秒）
     *
     * @return 过期秒数
     */
    public long getRefreshTokenExpirationSeconds() {
        return refreshExpiration / 1000;
    }

    /**
     * 获取访问令牌过期时间（LocalDateTime）
     *
     * @return 过期时间
     */
    public LocalDateTime getAccessTokenExpirationTime() {
        return LocalDateTime.now().plusSeconds(getAccessTokenExpirationSeconds());
    }

    /**
     * 获取刷新令牌过期时间（LocalDateTime）
     *
     * @return 过期时间
     */
    public LocalDateTime getRefreshTokenExpirationTime() {
        return LocalDateTime.now().plusSeconds(getRefreshTokenExpirationSeconds());
    }

    /**
     * 提取主题（通常是用户ID）
     *
     * @param token JWT令牌
     * @return 主题字符串
     */
    public String extractSubject(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            throw new BusinessException("Token无效");
        }
    }

    /**
     * 提取指定键的值
     *
     * @param token JWT令牌
     * @param key 键名
     * @return 值
     */
    public Object extractClaim(String token, String key) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.get(key);
        } catch (Exception e) {
            throw new BusinessException("Token无效");
        }
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
