package com.zll.jwt.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.zll.jwt.conf.AudienceConf;
import com.zll.jwt.exception.BizException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Date;

/**
 * <pre>
 *     JWT验证主要是通过过滤器验证，所以我们需要添加一个拦截器来演请求头中是否包含有后台颁发的 token，
 *     这里请求头的格式：Authorization: Bearer <token>
 * </pre>
 *
 * @see Base64 这个是一个对称加密类
 */
@Slf4j
public class JwtTokenUtil {

    public static final String AUTH_HEADER_KEY = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer" + StrUtil.SPACE;
    /**
     *
     */
    public static final String CLAIM_USER_ID = "userId";
    /**
     *
     */
    public static final String CLAIM_ROLE = "role";


    /**
     * 构建jwt
     */
    public static String createJWT(String userId, String userName, String role, AudienceConf audienceConf) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMills = System.currentTimeMillis();
        Date now = new Date(nowMills);
        //生产签名密匙
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(audienceConf.getBase64Secret());
        SecretKeySpec signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //userId是重要信息，进行加密下
        String encryId = Base64.encode(userId);


        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                // 可以将基本不重要的对象信息放到claims
                .claim(CLAIM_ROLE, role)
                .claim(CLAIM_USER_ID, encryId)
                .setSubject(userName)           // 代表这个JWT的主体，即它的所有人
                .setIssuer(audienceConf.getClientId())              // 代表这个JWT的签发主体；
                .setIssuedAt(new Date())        // 是一个时间戳，代表这个JWT的签发时间；
                .setAudience(audienceConf.getName())          // 代表这个JWT的接收对象；
                .signWith(signatureAlgorithm, signingKey);


        //添加token 过期时间
        Integer TTLMillis = audienceConf.getExpiresSecond();
        if (ObjectUtil.isNotNull(TTLMillis) && TTLMillis > 0) {
            long expireMills = nowMills + TTLMillis;
            Date expDate = new Date(expireMills);

            builder.setExpiration(expDate)  // 是一个时间戳，代表这个JWT的过期时间；
                    .setNotBefore(now); // 是一个时间戳，代表这个JWT生效的开始时间，意味着在这个时间之前验证JWT是会失败的
        }

        return builder.compact();
    }

    /**
     * 解析jwt
     */
    public static Claims parseJWT(String token, String base64Security) {
        try {
            JwtParser jwtParser = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(base64Security));

            Claims body = jwtParser.parseClaimsJws(token).getBody();
            return body;
        } catch (ExpiredJwtException e) {
            new BizException(100, "ExpiredJwtException");
        } catch (UnsupportedJwtException e) {
            new BizException(101, "UnsupportedJwtException");
        } catch (MalformedJwtException e) {
            new BizException(102, "ExpiredJwtException");
        } catch (SignatureException e) {
            new BizException(103, "SignatureException");
        } catch (IllegalArgumentException e) {
            new BizException(104, "IllegalArgumentException");
        }
        return null;
    }

    /**
     * 从token中获取用户名
     */
    public static String getUserName(String token, String base64Security) {
        return parseJWT(token, base64Security).getSubject();
    }

    /**
     * 从token中获取用户ID
     */
    public static String getUserId(String token, String base64Security) {

        String userId = parseJWT(token, base64Security).get(CLAIM_USER_ID, String.class);
        //因为userId 在创建时加密过了的
        return Base64Util.decode(userId);
    }


    /**
     * 是否已过期
     */
    public static boolean isExpire(String token, String base64Security) {
        return parseJWT(token, base64Security).getExpiration().before(new Date());
    }

}
