package com.demo.services;

import java.util.List;

import com.demo.entities.Role;

public interface RoleService {

	public Iterable<Role> findAll();
	public List<Role> findRole();
}
