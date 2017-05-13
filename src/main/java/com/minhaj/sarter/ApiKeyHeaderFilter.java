package com.minhaj.sarter;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

public class ApiKeyHeaderFilter extends OncePerRequestFilter {
	private String header;

	public ApiKeyHeaderFilter(String header) {
		this.header = header;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		if (request.getHeader(header) == null) { // API key is sent as basic auth username
			chain.doFilter(request, response);
		} else { // API key is sent as header
			final HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(httpRequest) {
				@Override
				public String getHeader(String name) {
					String auth = Base64.getEncoder().encodeToString((request.getHeader(header) + ":").getBytes());

					if ("Authorization".equalsIgnoreCase(name))
						return "Basic " + auth;
					else if (name.equalsIgnoreCase(header))
						return null;

					final String value = request.getParameter(name);
					if (value != null)
						return value;

					return super.getHeader(name);
				}
			};
			chain.doFilter(wrapper, response);
		}
	}

}
