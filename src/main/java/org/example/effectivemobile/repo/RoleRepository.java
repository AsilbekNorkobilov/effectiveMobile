package org.example.effectivemobile.repo;

import org.example.effectivemobile.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query(value = "select * from roles where role_name=? limit 1",nativeQuery = true)
    Role findByRoleName(String roleName);
}