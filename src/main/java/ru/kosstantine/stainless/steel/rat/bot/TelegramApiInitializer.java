package ru.kosstantine.stainless.steel.rat.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;

import javax.annotation.PostConstruct;

@Slf4j
@Component(TelegramApiInitializer.NAME)
public class TelegramApiInitializer {

    static final String NAME = "telegramApiInitializer";

    @PostConstruct
    public void initialize() {
        ApiContextInitializer.init();
        log.info("Telegram API initialized");
    }

}
