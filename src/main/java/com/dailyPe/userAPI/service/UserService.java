package com.dailyPe.userAPI.service;

import com.dailyPe.userAPI.dto.DeleteUserRequest;
import com.dailyPe.userAPI.dto.GetUserRequest;
import com.dailyPe.userAPI.dto.UpdateUserRequest;
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
import java.util.Map;
import java.util.Optional;
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
        }
//        } else if (request.getUser_id() != null) {
//            UUID userID=UUID.fromString(request.getUser_id());
//            return userRepository.findByUserId(userID);
//        }
        else if (request.getManager_id() != null) {
            UUID managerID=UUID.fromString(request.getManager_id());
            return userRepository.findByManagerId(managerID);
        } else {
            return userRepository.findAll();
        }
    }

    public User getUser(GetUserRequest request){
        UUID userID=UUID.fromString(request.getUser_id());
        return userRepository.findByUserId(userID);
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

    public StringBuilder updateUser(UpdateUserRequest requestDTO) {

        StringBuilder Messages = new StringBuilder();

        // Check if bulk update is allowed
        boolean isValidBulkUpdate = requestDTO.getUser_ids().size() > 1
                && requestDTO.getUpdate_data().size() > 1
                && !requestDTO.getUpdate_data().stream().anyMatch(data -> data.size() > 1)
                && !requestDTO.getUpdate_data().stream().allMatch(data -> data.containsKey("manager_id"));

        if (isValidBulkUpdate) {
            return Messages.append("Bulk update is only allowed for manager_id.");
        }

        boolean isBulkUpdate = requestDTO.getUser_ids().size() > 1
                && requestDTO.getUpdate_data().size() > 1
                && requestDTO.getUpdate_data().stream().allMatch(data -> data.containsKey("manager_id"));

        if (isBulkUpdate) {
            // Handle bulk update
           for (String userId : requestDTO.getUser_ids()) {
               for (Map<String, String> updateData : requestDTO.getUpdate_data()) {
                   // Process each update data set
                    Messages.append(processUpdate(userId, updateData)+" for "+userId+" ");
               }
           }


                // for (Map<String, String> updateData : requestDTO.getUpdate_data()) {
                //     for (String userId : requestDTO.getUser_ids()) {
                //     // Process each update data set

                //      Messages.append(processUpdate(userId, updateData)+" for "+userId+" ");
                // }
           // }

        }
        else {
            // Handle individual update
            for (String userId : requestDTO.getUser_ids()) {
                Map<String, String> updateData = requestDTO.getUpdate_data().get(0); // Assuming individual update only contains one set of update data
                return Messages.append(processUpdate(userId, updateData)+" for "+userId+" ");
            }
        }
        return Messages;
    }

    private String processUpdate(String userId, Map<String, String> updateData) {

        UUID userID = UUID.fromString(userId);

        User user = userRepository.findByUserId(userID);

        if (user == null) {
            // User not found, handle error
            return "Invalid ! user not found";
        }

        for (Map.Entry<String, String> entry : updateData.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();
            switch (field) {
                case "full_name":

                    if (value == null || value.isEmpty()) {

                        return "Invalid! name cannot be null";
                    } else {
                        user.setFullName(value);
                    }
                    break;
                case "mob_num":
                    // Validate and update mob_num
                    if (isValidMobileNumber(value)) {
                        user.setMobileNumber(value);
                    } else {

                        return "Invalid mobile number";
                    }
                    break;
                case "pan_num":
                    // Validate and update pan_num
                    if (isValidPANNumber(value)) {
                        user.setPanNumber(value.toUpperCase());
                    } else {

                        return "Invalid PAN number";
                    }
                    break;
                case "manager_id":
                    UUID id = UUID.fromString(value);
                    // Validate and update manager_id
                    if (isValidManagerId(id)) {
                        user.setManagerId(id);
                    } else {
                        return "Invalid Manager id";
                    }
                    break;
                default:
                    // Unsupported field

                    return "Invalid field";

            }
        }
        // Update updated_at timestamp
        user.setUpdatedAt(LocalDateTime.now());
        try{
            userRepository.save(user);
            return "success";
        }
        catch (Exception e){
            return "failure";
        }
    }

    private boolean isValidManagerId(UUID id) {

        Optional<Manager> man = managerRepository.findById(id);
        if(man.isEmpty()){
            return false;
        }
        return true;
    }
}
