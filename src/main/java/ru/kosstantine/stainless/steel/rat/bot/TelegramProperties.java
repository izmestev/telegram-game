package ru.kosstantine.stainless.steel.rat.bot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties("telegram")
public class TelegramProperties {

    private String token;
    private String name;

}
