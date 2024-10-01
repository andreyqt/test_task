package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.UserDto;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user = userRepository.save(user);
        log.info("User with id {} has been created", user.getId());
        return userMapper.toDto(user);
    }

    @Transactional
    public UserDto updateUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("no user with such id"));
        userMapper.updateEntity(user, userDto);
        userRepository.save(user);
        log.info("User with id {} has been updated", user.getId());
        return userMapper.toDto(user);
    }

    @Transactional
    public void deleteUser(long id) {
        userRepository.deleteById(id);
        log.info("User with id {} has been deleted", id);
    }

    @Transactional(readOnly = true)
    public UserDto getUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("no user with such id"));
        return userMapper.toDto(user);
    }

}
