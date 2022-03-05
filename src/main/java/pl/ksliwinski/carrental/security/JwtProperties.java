package pl.ksliwinski.carrental.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("jwt")
@Getter
@Setter
public class JwtProperties {
    private String tokenHeader;
    private String tokenPrefix;
    private String secret;
    private Long expirationTime;
}
