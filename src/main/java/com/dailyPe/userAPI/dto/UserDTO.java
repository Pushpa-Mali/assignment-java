package com.dailyPe.userAPI.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserDTO {

    private String fullName;
    private String mobileNumber;
    private String panNumber;
    private UUID managerId;
}
