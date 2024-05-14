package com.dailyPe.userAPI.controller;

import com.dailyPe.userAPI.dto.DeleteUserRequest;
import com.dailyPe.userAPI.dto.GetUserRequest;
import com.dailyPe.userAPI.dto.UpdateUserRequest;
import com.dailyPe.userAPI.dto.UserDTO;
import com.dailyPe.userAPI.entity.User;
import com.dailyPe.userAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create_user")
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO){
        String response = userService.createUser(userDTO);
        if(response.startsWith("Invalid")){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update_user")
    public ResponseEntity<StringBuilder> updateUser(@RequestBody UpdateUserRequest request) {

            StringBuilder response = userService.updateUser(request);
            String resp=response.toString();
            if(resp.startsWith("Invalid")){
                return ResponseEntity.badRequest().body(response);
             }
                return ResponseEntity.ok(response);

    }

    @PostMapping("/get_users")
    public ResponseEntity<?> getUsers(@RequestBody Optional<GetUserRequest> getUserRequest) {
        if(getUserRequest.isPresent()) {
            if(getUserRequest.get().getUser_id() != null){
                User user = userService.getUser(getUserRequest.get());
                if(user!=null){
                return ResponseEntity.ok().body(user);}
                else{
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                }
            }
            List<User> users = userService.getUsers(getUserRequest.get());
            if (!users.isEmpty()) {
                return ResponseEntity.ok().body(users);
            }

            else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        }
        else{
            List<User> allUsers = userService.getAllUsers();
            if (allUsers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.ok().body(allUsers);
            }
        }
    }

    @PostMapping("/delete_user")
    public ResponseEntity<String> deleteUser(@RequestBody DeleteUserRequest request) {
        boolean deleted = userService.deleteUser(request);
        if (deleted) {
            return ResponseEntity.ok("User deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

}
