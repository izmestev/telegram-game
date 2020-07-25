package ru.kosstantine.stainless.steel.rat.bot.message;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.kosstantine.stainless.steel.rat.bot.model.Button;
import ru.kosstantine.stainless.steel.rat.bot.model.Chapter;

import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
public class MessageCreator {

    public static AnswerCallbackQuery callback(CallbackQuery callback) {
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(callback.getId());
        answer.setShowAlert(false);
        return answer;
    }

    public static SendMessage message(Message incomeMessage, Chapter chapter) {
        SendMessage message = new SendMessage(incomeMessage.getChatId(), chapter.getText());
        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(createButtons(chapter));
        message.setReplyMarkup(markupKeyboard);
        return message;
    }

    private static List<List<InlineKeyboardButton>> createButtons(Chapter chapter) {
        return chapter.getButtons().stream()
                .sorted(Comparator.comparing(Button::getId))
                .map(MessageCreator::createButton)
                .map(List::of)
                .collect(Collectors.toList());
    }

    private static InlineKeyboardButton createButton(Button button) {
        String text = button.getSmile() + " " + button.getText();
        Long redirect = getRedirect(button.getRedirect());
        return new InlineKeyboardButton(text)
                .setCallbackData(String.valueOf(redirect));
    }

    private static Long getRedirect(Set<Long> redirects) {
        int size = redirects.size();
        int index = size > 1 ? new Random().nextInt(size - 1) : 0;
        return new ArrayList<>(redirects).get(index);
    }

}
