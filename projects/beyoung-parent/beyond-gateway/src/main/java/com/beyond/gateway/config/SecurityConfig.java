package com.beyond.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    	
    	/*return http
    	        .csrf(csrf -> csrf.disable())
    	        .authorizeExchange(exchanges -> exchanges.anyExchange().permitAll()) // 全開放測試
    	        .build();
    	*/
    	
        return http
            .csrf(csrf -> csrf.disable()) // 關閉 CSRF
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/admin/gateway/**").hasRole("ADMIN") // 只有 ADMIN 能進
                .anyExchange().permitAll() // 其他 API 路由全放行
            )
            .httpBasic(Customizer.withDefaults()) // 使用 Basic Auth
            .build();
        
    }

    // 1. 定義加密器 (Bean)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. 使用加密器處理密碼
    @Bean
    public MapReactiveUserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails adminUser = User.builder()
            .username("gateway-admin")
            // "secret123" 經過 BCrypt 加密後的字串
            .password(encoder.encode("secret123")) 
            .roles("ADMIN")
            .build();
        return new MapReactiveUserDetailsService(adminUser);
    }
}