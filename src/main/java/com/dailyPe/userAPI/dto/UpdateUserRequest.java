package com.dailyPe.userAPI.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class UpdateUserRequest {
    private List<String> user_ids;
    private List<Map<String, String>> update_data;
}
