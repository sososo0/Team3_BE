package com.bungaebowling.server._core.security;

import com.bungaebowling.server._core.config.Configs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 1. CSRF 해제
        // SSR으로 스프링이 직접 페이지 만들어서 줄때는 csrf를 붙이는게 좋지만 프론트가 분리되어 있다면 그냥 해제하는게 낫다.
        // Postman 에서 테스트할때도 csrf.disable 을 하지 않으면 에러가 발생한다.
        http.csrf(AbstractHttpConfigurer::disable); // postman 접근해야 함!! - CSR 할때!!

        // 2. iframe 거부
        http.headers(headers ->
                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        // 3. cors 재설정
        http.cors(cors ->
                cors.configurationSource(configurationSource()));

        // 4. jSessionId 가 응답이 될 때 세션영역에서 사라진다(JWT 로 stateless 하게 할거임)
        http.sessionManagement(management ->
                management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 5. form 로그인 해제해서 UsernamePasswordAuthenticationFilter 비활성화 시키기 (Form Post x-www-form-urlencoded)
        http.formLogin(AbstractHttpConfigurer::disable);

        // 6. 로그인 인증창이 뜨지 않도록 HttpBasicAuthenticationFilter 비활성화 (헤더에 username, password)
        http.httpBasic(AbstractHttpConfigurer::disable);



        return http.build();
    }

    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE (Javascript 요청 허용)
        configuration.setAllowedOrigins(Configs.CORS);
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용
        configuration.addExposedHeader("Authorization"); // 헤더로 Authorization
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
