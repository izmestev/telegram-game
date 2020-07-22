package ru.kosstantine.stainless.steel.rat.bot;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBotRegistrar implements ApplicationListener<ContextRefreshedEvent> {

    private final StainlessSteelRatBot bot;

    @Override
    @SneakyThrows
    public void onApplicationEvent(@NonNull ContextRefreshedEvent contextRefreshedEvent) {
        new TelegramBotsApi().registerBot(bot);
        log.info("Telegram bot registered");
    }

}
