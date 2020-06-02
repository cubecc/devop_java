package com.cube.webDemo.dao;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cube.webDemo.entity.WebUser;


@Repository
public interface WebUserDao extends  CrudRepository<WebUser, Integer>{
	  List<WebUser> findByLoginAndPassword(String login, String password);
}
