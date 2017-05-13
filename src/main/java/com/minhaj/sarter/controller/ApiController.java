package com.minhaj.sarter.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.minhaj.sarter.entity.ApiKey;
import com.minhaj.sarter.entity.ApiPattern;
import com.minhaj.sarter.repository.ApiKeyRepository;
import com.minhaj.sarter.repository.ApiPatternRepository;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class ApiController {

	@Autowired
	private ApiPatternRepository service;

	@Autowired
	private ApiKeyRepository apiservice;

	@ApiOperation(tags = "Api key", value = "api key", notes = "set value")
	@RequestMapping(value = "/api", method = RequestMethod.POST, produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public HashMap<String, Object> createApi(
			@ApiParam(value = "Api pattern", required = true) @RequestBody ApiPattern apiPattern) {
		service.save(apiPattern);
		HashMap<String, Object> res = new HashMap<String, Object>();
		res.put("message", "Success");
		return res;
	}

	@ApiOperation(tags = "Api key", value = "api key", notes = "set value")
	@RequestMapping(value = "/api/ses", method = RequestMethod.POST, produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public HashMap<String, Object> createEna(@ApiParam(value = "Api", required = true) @RequestBody ApiKey apikey) {
		apiservice.save(apikey);
		HashMap<String, Object> res = new HashMap<String, Object>();
		res.put("message", "Success");
		return res;
	}

	@ApiOperation(tags = "Api key", value = "All info", notes = "api")
	@RequestMapping(value = "/api/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ApiPattern> getAll() {
		return service.findAll();
	}

	@ApiOperation(tags = "Api key", value = "All info", notes = "api")
	@RequestMapping(value = "/api/ses", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Iterable<ApiKey> getAllSes() {
		return apiservice.findAll();
	}

}
