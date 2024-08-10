package org.example.effectivemobile.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;

import java.util.UUID;

public interface UserService {
    HttpEntity<?> getAuthors(Pageable pageable);

    HttpEntity<?> getUser(UUID id);

    HttpEntity<?> getExecutors(Pageable pageable);
}
