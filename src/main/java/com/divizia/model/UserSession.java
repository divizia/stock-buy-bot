package com.divizia.model;

import com.divizia.enums.ConversationState;
import com.divizia.model.entity.Order;
import com.divizia.model.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSession {
    private Long chatId;
    private ConversationState state;
    private Long userId;
    private User user;
    private Order order;
}
