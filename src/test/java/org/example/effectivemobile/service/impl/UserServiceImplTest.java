package org.example.effectivemobile.service.impl;

import org.example.effectivemobile.entity.Role;
import org.example.effectivemobile.entity.User;
import org.example.effectivemobile.entity.enums.RoleName;
import org.example.effectivemobile.repo.RoleRepository;
import org.example.effectivemobile.repo.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


class UserServiceImplTest {
    private UserRepository userRepository;
    private UserServiceImpl userService;
    private RoleRepository roleRepository;

    @BeforeEach
    public void before(){
        roleRepository=Mockito.mock(RoleRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        userService=new UserServiceImpl(userRepository);
    }

    //@Test
    void getAuthors() {
        UUID id=UUID.randomUUID();
        Role roleAuthor=new Role(1,RoleName.ROLE_AUTHOR);
        User user=User.builder()
                .id(id)
                .roles(List.of(roleAuthor))
                .build();
        Pageable pageable= PageRequest.of(1,2);
        Page<User> userPage = new PageImpl<>(Collections.singletonList(user), pageable, 1);
        //Mockito.when(userRepository.findAllByRole("ROLE_AUTHOR",pageable)).thenReturn(userPage);
        HttpEntity<?> entity = userService.getAuthors(pageable);
        Page<User> paged = (Page<User>)entity.getBody();
        List<User> content = paged.getContent();
        User found = content.get(0);
        Assertions.assertEquals(id,found.getId());
    }

    @Test
    void getUser() {
        UUID id=UUID.randomUUID();
        User user=User.builder()
                .id(id)
                .email("a.gmail.com")
                .firstName("a")
                .lastName("aa")
                .build();
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
        HttpEntity<?> httpEntity = userService.getUser(id);
        User foundedUser = (User)httpEntity.getBody();
        Assertions.assertEquals(id,foundedUser.getId());
    }

    @Test
    void userNotFoundWhenGet(){
        UUID id=UUID.randomUUID();
        Assertions.assertThrows(RuntimeException.class,()->{
            userService.getUser(id);
        });
    }

   // @Test
    void getExecutors() {
        UUID id=UUID.randomUUID();
        Role roleExecutor=new Role(1,RoleName.ROLE_EXECUTOR);
        User user=User.builder()
                .id(id)
                .roles(List.of(roleExecutor))
                .build();
        Pageable pageable= PageRequest.of(1,2);
        Page<User> userPage = new PageImpl<>(Collections.singletonList(user), pageable,1);
        //Mockito.when(userRepository.findAllByRole("ROLE_EXECUTOR",pageable)).thenReturn(userPage);
        HttpEntity<?> entity = userService.getExecutors(pageable);
        Page<User> paged = (Page<User>)entity.getBody();
        List<User> content = paged.getContent();
        User found = content.get(0);
        Assertions.assertEquals(id,found.getId());
    }
}