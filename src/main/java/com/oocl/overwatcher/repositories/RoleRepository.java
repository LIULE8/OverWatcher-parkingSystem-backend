package com.oocl.overwatcher.repositories;

import com.oocl.overwatcher.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author LIULE9
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String roleName);
}
