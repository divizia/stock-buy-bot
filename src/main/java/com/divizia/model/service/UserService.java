package com.divizia.model.service;

import com.divizia.model.entity.Order;
import com.divizia.model.entity.User;
import org.springframework.lang.Nullable;

import java.util.List;

public interface UserService {

    User saveAndFlush(User user);
    void delete(User user);
    void deleteById(long id);
    @Nullable
    User getById(long id);
    List<User> findAll();

}
