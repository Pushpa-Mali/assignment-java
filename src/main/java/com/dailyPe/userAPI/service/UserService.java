package com.dailyPe.userAPI.service;

import com.dailyPe.userAPI.dto.DeleteUserRequest;
import com.dailyPe.userAPI.dto.GetUserRequest;
import com.dailyPe.userAPI.dto.UserDTO;
import com.dailyPe.userAPI.entity.Manager;
import com.dailyPe.userAPI.repository.ManagerRepository;
import com.dailyPe.userAPI.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dailyPe.userAPI.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ManagerRepository managerRepository;

    public String createUser(UserDTO userDTO) {

        if(userDTO.getFullName() == null){
            return "Invalid ! Fullname must not be empty";
        }

        String panNum = userDTO.getPanNumber().toUpperCase();
        if (!isValidPANNumber(panNum)) {
            return "Invalid PAN number.";
        }

        String mobNum = adjustMobileNumber(userDTO.getMobileNumber());
        if (!isValidMobileNumber(mobNum)) {
            return "Invalid mobile number.";
        }

//        UUID managerId = UUID.fromString(userDTO.getManagerId());

        if(userDTO.getManagerId()!=null) {
            Manager manager = managerRepository.findById(userDTO.getManagerId()).orElse(null);
        if(manager == null || !manager.isActive()){
//            if (!manager.isActive()) {
                return "Invalid manager ID or manager is inactive.";
            }
        }

        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setFullName(userDTO.getFullName());
        user.setMobileNumber(userDTO.getMobileNumber());
        user.setPanNumber(userDTO.getPanNumber().toUpperCase());
        user.setManagerId(userDTO.getManagerId());
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(true);
        userRepository.save(user);

        return "User created successfully.";
    }


    private String adjustMobileNumber(String mobileNumber) {
        // Remove country code or prefix, if present
        if (mobileNumber.startsWith("0")) {
            return mobileNumber.substring(1);
        } else if (mobileNumber.startsWith("+91")) {
            return mobileNumber.substring(3);
        }
        return mobileNumber;
    }

    private boolean isValidMobileNumber(String mobileNumber) {
        return mobileNumber.matches("\\d{10}");
    }

    private boolean isValidPANNumber(String panNumber) {
        return panNumber.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}");
    }

    public List<User> getUsers(GetUserRequest request) {
        if (request.getMob_num() != null) {
            return userRepository.findByMobileNumber(request.getMob_num());
        } else if (request.getUser_id() != null) {
            UUID userID=UUID.fromString(request.getUser_id());
            return userRepository.findByUserId(userID);
        } else if (request.getManager_id() != null) {
            UUID managerID=UUID.fromString(request.getManager_id());
            return userRepository.findByManagerId(managerID);
        } else {
            return userRepository.findAll();
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean deleteUser(DeleteUserRequest request) {
        if (request.getUser_id() != null) {

            UUID user_id=UUID.fromString(request.getUser_id());
            return userRepository.deleteByUserId(user_id)>0;
        } else if (request.getMob_num() != null) {

            return userRepository.deleteByMobileNumber(request.getMob_num())>0;
        } else {

            return false;
        }
    }
}
