package com.divizia.handler.impl;

import com.divizia.enums.ConversationState;
import com.divizia.handler.UserRequestHandler;
import com.divizia.helper.KeyboardHelper;
import com.divizia.model.UserRequest;
import com.divizia.model.UserSession;
import com.divizia.model.entity.Order;
import com.divizia.model.entity.OrderStatus;
import com.divizia.model.entity.Role;
import com.divizia.model.entity.User;
import com.divizia.model.service.OrderService;
import com.divizia.service.TelegramService;
import com.divizia.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

@Component
public class MyOrdersHandler extends UserRequestHandler {

    public static String MY_ORDERS = "My orders";

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;
    private OrderService orderService;

    public MyOrdersHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService, OrderService orderService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
        this.orderService = orderService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate(), MY_ORDERS);
    }

    @Override
    public void handle(UserRequest userRequest) {
        UserSession userSession = userRequest.getUserSession();
        List<Order> orders = orderService.findByUserId(userSession.getUser().getId());

        orders.forEach(x -> {
            if (x.getStatus() == OrderStatus.WAIT)
                telegramService.sendMessage(userRequest.getChatId(), x.toString(), keyboardHelper.buildOrderInline(x));
            else
                telegramService.sendMessage(userRequest.getChatId(), x.toString());
        });

        ReplyKeyboard replyKeyboard = keyboardHelper.buildMainMenu(true);
        telegramService.sendMessage(userRequest.getChatId(), "Choose from the menu below ⤵️", replyKeyboard);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }

}
