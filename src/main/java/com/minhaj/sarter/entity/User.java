package com.minhaj.sarter.entity;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="User model")
public class User implements Serializable{

	private static final long serialVersionUID = 3405534655211412084L;
	
	@ApiModelProperty(value="User id", dataType="string", required=true)
	private String id;
	
	@ApiModelProperty(value="User name", dataType="string", required=true)
	private String name;
	
	@ApiModelProperty(value="User email", dataType="string", required=true)
	private String email;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + "]";
	}

}
