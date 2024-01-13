package com.cydeo.service.impl;

import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;

    public UserServiceImpl(UserRepository userRepository, MapperUtil mapperUtil) {
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
    }

    public UserDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return mapperUtil.convert(user, new UserDTO());
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> userList = userRepository.findAllByIsDeleted(false);
        return userList.stream()
                .map(user -> mapperUtil.convert(user, new UserDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public void save(UserDTO userDTO) {
        userDTO.setEnabled(true);
        User user = mapperUtil.convert(userDTO, new User());
        userRepository.save(mapperUtil.convert(userDTO, new User()));
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findByIdAndIsDeleted(id, false);
        user.setIsDeleted(true);
        userRepository.save(user);
    }

    @Override
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return mapperUtil.convert(user, new UserDTO());
    }

    @Override
    public UserDTO updateUser(UserDTO userDtoToBeUpdate) {
        User user1 = userRepository.findById(userDtoToBeUpdate.getId()).orElseThrow();
        user1.setUsername(user1.getUsername());
        userRepository.save(user1);
        User convertedUser = mapperUtil.convert(userDtoToBeUpdate, new User());
        convertedUser.setId(user1.getId());
        userRepository.save(convertedUser);
        return mapperUtil.convert(convertedUser, new UserDTO());
    }



}
