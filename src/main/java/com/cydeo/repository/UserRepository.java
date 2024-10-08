package com.cydeo.repository;

import com.cydeo.entity.Role;
import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameAndIsDeleted(String username, boolean isDeleted);


    List<User> findAllByIsDeleted( boolean isDeleted);

    Optional<User> findByUsername(String username);

    List<User> findAllByOrderByCompanyTitleAscRoleAsc();


    List<User> findAllByRole(String role);

    List<User> findAllByCompanyId(Long id);

}
