package com.zhy.test.service;

import com.zhy.test.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<User,String> {

    User findByUsername(String username);

    @Query(value="select r.roleCode from User u inner join u.roles as r where u.username= :userName")
    List<String> queryUserOwnedRoleCodes(String userName);
}
