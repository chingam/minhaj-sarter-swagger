package com.minhaj.sarter.repository;

import org.springframework.data.repository.CrudRepository;

import com.minhaj.sarter.entity.ApiKey;

public interface ApiKeyRepository extends CrudRepository<ApiKey, String>{
	ApiKey findByKey(String key);
}
