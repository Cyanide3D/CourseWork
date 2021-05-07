package com.example.CourseWork.config;

import com.example.CourseWork.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity // Включаем Spring Security
@Configuration // Указываем, что класс является конфигурационным (Будем создавать бины в этом классе)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception { // Основной метод для настройки Spring Security
        http
                .antMatcher("/**").authorizeRequests() // Spring security обрабатываем все эндпоинты
                .antMatchers("/", "/login", "/auth", "/css/**", "/img/**").permitAll() // Указываем эндпоинты, к которым будет доступ у всех...
                .anyRequest().authenticated() //...к остальным эндпоинтам доступ будет только у авторизированных пользователей
                    .and()
                .formLogin() // Включаем стандартную форму логина
                    .and()
                .logout().permitAll() // Логаут доступен всем
                    .and()
                .csrf().disable(); // Отключаем CSRF
    }

    @Bean
    public DaoAuthenticationProvider authProvider() { // Объясняем спрингу где и как искать пользователей для авторизации
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) { // Подключаем наш провайдер
        auth.authenticationProvider(authProvider());
    }

    @Bean
    public PasswordEncoder encoder() { // Создаём бин энкодера, для кодироваия паролей
        return new BCryptPasswordEncoder(10);
    }
}