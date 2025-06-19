package com.example.thegioicongnghe.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) // Kích hoạt @PreAuthorize và @PostAuthorize
public class SecurityConfig {

	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	
	@Autowired
	@Lazy
	private AuthFailureHandlerImpl authenticationFailureHandler;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()) // Tắt CSRF nếu không cần bảo vệ CSRF
				.cors(cors -> cors.disable()) // Nếu có yêu cầu CORS, tắt nó đi
				.authorizeHttpRequests(req -> req
						.requestMatchers("/**", "/shop", "/signin", "/login", "/posts/**", "/post/**", "/shop/**", "/search/**", "/search?/**").permitAll() // Cho phép tất cả request như trang chủ, sản phẩm, bài viết mà không cần đăng nhập
						.requestMatchers("/admin/**").hasRole("ADMIN")
						// Cho phép tài nguyên tĩnh (CSS, JS, images) mà không cần đăng nhập
						.requestMatchers("/user/css/**", "/user/js/**", "/user/images/**", "/user/fonts/**").permitAll()
						.requestMatchers("/cart/**", "/checkout/**", "/payment/**").authenticated() // Chỉ cho phép truy cập các trang giỏ hàng, thanh toán khi đăng nhập
						.anyRequest().authenticated() // Các request khác đều phải đăng nhập
				)
				.formLogin(form -> form
						.loginPage("/signin") // Trang đăng nhập của bạn
						.loginProcessingUrl("/login") // Xử lý đăng nhập
						.defaultSuccessUrl("/", true) // Sau khi đăng nhập, chuyển hướng về trang chủ
						.failureHandler(authenticationFailureHandler)
						.successHandler(authenticationSuccessHandler)
				)
				.logout(logout -> logout.permitAll()); // Cho phép logout không cần xác thực

		return http.build();
	}



	/*
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http.csrf(csrf->csrf.disable()).cors(cors->cors.disable())
				.authorizeHttpRequests(req->req
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.requestMatchers("/**").permitAll())
				.formLogin(form->form.loginPage("/signin")
						.loginProcessingUrl("/login")
//						.defaultSuccessUrl("/")
						.failureHandler(authenticationFailureHandler)
						.successHandler(authenticationSuccessHandler))
				.logout(logout->logout.permitAll());
		
		return http.build();
	}
	 */

}
