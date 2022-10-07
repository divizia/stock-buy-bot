package com.divizia.model.repo;

import com.divizia.model.entity.Order;
import com.divizia.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}
