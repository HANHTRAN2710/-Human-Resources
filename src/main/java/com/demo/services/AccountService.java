package com.demo.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;


import com.demo.entities.Nhanvien;
import com.demo.entities.Yeucau;

public interface AccountService extends UserDetailsService {

	public Iterable<Nhanvien> findAll();
	
	public boolean save(Nhanvien nhanvien);

	public Nhanvien login(String username, String password);

	public List<Nhanvien> findAllNv();
	
	public List<Nhanvien> findAllNhanVienSupport();
	
	public Nhanvien find(String username);
	
	Nhanvien findNhanvienByYeucauId(int yeucauId);
	
	public String getpassword(String username);
}
