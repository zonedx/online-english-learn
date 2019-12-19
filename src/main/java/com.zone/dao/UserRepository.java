package com.zone.dao;

import com.zone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,Integer> {
    @Query(value = "select count(*) from oel_user where oel_user.username=:username",nativeQuery = true)
    public int isExistUsername(@Param("username") String username);

    @Query(value = "select count(*) from oel_user where oel_user.email=:email",nativeQuery = true)
    public int isExistEmali(@Param("email") String email);

    @Query("select u from User u where u.activateCode = :activateCode")
    public User findDistinctByActivateCode(@Param("activateCode") String activateCode);

    public User findByUsernameAndPassword(String username, String password);

    public User findByEmailAndPassword(String email, String password);

    public User findByUsername(String username);
}
