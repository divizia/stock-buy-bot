package com.divizia.handler.impl;

import com.divizia.enums.ConversationState;
import com.divizia.handler.UserRequestHandler;
import com.divizia.helper.KeyboardHelper;
import com.divizia.model.UserRequest;
import com.divizia.model.UserSession;
import com.divizia.model.entity.Order;
import com.divizia.model.service.OrderService;
import com.divizia.service.TelegramService;
import com.divizia.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class AmountEnteredHandler extends UserRequestHandler {

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;
    private OrderService orderService;

    public AmountEnteredHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService, OrderService orderService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
        this.orderService = orderService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate())
                && ConversationState.WAITING_FOR_AMOUNT.equals(userRequest.getUserSession().getState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        UserSession userSession = userRequest.getUserSession();

        try {
            userSession.getOrder().setAmount(Integer.parseInt(userRequest.getUpdate().getMessage().getText()));
        } catch (NumberFormatException e) {
            ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMenuWithCancel();
            telegramService.sendMessage(userRequest.getChatId(),
                    "This amount is not valid. Please use only digits",
                    replyKeyboardMarkup);
            return;
        }

        orderService.saveAndFlush(userSession.getOrder());

        userSession.setState(ConversationState.CONVERSATION_STARTED);
        userSessionService.saveSession(userRequest.getChatId(), userSession);

        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMainMenu(userSession.getUser() != null);
        telegramService.sendMessage(userRequest.getChatId(),"Congrats, your order has been sent to manager!", replyKeyboardMarkup);
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
