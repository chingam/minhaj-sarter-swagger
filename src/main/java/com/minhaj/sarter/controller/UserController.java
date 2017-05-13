package com.minhaj.sarter.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.minhaj.sarter.entity.User;
import com.minhaj.sarter.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@RestController
public class UserController {

	@Autowired 
	private UserService service;
	
	
	@ApiOperation(tags="User registration", value="Get user registration", notes="Get user registration for api")
	@RequestMapping(value="/user", method = RequestMethod.POST, produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public HashMap<String, Object> createUser(@ApiParam(value="User Model", required=true) @RequestBody User user){
		service.addUser(user);
		HashMap<String, Object> res = new HashMap<String, Object>();
		res.put("message", "Success");
		return res;
	}
	
	@ApiOperation(tags="Update user", value="User info", notes="Put user info for api")
	@RequestMapping(value="/user/{id}/{name}/{email}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public HashMap<String, String> UpdateUser(@ApiParam(value="User id", required=true) @PathVariable String id, 
			@ApiParam(value="User name", required=true) @PathVariable String name, @ApiParam(value="User email", required=true) @PathVariable String email){
		HashMap<String, String> res = new HashMap<String, String>();
		if(id != null){
			User user = new User();
			user.setId(id);
			user.setName(name);
			user.setEmail(email);
			service.updateUser(user);
			res.put("message", "Update Success");
		}else{
			res.put("message", "User id is emty");
		}
		
		return res;
	}
	
	
	@ApiOperation(tags="Users All", value="Get user info", notes="Get user info for api")
	@RequestMapping(value="/user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> getUserAll(){
		return service.fetchAll();
	}
	
	@ApiOperation(tags="Delete user", value="User info", notes="Put user info for api")
	@RequestMapping(value="/user/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HashMap<String, String> deleteUser(@ApiParam(value="User id", required=true) @PathVariable String id){
		User user = new User();
		user.setId(id);
		service.deleteUser(user);
		HashMap<String, String> res = new HashMap<String, String>();
		res.put("message", "Delete Success");
		return res;
	}
	
}
