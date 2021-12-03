package com.zhy.service;

import com.zhy.entity.Menu;
import com.zhy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuService  extends JpaRepository<User,String> {

    @Query(value = "select m,r from Menu m inner join m.roleList r where m.status=1")
    List<Menu> getAllMenu();
}
