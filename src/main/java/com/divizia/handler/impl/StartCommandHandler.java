package com.divizia.handler.impl;

import com.divizia.enums.ConversationState;
import com.divizia.handler.UserRequestHandler;
import com.divizia.helper.KeyboardHelper;
import com.divizia.model.UserRequest;
import com.divizia.model.UserSession;
import com.divizia.model.entity.User;
import com.divizia.service.TelegramService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Component
public class StartCommandHandler extends UserRequestHandler {

    private static String command = "/start";

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;

    public StartCommandHandler(TelegramService telegramService, KeyboardHelper keyboardHelper) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isCommand(userRequest.getUpdate(), command);
    }

    @Override
    public void handle(UserRequest request) {

        telegramService.sendMessage(request.getChatId(), "\uD83D\uDC4B Hello! I can help you order stocks!");

        ReplyKeyboard replyKeyboard = keyboardHelper.buildMainMenu(request.getUserSession().getUser() != null);
        telegramService.sendMessage(request.getChatId(), "Choose from the menu below ⤵️", replyKeyboard);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
