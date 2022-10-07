package com.divizia.service;

import com.divizia.model.UserSession;
import com.divizia.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserSessionService {

    private Map<Long, UserSession> userSessionMap = new HashMap<>();

    private UserService userService;

    public UserSessionService(UserService userService) {
        this.userService = userService;
    }

    public UserSession getSession(Long chatId, Long userId) {
        return userSessionMap.getOrDefault(chatId, UserSession
                .builder()
                .chatId(chatId)
                .userId(userId)
                .user(userService.getById(userId))
                .build());
    }

    public UserSession saveSession(Long chatId, UserSession session) {
        return userSessionMap.put(chatId, session);
    }
}
