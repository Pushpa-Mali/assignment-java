package com.dailyPe.userAPI.repository;

import com.dailyPe.userAPI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {


    List<User> findByMobileNumber(String mobNum);

    User findByUserId(UUID userId);

    List<User> findByManagerId(UUID managerId);

    int deleteByUserId(UUID userId);

    int deleteByMobileNumber(String mobNum);

//    User findByUserId(UUID userId);
}
