package com.minhaj.sarter;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;
import java.util.Base64;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.minhaj.sarter.entity.ApiKey;
import com.minhaj.sarter.entity.ApiPattern;
import com.minhaj.sarter.repository.ApiKeyRepository;
import com.minhaj.sarter.repository.ApiPatternRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MinhajSarterSwaggerApplication.class)
@WebAppConfiguration
public class ApiKeyAuthTests {
	@Value("${server.port}")
	private int port;
	@Value("${apikeyauth.header}")
	private String header;
	
	private String baseUrl;
	
	private TestRestTemplate restTemplate;
	
	@Autowired
	private ApiKeyRepository apiKeyRepository;
	@Autowired
	private ApiPatternRepository apiPatternRepository;
	
	private static final String VALID_API_KEY = "API_KEY_AUTH_TEST_KEY";
	private static final String INVALID_API_KEY = "API_KEY_AUTH_TEST_KEY_INVALID";
	private static final String VALID_AUTHORITY = "API_KEY_AUTH_TEST_AUTHORITY";
	private static final String INVALID_AUTHORITY = "API_KEY_AUTH_TEST_AUTHORITY_INVALID";
	private static final String ALLOWED_PATH = "/api/key/auth/test/path/1";
	private static final String DISALLOWED_PATH = "/api/key/auth/test/path/2";
	
	@Before
	public void setUp() {
		ApiKey apiKey = new ApiKey();
		apiKey.setKey(VALID_API_KEY);
		apiKey.setEnabled(true);
		apiKey.setExpired(false);
		apiKey.setLocked(false);
		apiKey.setAuthorities(new TreeSet<>(Arrays.asList(VALID_AUTHORITY)));
		apiKeyRepository.save(apiKey);
		
		ApiPattern apiPattern1 = new ApiPattern();
		apiPattern1.setPattern(ALLOWED_PATH);
		apiPattern1.setAuthorities(new TreeSet<>(Arrays.asList(VALID_AUTHORITY)));
		apiPatternRepository.save(apiPattern1);
		
		ApiPattern apiPattern2 = new ApiPattern();
		apiPattern2.setPattern(DISALLOWED_PATH);
		apiPattern2.setAuthorities(new TreeSet<>(Arrays.asList(INVALID_AUTHORITY)));
		apiPatternRepository.save(apiPattern2);
		
		baseUrl = "http://localhost:" + port;
		restTemplate = new TestRestTemplate();
	}
	
	@Test
	public void testNoCredential() {
		HttpEntity<Object> entity = new HttpEntity<>(null);
		ResponseEntity<Object> response = restTemplate.exchange(baseUrl, HttpMethod.GET, entity, Object.class);
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	}
	
	@Test
	public void testValidApiKey() {
		HttpHeaders headers = new HttpHeaders();
		headers.set(header, VALID_API_KEY);
		HttpEntity<Object> entity = new HttpEntity<>(headers);
		ResponseEntity<Object> response = restTemplate.exchange(baseUrl, HttpMethod.GET, entity, Object.class);
		assertNotEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	}
	
	@Test
	public void testInvalidApiKey() {
		HttpHeaders headers = new HttpHeaders();
		headers.set(header, INVALID_API_KEY);
		HttpEntity<Object> entity = new HttpEntity<>(headers);
		ResponseEntity<Object> response = restTemplate.exchange(baseUrl, HttpMethod.GET, entity, Object.class);
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	}
	
	@Test
	public void testValidBasicAuth() {
		String auth = Base64.getEncoder().encodeToString((VALID_API_KEY + ":").getBytes());
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Basic " + auth);
		HttpEntity<Object> entity = new HttpEntity<>(headers);
		ResponseEntity<Object> response = restTemplate.exchange(baseUrl, HttpMethod.GET, entity, Object.class);
		assertNotEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	}
	
	@Test
	public void testInvalidBasicAuth() {
		String auth = Base64.getEncoder().encodeToString((INVALID_API_KEY + ":").getBytes());
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Basic " + auth);
		HttpEntity<Object> entity = new HttpEntity<>(headers);
		ResponseEntity<Object> response = restTemplate.exchange(baseUrl, HttpMethod.GET, entity, Object.class);
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	}
	
	@Test
	public void testAllowedPath() {
		HttpHeaders headers = new HttpHeaders();
		headers.set(header, VALID_API_KEY);
		HttpEntity<Object> entity = new HttpEntity<>(headers);
		ResponseEntity<Object> response = restTemplate.exchange(baseUrl + ALLOWED_PATH, HttpMethod.GET, entity, Object.class);
		assertNotEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
	}
	
	@Test
	public void testDisallowedPath() {
		HttpHeaders headers = new HttpHeaders();
		headers.set(header, VALID_API_KEY);
		HttpEntity<Object> entity = new HttpEntity<>(headers);
		ResponseEntity<Object> response = restTemplate.exchange(baseUrl + DISALLOWED_PATH, HttpMethod.GET, entity, Object.class);
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
	}
}