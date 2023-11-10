package com.demo.services;

import java.util.Date;
import java.util.List;

 
import com.demo.entities.Yeucau;

public interface YeucauService {

	public Iterable<Yeucau> findAll();

	public Iterable<Yeucau> findAll(String username);
	
	public Yeucau findNhanvienTenByYeucauId(String username);
	
	public List<Yeucau> findByMaNhanVien(String maNhanVien);

	public List<Yeucau> findByMaNhanVienXuLy(String maNhanVien);

	public List<Yeucau> findByPriority(Integer priorityId);

	public List<Yeucau> findByDate(Date startDate, Date endDate);
	
	public Boolean assignToNhanVienSupport(Yeucau yeucau, String maNhanVienSupport);
}
