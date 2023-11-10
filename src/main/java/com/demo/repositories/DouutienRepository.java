package com.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.entities.Douutien;
import com.demo.entities.Nhanvien;
import com.demo.entities.Yeucau;

@Repository
public interface DouutienRepository extends CrudRepository<Douutien, Integer> {

}
