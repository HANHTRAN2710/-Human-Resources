package com.demo.services;

import com.demo.entities.Douutien;
 

public interface DouutienService {
	public Iterable<Douutien> findAll();
	public boolean save(Douutien entity);
}
