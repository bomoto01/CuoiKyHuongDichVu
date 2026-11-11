package com.haui.library.config;

import com.haui.library.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/TaiKhoan/Login",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/h2-console/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        // Admin-specific operations
                        .requestMatchers("/api/TaiKhoan/AddTaiKhoan", "/api/TaiKhoan/UpdateTaiKhoan", "/api/TaiKhoan/DeleteTaiKhoan").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/Sach/AddSach").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/Sach/UpdateSach", "/api/Sach/AddMoreSach", "/api/Sach/RemoveSomeSach").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/Sach/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/TheLoai/AddTheLoai").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/TheLoai/UpdateTheLoai").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/TheLoai/**").hasRole("ADMIN")
                        .requestMatchers("/api/LiSuGiaoDich/**", "/api/LichSuGiaoDich/**").hasAnyRole("ADMIN", "MANAGER")
                        // Manager & Admin shared operations
                        .requestMatchers("/api/KhachHang/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/Muon/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/Sach/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/TheLoai/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/TaiKhoan/**").hasAnyRole("ADMIN", "MANAGER")
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Auth-Token", "Origin", "Accept"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

