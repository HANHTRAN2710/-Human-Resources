package com.demo.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.demo.entities.Nhanvien;
import com.demo.repositories.AccountRepository;

public class DanhsachNVServiceImpl implements DanhsachNVService{
	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Iterable<Nhanvien> findAll() {
		return accountRepository.findAll();
	}

	@Override
	public Nhanvien find(String username) {
		 return accountRepository.findByUsername(username);
	}

	

}
