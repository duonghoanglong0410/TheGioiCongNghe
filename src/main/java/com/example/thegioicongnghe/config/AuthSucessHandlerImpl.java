package com.example.thegioicongnghe.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

@Service
public class AuthSucessHandlerImpl implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication) throws IOException, ServletException {
		// Lấy thông tin người dùng từ Authentication
		String username = authentication.getName();

		// Lưu thông điệp chào mừng vào session
		HttpSession session = request.getSession();
		session.setAttribute("welcomeMessage", "Welcome, " + username + "!");

		// Lấy vai trò của người dùng
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Set<String> roles = AuthorityUtils.authorityListToSet(authorities);

		// Điều hướng dựa trên vai trò
		if (roles.contains("ROLE_ADMIN")) {
			response.sendRedirect("/admin/products/list");
		} else {
			response.sendRedirect("/");
		}
	}
}
