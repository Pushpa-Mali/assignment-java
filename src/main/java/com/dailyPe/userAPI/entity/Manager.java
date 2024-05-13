package com.dailyPe.userAPI.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID managerId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private boolean isActive;
}
