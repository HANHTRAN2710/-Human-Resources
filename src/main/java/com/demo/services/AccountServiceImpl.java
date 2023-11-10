package com.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.demo.entities.Nhanvien;
import com.demo.entities.Role;
import com.demo.entities.Yeucau;
import com.demo.repositories.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public boolean save(Nhanvien nhanvien) {
		try {
			accountRepository.save(nhanvien);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Nhanvien account = accountRepository.findByUsername(username);
		if (account == null) {
			throw new UsernameNotFoundException("Username Not Found");
		} else {
			Role role = account.getRole();
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

			authorities.add(new SimpleGrantedAuthority(role.getName()));

			return new User(username, account.getPassword(), authorities);
		}
	}

	@Override
	public Iterable<Nhanvien> findAll() {
		return accountRepository.findAll();
	}

	@Override
	public Nhanvien login(String username, String password) {
		try {
			return accountRepository.login(username, password);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Nhanvien> findAllNv() {
		return accountRepository.findAllNv();
	}
	
	@Override
	public List<Nhanvien> findAllNhanVienSupport() {
		return accountRepository.findAllNhanvienSupport();
	}

	@Override
	public Nhanvien find(String username) {
		return accountRepository.findByUsername(username);
	}

	@Override
	public Nhanvien findNhanvienByYeucauId(int yeucauId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getpassword(String username) {
		Nhanvien nv = accountRepository.findByUsername(username);
		return nv.getPassword();
	}

}
