package com.fox.expenseincomemanage.config;

import com.fox.expenseincomemanage.handler.AuthenticationEntryPointImpl;
import com.fox.expenseincomemanage.util.RedisCacheUtils;
import com.fox.expenseincomemanage.filter.AuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * 与安全框架spring-security相关的
 *
 * @author 狐狸半面添
 * @create 2023-01-15 22:56
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private RedisCacheUtils redisCacheUtils;

    @Autowired
    public SecurityConfig(RedisCacheUtils redisCacheUtils){
        this.redisCacheUtils = redisCacheUtils;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 关闭csrf
                .csrf().disable()
                // 不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 部分接口允许匿名访问
                .antMatchers("/user/login").anonymous()
                .antMatchers("/user/register").anonymous()
                .antMatchers("/sms/user/sendRegisterCode").anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        // 把token校验过滤器添加到过滤器链中
        http.addFilterBefore(new AuthenticationTokenFilter(redisCacheUtils), UsernamePasswordAuthenticationFilter.class);

        // 配置异常处理器
        http.exceptionHandling()
                // 配置认证失败处理器
                .authenticationEntryPoint(new AuthenticationEntryPointImpl())
                // 配置授权失败处理器
                .accessDeniedHandler(new AccessDeniedHandlerImpl());

        // 允许跨域
        http.cors()
                .configurationSource(configurationSource());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    CorsConfigurationSource configurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("*"));
        corsConfiguration.setAllowedOriginPatterns(Arrays.asList("*"));
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
