package com.divizia.handler.impl.signUp;

import com.divizia.enums.ConversationState;
import com.divizia.handler.UserRequestHandler;
import com.divizia.helper.KeyboardHelper;
import com.divizia.model.UserRequest;
import com.divizia.model.UserSession;
import com.divizia.model.entity.Role;
import com.divizia.model.entity.User;
import com.divizia.service.TelegramService;
import com.divizia.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.ArrayList;

@Component
public class SignUpHandler extends UserRequestHandler {

    public static String SIGN_UP = "Sign up";

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public SignUpHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate(), SIGN_UP);
    }

    @Override
    public void handle(UserRequest userRequest) {
        UserSession userSession = userRequest.getUserSession();
        User user = new User();
        user.setId(userSession.getUserId());
        user.setRole(Role.USER);
        userSession.setUser(user);
        userSession.setState(ConversationState.WAITING_FOR_FIRSTNAME);
        userSessionService.saveSession(userSession.getChatId(), userSession);

        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMenuWithCancel();
        telegramService.sendMessage(userRequest.getChatId(),"✍️Please enter your first name ⤵️", replyKeyboardMarkup);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }

}
