package com.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.entities.Douutien;
import com.demo.repositories.DouutienRepository;

@Service
public class DouutienServiceImpl implements DouutienService {

	@Autowired
	private DouutienRepository douutienRepository;
	
	@Override
	public Iterable<Douutien> findAll() {
		return douutienRepository.findAll();
	}

	@Override
	public boolean save(Douutien entity) {
		try {
			douutienRepository.save(entity);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
