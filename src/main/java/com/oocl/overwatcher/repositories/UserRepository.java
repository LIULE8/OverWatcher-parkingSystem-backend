package com.oocl.overwatcher.repositories;

import com.oocl.overwatcher.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author LIULE9
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> , JpaSpecificationExecutor<User> {
    Optional<User> findByUserName(String name);

    @Query(value = "select * from `user` where user_name=?1 and alive = ?2",nativeQuery = true)
    Optional<User> findByUserNameAndAlive(String username, boolean b);
}
