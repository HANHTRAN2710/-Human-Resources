package com.demo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.entities.Nhanvien;
import com.demo.entities.Yeucau;

@Repository
public interface YeucauRepository extends CrudRepository<Yeucau, Integer> {

	@Query("FROM Yeucau  WHERE tieude = :tieude")
	public Yeucau findNhanvienTenByYeucauId(@Param("tieude") String tieude);
	
	@Query("from Yeucau where ngaygui >= :form and ngaygui <= :to")
	public List<Yeucau> findByDate(@Param("form") Date form, @Param("to") Date to );
	 
	@Query("from Yeucau where nhanvienByManvGui = :nhanvienByManvGui")
	public List<Yeucau> findYeucau(@Param("nhanvienByManvGui") String nhanvienByManvGui);

}
