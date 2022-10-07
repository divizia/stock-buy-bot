package com.divizia.handler.impl;

import com.divizia.handler.UserRequestHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import com.divizia.enums.ConversationState;
import com.divizia.helper.KeyboardHelper;
import com.divizia.model.UserRequest;
import com.divizia.model.UserSession;
import com.divizia.service.TelegramService;
import com.divizia.service.UserSessionService;

import static com.divizia.constant.Constants.BTN_CANCEL;

@Component
public class CancelHandler extends UserRequestHandler {

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public CancelHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate(), BTN_CANCEL);
    }

    @Override
    public void handle(UserRequest userRequest) {
        UserSession userSession = userRequest.getUserSession();
        userSession.setState(ConversationState.CONVERSATION_STARTED);
        userSessionService.saveSession(userSession.getChatId(), userSession);

        ReplyKeyboard replyKeyboard = keyboardHelper.buildMainMenu(userSession.getUser() != null);
        telegramService.sendMessage(userRequest.getChatId(),
                "Choose from the menu below ⤵️", replyKeyboard);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
