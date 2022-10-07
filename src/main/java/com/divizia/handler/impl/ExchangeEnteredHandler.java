package com.divizia.handler.impl;

import com.divizia.enums.ConversationState;
import com.divizia.handler.UserRequestHandler;
import com.divizia.helper.KeyboardHelper;
import com.divizia.model.UserRequest;
import com.divizia.model.UserSession;
import com.divizia.model.service.OrderService;
import com.divizia.service.TelegramService;
import com.divizia.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class ExchangeEnteredHandler extends UserRequestHandler {

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;
    private OrderService orderService;

    public ExchangeEnteredHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService, OrderService orderService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
        this.orderService = orderService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate())
                && ConversationState.WAITING_FOR_EXCHANGE.equals(userRequest.getUserSession().getState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        String exchange = userRequest.getUpdate().getMessage().getText();

        UserSession session = userRequest.getUserSession();
        session.getOrder().setExchange(exchange);
        session.setState(ConversationState.WAITING_FOR_STOCK);
        userSessionService.saveSession(userRequest.getChatId(), session);

        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildStocksMenu(orderService.findByUserIdLast(session.getUser().getId()));
        telegramService.sendMessage(userRequest.getChatId(),
                "✍️Write stock you want to buy or choose from history ⤵️",
                replyKeyboardMarkup);
    }

    @Override
    public boolean isGlobal() {
        return false;
    }

}
