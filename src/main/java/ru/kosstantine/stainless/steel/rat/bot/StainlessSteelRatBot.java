package ru.kosstantine.stainless.steel.rat.bot;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.kosstantine.stainless.steel.rat.bot.message.MessageCreator;
import ru.kosstantine.stainless.steel.rat.bot.model.Chapter;
import ru.kosstantine.stainless.steel.rat.bot.repository.ChapterRepository;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
@DependsOn(TelegramApiInitializer.NAME)
public class StainlessSteelRatBot extends TelegramLongPollingBot {

    private final static String START = "/start";

    private final TelegramProperties properties;
    private final ChapterRepository repository;

    @Override
    public String getBotToken() {
        return properties.getToken();
    }

    @Override
    public String getBotUsername() {
        return properties.getName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Optional<CallbackQuery> callback = Optional.ofNullable(update.getCallbackQuery());

        // Send callback
        callback.map(MessageCreator::callback)
                .ifPresent(this::send);

        // Get message
        Message message = Optional.ofNullable(update.getMessage())
                .or(() -> callback.map(CallbackQuery::getMessage))
                .orElseThrow();

        // Get chapter id
        Long id = getNextChapterId(message, update);

        // Log chapter
        logUser(message.getChat(), id);

        // Find next chapter
        Optional<Chapter> chapter = repository.findById(id);

        // Send message
        chapter.map(c -> MessageCreator.message(message, c))
                .ifPresent(this::send);
    }

    @SneakyThrows
    private void send(BotApiMethod<?> method) {
        execute(method);
    }

    private void logUser(Chat chat, Long chapter) {
        log.info(String.format("User %s %s on chapter %d", chat.getFirstName(),
                StringUtils.defaultString(chat.getLastName()), chapter));
    }

    private Long getNextChapterId(Message message, Update update) {
        return Optional.of(message)
                .map(Message::getText)
                .filter(START::equals)
                .map(text -> 0L)
                .orElse(getChapterId(update));
    }

    private Long getChapterId(Update update) {
        return Optional.ofNullable(update.getCallbackQuery())
                .map(CallbackQuery::getData)
                .map(Long::valueOf).orElse(-1L);
    }

}
