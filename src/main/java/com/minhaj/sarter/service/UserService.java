package com.minhaj.sarter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.minhaj.sarter.entity.User;

@Component
public class UserService {

	List<User> users = new ArrayList<User>();
	
	public void addUser(User user){
		users.add(user);
	}
	
	public void updateUser(User user){
		List<User> userList = users.stream().filter(u -> u.getId().equalsIgnoreCase(user.getId())).collect(Collectors.toList());
		if (!userList.isEmpty()){
			users.remove(userList.get(0));
			addUser(user);
		}
	}
	
	public void deleteUser(User user){
		List<User> userList = users.stream().filter(u -> u.getId().equalsIgnoreCase(user.getId())).collect(Collectors.toList());
		if (!userList.isEmpty()){
			users.remove(userList.get(0));
		}
	}
	
	public List<User> fetchAll(){
		return users;
	}
}
