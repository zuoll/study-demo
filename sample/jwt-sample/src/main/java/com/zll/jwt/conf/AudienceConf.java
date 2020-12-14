package com.zll.jwt.conf;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "audience")
public class AudienceConf {

    /**
     *
     */
    private String clientId;
    /**
     * 密钥, 经过Base64加密
     */
    private String base64Secret;
    /**
     * JWT的签发主体，存入issuer
     */
    private String name;
    /**
     * token 过期时间
     */
    private Integer expiresSecond;
}
