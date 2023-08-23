package com.teamsupercat.roupangbackend.dto.OAuthToken;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "security.oauth2")
public class KakaoUriDto {
    private String client_id;
    private String client_secret;
    private String redirect_uri;
}
