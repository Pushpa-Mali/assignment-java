package com.dailyPe.userAPI.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserRequest {

    private String mob_num;
    private String user_id;
    private String manager_id;

}
