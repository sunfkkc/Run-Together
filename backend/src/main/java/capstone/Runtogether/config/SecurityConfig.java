package capstone.Runtogether.config;

import capstone.Runtogether.config.filter.JwtAuthenticationFilter;
import capstone.Runtogether.config.handler.CustomAccessDeniedHandler;
import capstone.Runtogether.config.handler.CustomEntryPoint;
import capstone.Runtogether.util.CookieUtil;
import capstone.Runtogether.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtil cookieUtil;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomEntryPoint customEntryPoint;

    // 암호화를 위해
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) {
        // resources 모든 접근을 허용하는 설정을 해버리면
        // HttpSecurity 설정한 ADIM권한을 가진 사용자만 resources 접근가능한 설정을 무시해버린다.
        web.ignoring()
                .antMatchers("/resources/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/*/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()
                .exceptionHandling().authenticationEntryPoint(customEntryPoint).accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, cookieUtil), UsernamePasswordAuthenticationFilter.class);
        //.addFilterBefore(new JwtExceptionFilter(objectMapper), JwtAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();

        cors.setAllowedOrigins(Arrays.asList("http://localhost:3001", "http://localhost:3000","http://192.168.0.170:3000","https://192.168.0.170:3000"));
        cors.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        cors.setAllowedHeaders(List.of("*"));
        cors.setAllowCredentials(true); // 자격증명과 함께 요청 여부 (Authorization로 사용자 인증 사용 시 true)
        //configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }


}
