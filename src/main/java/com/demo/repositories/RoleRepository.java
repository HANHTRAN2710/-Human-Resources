package com.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
 
import org.springframework.stereotype.Repository;

import com.demo.entities.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
	
	@Query(value = "select * from Role order by id desc limit 2", nativeQuery = true)
	public List<Role> findRole();
}
