package com.divizia.handler.impl;

import com.divizia.enums.ConversationState;
import com.divizia.handler.UserRequestHandler;
import com.divizia.helper.KeyboardHelper;
import com.divizia.model.UserRequest;
import com.divizia.model.UserSession;
import com.divizia.model.entity.Order;
import com.divizia.model.entity.OrderStatus;
import com.divizia.service.TelegramService;
import com.divizia.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class NewOrderHandler extends UserRequestHandler {

    public static String NEW_ORDER = "❗️New order";

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public NewOrderHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate(), NEW_ORDER);
    }

    @Override
    public void handle(UserRequest userRequest) {
        UserSession userSession = userRequest.getUserSession();
        Order order = new Order();
        order.setUser(userSession.getUser());
        order.setStatus(OrderStatus.WAIT);
        userSession.setOrder(order);
        userSession.setState(ConversationState.WAITING_FOR_EXCHANGE);
        userSessionService.saveSession(userSession.getChatId(), userSession);

        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildExchangesMenu();
        telegramService.sendMessage(userRequest.getChatId(),"Choose an exchange or write it manually ⤵️", replyKeyboardMarkup);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }

}
