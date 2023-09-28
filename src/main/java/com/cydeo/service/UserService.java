package com.cydeo.service;

import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;

import java.util.List;

public interface UserService {

    List<UserDTO> listAllUser();
   UserDTO findByUserName(String username);
   void save(UserDTO dto);
   UserDTO update(UserDTO dto);
   void deleteUserName(String name);

}
