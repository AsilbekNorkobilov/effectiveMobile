package org.example.effectivemobile.repo;

import org.example.effectivemobile.entity.Role;
import org.example.effectivemobile.entity.User;
import org.example.effectivemobile.entity.enums.RoleName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);

    @Query(value = """
        select u.* from users u
        join users_roles ur on ur.user_id = u.id
        join roles r on ur.roles_id = r.id
        where r.role_name=:roleName
        """, nativeQuery = true)
    List<User> findAllByRoleName(String roleName);
}