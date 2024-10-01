package service;

import jakarta.persistence.EntityNotFoundException;
import org.example.dto.UserDto;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    @InjectMocks
    private UserService userService;

    private UserDto userDto;
    private User user;

    @BeforeEach
    public void init() {
        userDto = UserDto.builder()
                .id(1L)
                .email("example@test.com")
                .username("testUser")
                .build();
        user = User.builder()
                .id(1L)
                .email("example@test.com")
                .username("testUser")
                .build();
    }

    @Test
    public void testCreateUser() {
        Mockito.when(userRepository.save(user)).thenAnswer(i -> i.getArgument(0));
        UserDto actualUserDto = userService.createUser(userDto);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Assertions.assertEquals(userDto, actualUserDto);
    }

    @Test
    public void testUpdateUser() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenAnswer(i -> i.getArgument(0));
        UserDto actualUserDto = userService.updateUser(userDto);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Assertions.assertEquals(userDto, actualUserDto);
    }

    @Test
    public void testUpdateUserWithInvalidId() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Exception e = Assertions.assertThrows(EntityNotFoundException.class, () -> userService.updateUser(userDto));
        Assertions.assertEquals("no user with such id", e.getMessage());
    }

    @Test
    public void testDeleteUser() {
        Mockito.doNothing().when(userRepository).deleteById(1L);
        userService.deleteUser(1L);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteUserWithInvalidId() {
        Mockito.doThrow(EntityNotFoundException.class).when(userRepository).deleteById(1L);
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(1L));
    }

    @Test
    public void testGetUserById() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserDto actualUserDto = userService.getUser(1L);
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Assertions.assertEquals(userDto, actualUserDto);
    }

    @Test
    public void testGetUserWithInvalidId() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.getUser(1L));
    }
}
