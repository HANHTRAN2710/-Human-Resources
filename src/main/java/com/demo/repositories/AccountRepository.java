package com.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.entities.Nhanvien;
import com.demo.entities.Yeucau;

@Repository
public interface AccountRepository extends CrudRepository<Nhanvien, Integer> {

	@Query("from Nhanvien where username = :username")
	public Nhanvien findByUsername(@Param("username") String username);
	

	
	@Query("from Nhanvien where username = :username and password = :password")
	public Nhanvien login(@Param("username") String username, @Param("password") String password);
	
	@Query("from Nhanvien where role.id = 3")
	public List<Nhanvien> findAllNv();
	
	@Query("from Nhanvien where role.id = 2")
	public List<Nhanvien> findAllNhanvienSupport();
	
	
}
