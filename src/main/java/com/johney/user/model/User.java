package com.johney.user.model;

import java.io.Serializable;


/**
 * Created by zl on 2015/8/27.
 */
public class User  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
//	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private String name;
	
	private int age;
	
	private String phone;
	
	private String address;

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

//	public  Integer getSerialVersionUID() {
//		return id;
//	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", age=" + age + ", phone=" + phone + ", address=" + address + "]";
	}

}
