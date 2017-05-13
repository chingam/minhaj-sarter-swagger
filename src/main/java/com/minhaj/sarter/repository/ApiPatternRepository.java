package com.minhaj.sarter.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.minhaj.sarter.entity.ApiPattern;

public interface ApiPatternRepository extends CrudRepository<ApiPattern, String>{
	List<ApiPattern> findAll();
}
