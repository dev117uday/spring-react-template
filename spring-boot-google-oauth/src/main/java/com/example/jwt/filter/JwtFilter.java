package com.example.jwt.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.jwt.exception.ExceptionBroker;
import com.example.jwt.service.UserService;
import com.example.jwt.utility.JWTUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JWTUtility jwtUtility;

	@Autowired
	private UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authorization = request.getHeader("Authorization");
		String token = null;
		String username = null;

		if (null != authorization && authorization.startsWith("Bearer ")) {
			token = authorization.substring(7);
			try {
				username = jwtUtility.getUserIdFromToken(token);
			} catch (ExceptionBroker e) {
				throw new ServletException("invalid jwt");
			}
		}

		try {
			if (null != username && SecurityContextHolder.getContext().getAuthentication() == null
					&& jwtUtility.validateToken(token)) {
				UserDetails userDetails = userService.loadUserByUsername(username);

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication((usernamePasswordAuthenticationToken));
			}
		} catch (UsernameNotFoundException | ExceptionBroker e) {
			throw new ServletException("invalid jwt");
		}

		filterChain.doFilter(request, response);
	}

}
