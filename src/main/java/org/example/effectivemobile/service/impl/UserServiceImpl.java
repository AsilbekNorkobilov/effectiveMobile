package org.example.effectivemobile.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.effectivemobile.entity.User;
import org.example.effectivemobile.entity.enums.RoleName;
import org.example.effectivemobile.repo.UserRepository;
import org.example.effectivemobile.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public HttpEntity<?> getAuthors(Pageable pageable) {
        List<User> authors = userRepository.findAllByRoleName(RoleName.ROLE_AUTHOR.name());
        Page<User> pagedAuthors=new PageImpl<>(authors,pageable,authors.size());
        return ResponseEntity.ok(pagedAuthors);
    }

    @Override
    public HttpEntity<?> getUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user);
    }

    @Override
    public HttpEntity<?> getExecutors(Pageable pageable) {
        List<User> executors = userRepository.findAllByRoleName(RoleName.ROLE_EXECUTOR.name());
        Page<User> pagedExecutors=new PageImpl<>(executors,pageable,executors.size());
        return ResponseEntity.ok(pagedExecutors);
    }
}
