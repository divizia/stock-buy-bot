package com.divizia.model.service.impl;

import com.divizia.model.entity.User;
import com.divizia.model.repo.UserRepo;
import com.divizia.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserImpl implements UserService {

    private UserRepo userRepo;

    @Autowired
    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User saveAndFlush(User user) {
        return userRepo.saveAndFlush(user);
    }

    @Override
    public void delete(User user) {
        userRepo.delete(user);
    }

    @Override
    public void deleteById(long id) {
        userRepo.deleteById(id);
    }

    @Override
    public User getById(long id) {
        return userRepo.getById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }
}
