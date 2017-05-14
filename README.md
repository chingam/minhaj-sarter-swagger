# Rest Api with security also integrated swagger

## Running minhaj-sarter-swagger locally
```
	git clone https://github.com/chingam/minhaj-sarter-swagger.git
	cd minhaj-sarter-swagger
	mvn -DskipTests spring-boot:run
```

You can then access here: http://localhost:8080/swagger-ui.html

## How to use API key
1. Add API key to <b>api_key</b> database table or using <b>ApiKey</b> JPA entity.
2. Add header <b>ApiKey</b> or use HTTP basic authentication with <b>username = {API key}</b> and <b>password = EMPTY</b> to access REST resource.

	```ApiKey : ff808181569d2a7401569d2f12930001```</br>

	```curl -H "ApiKey: YOUR_API_KEY" https://yourhost/yourpath```</br>or</br>

	```curl -u YOUR_API_KEY: https://yourhost/yourpath```

## How to control access to URL patterns
1. Add pattern and authorities to <b>api_pattern</b> database table or using <b>ApiPattern</b> JPA entity.



## Database configuration
In its default configuration, i am uses an in-memory database (H2) DB.

### Steps:

1) In the command line
```
git clone https://github.com/chingam/minhaj-sarter-swagger.git
```
2) Inside Eclipse
```
File -> Import -> Maven -> Existing Maven project
```