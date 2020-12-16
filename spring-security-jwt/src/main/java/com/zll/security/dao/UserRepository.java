package com.zll.security.dao;

import com.zll.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {


    Optional<User> findByUserName(String username);

    @Modifying
    @Transactional(rollbackOn = Exception.class)
    void deleteByUserName(String userName);

    boolean existsByUserName(String username);
}