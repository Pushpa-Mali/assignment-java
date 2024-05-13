package com.dailyPe.userAPI.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String mobileNumber;

    @Column(nullable = false)
    private String panNumber;


    private UUID managerId;

    @Column(nullable = false)
    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean isActive = true;


}
