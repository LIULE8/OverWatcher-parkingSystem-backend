package com.oocl.overwatcher.repositories;

import com.oocl.overwatcher.entities.Role;
import com.oocl.overwatcher.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String name);

    @Query(value = "update User set `status` = ?2 where id = ?1", nativeQuery = true)
    @Modifying
    int updateStatusById(Long id, String status);
    @Query(value = "update User set `alive` = ?2 where id = ?1", nativeQuery = true)
    @Modifying
    int updateAliveById(Long id, Boolean alive);

    @Query(value = "update User set `name` = ?2,`username` = ?3,`email` = ?4,`phone` = ?5, where id = ?1", nativeQuery = true)
    @Modifying
    int updateBasicMessageOfEmployees(Long id, String name, String username, String email, String phone, List<Role> roles);

    @Query(value = "select * from user where name like %?1%", nativeQuery = true)
    List<User> findEmployeeByName(String name);

    @Query(value = "select * from user where email like %?1%", nativeQuery = true)
    List<User> findEmployeeByEmail(String email);

    @Query(value = "select * from user where phone like %?1%", nativeQuery = true)
    List<User> findEmployeeByPhone(String phone);

    @Query(value = "select * from user where `status` = '上班'", nativeQuery = true)
    List<User> findAllEmployeesOnWork();

    @Query(value = "select * from `user` where user_name=?1 and alive = ?2",nativeQuery = true)
    Optional<User> findByUserNameAndAlive(String username, boolean b);
}
