package DA.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig  {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Tắt CSRF theo cách mới
                .authorizeHttpRequests(auth -> auth  // Sử dụng cấu hình mới cho quyền truy cập
                        .requestMatchers("/api/user/login",
                                "/api/user/add",
                                "/api/user/add/images",
                                "/api/user/**",
                                "/api/question/**",
                                "/api/user/update",
                                "/api/user/profile",
                                "/api/user/{id}/image",
                                "/api/department/add"
                                ,"/api/department/add"
                                ,"/api/department/update"
                                ,"/api/department/listUser"
                                ,"/api/department/listDepartment"
                                , "/api/department/addUser"
                                ,"/api/department"
                                ,"/api/department/listDepartmentUser"
                                , "/api/user/export/excel"
                                ,"/api/position/**",
                                "/api/file/**"
                        ,"/api/evaluate/**"
                        ,"/api/questions/**"
                        ,"/api/questionSet/**"
                        ,"/api/timeEvaluateRole/**"
                        ,"/api/userEvaluate/**")
                        .permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }

//    @Override
//    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
//        configurer.defaultContentType(MediaType.APPLICATION_JSON);
//    }
}


