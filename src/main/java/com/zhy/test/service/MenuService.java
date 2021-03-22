package com.zhy.test.service;

import com.zhy.test.entity.Menu;
import com.zhy.test.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuService  extends JpaRepository<User,String> {

    @Query(value = "select m,r from Menu m inner join m.roleList r")
    List<Menu> getAllMenu();
}
