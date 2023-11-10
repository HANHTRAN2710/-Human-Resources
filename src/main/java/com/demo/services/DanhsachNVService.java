package com.demo.services;

import com.demo.entities.Nhanvien;
 
 

public interface DanhsachNVService {
	public Iterable<Nhanvien> findAll();
	
	public Nhanvien find(String username);

}
