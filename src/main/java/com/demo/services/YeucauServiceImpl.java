package com.demo.services;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.controller.admin.NhanvienSupportController;
import com.demo.entities.Nhanvien;
import com.demo.entities.Yeucau;

import com.demo.repositories.YeucauRepository;
@Service
public class YeucauServiceImpl implements YeucauService{
	@Autowired
	private YeucauRepository yeucauRepository;

	@Override
	public Iterable<Yeucau> findAll() {
		 
		return  yeucauRepository.findAll();
	}

	@Override
	public Yeucau findNhanvienTenByYeucauId(String username) {
		return yeucauRepository.findNhanvienTenByYeucauId(username);
	}
	
//	cau b
	@Override
	public List<Yeucau> findByMaNhanVien(String maNhanVien) {
		return StreamSupport
	    		.stream(yeucauRepository.findAll().spliterator(), false)
    			.filter((yc) -> yc.getNhanvienByManvGui().getUsername().equals(maNhanVien))
    			.collect(Collectors.toList());
	}
	@Override
	public List<Yeucau> findByMaNhanVienXuLy(String maNhanVien) {
		return StreamSupport
	    		.stream(yeucauRepository.findAll().spliterator(), false)
    			.filter((yc) -> {
    				if(yc.getNhanvienByManvXuly() != null ) {
    					return yc.getNhanvienByManvXuly().getUsername().equals(maNhanVien);
    				}
    				return false;
    			})
    			.collect(Collectors.toList());
	}	

	
//	cau e
	@Override
	public Iterable<Yeucau> findAll(String username) {
		return yeucauRepository.findAll();
	}
	
	@Override
	public List<Yeucau> findByPriority(Integer priorityId) {
	    return StreamSupport
	    		.stream(yeucauRepository.findAll().spliterator(), false)
    			.filter((yc) -> yc.getDouutien().getMadouutien().compareTo(priorityId) == 0)
    			.collect(Collectors.toList());
	}
	
	@Override
	public List<Yeucau> findByDate(Date form, Date to) {
		return yeucauRepository.findByDate(form, to);
	}

	@Override
	public Boolean assignToNhanVienSupport(Yeucau yeucau,String maNhanVienSupport) {
		Nhanvien nhanvien = new Nhanvien();
		nhanvien.setUsername(maNhanVienSupport);
		yeucau.setNhanvienByManvXuly(nhanvien);
		yeucauRepository.save(yeucau);
		return true;
	}

}
