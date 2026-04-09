package com.nazmul.polling.repository;

import com.nazmul.polling.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
