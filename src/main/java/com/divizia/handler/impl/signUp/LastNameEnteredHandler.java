package com.divizia.handler.impl.signUp;

import com.divizia.enums.ConversationState;
import com.divizia.handler.UserRequestHandler;
import com.divizia.helper.KeyboardHelper;
import com.divizia.model.UserRequest;
import com.divizia.model.UserSession;
import com.divizia.service.TelegramService;
import com.divizia.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class LastNameEnteredHandler extends UserRequestHandler {

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public LastNameEnteredHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate())
                && ConversationState.WAITING_FOR_LASTNAME.equals(userRequest.getUserSession().getState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        String lastName = userRequest.getUpdate().getMessage().getText();

        UserSession session = userRequest.getUserSession();
        session.getUser().setLastName(lastName);
        session.setState(ConversationState.WAITING_FOR_OFFICE_ID);
        userSessionService.saveSession(userRequest.getChatId(), session);

        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMenuWithCancel();
        telegramService.sendMessage(userRequest.getChatId(),
                "✍️Please enter your office id ⤵️",
                replyKeyboardMarkup);
    }

    @Override
    public boolean isGlobal() {
        return false;
    }

}
