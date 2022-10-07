package com.divizia.handler.impl.signUp;

import com.divizia.enums.ConversationState;
import com.divizia.handler.UserRequestHandler;
import com.divizia.helper.KeyboardHelper;
import com.divizia.model.UserRequest;
import com.divizia.model.UserSession;
import com.divizia.model.service.UserService;
import com.divizia.service.TelegramService;
import com.divizia.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class OfficeIdEnteredHandler extends UserRequestHandler {

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;
    private UserService userService;

    public OfficeIdEnteredHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService, UserService userService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
        this.userService = userService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate())
                && ConversationState.WAITING_FOR_OFFICE_ID.equals(userRequest.getUserSession().getState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        String officeId = userRequest.getUpdate().getMessage().getText();

        UserSession userSession = userRequest.getUserSession();
        try {
            userSession.getUser().setOfficeId(Integer.parseInt(officeId));
        } catch (NumberFormatException e) {
            ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMenuWithCancel();
            telegramService.sendMessage(userRequest.getChatId(),
                    "This office id is not valid. Please use only digits",
                    replyKeyboardMarkup);
            return;
        }

        userSession.setUser(userService.saveAndFlush(userSession.getUser()));

        userSession.setState(ConversationState.CONVERSATION_STARTED);
        userSessionService.saveSession(userRequest.getChatId(), userSession);

        ReplyKeyboard replyKeyboard = keyboardHelper.buildMainMenu(userSession.getUser() != null);
        telegramService.sendMessage(userRequest.getChatId(),
                "Thanks for sign up. Choose from the menu below ⤵️", replyKeyboard);
    }

    @Override
    public boolean isGlobal() {
        return false;
    }

}
