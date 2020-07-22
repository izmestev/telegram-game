package ru.kosstantine.stainless.steel.rat.bot;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.kosstantine.stainless.steel.rat.bot.model.Button;
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
        Message receivedMessage = Optional.ofNullable(update.getMessage())
                .flatMap(m -> Optional.ofNullable(update.getCallbackQuery())
                        .map(CallbackQuery::getMessage))
                .orElseThrow(NoSuchElementException::new);

        Long id = Optional.of(receivedMessage)
                .map(Message::getText)
                .filter(START::equals)
                .map(text -> 0L)
                .orElse(getChapterId(update));

        Optional<Chapter> chapter = repository.findById(id);
        chapter.ifPresent(c -> sendMessage(c, receivedMessage.getChatId()));

    }

    private Long getChapterId(Update update) {
        return Optional.ofNullable(update.getCallbackQuery())
                .map(CallbackQuery::getData)
                .map(Long::valueOf).orElse(-1L);
    }

    private Long getRedirect(List<Long> redirects) {
        int size = redirects.size();
        int index = size > 1 ? new Random().nextInt(size - 1) : 0;
        return redirects.get(index);
    }

    private List<List<InlineKeyboardButton>> createButtons(Chapter chapter) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        for (Button button : chapter.getButtons()) {
            String text = button.getSmile() + " " + button.getText();
            Long redirect = getRedirect(button.getRedirect());
            InlineKeyboardButton keyboardButton = new InlineKeyboardButton(text)
                    .setCallbackData(String.valueOf(redirect));
            buttons.add(Collections.singletonList(keyboardButton));
        }
        
        return buttons;
    }

    @SneakyThrows
    private void sendMessage(Chapter chapter, Long chatId) {
        SendMessage message = new SendMessage(chatId, chapter.getText());
        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(createButtons(chapter));
        message.setReplyMarkup(markupKeyboard);
        execute(message);
    }

}
