package com.nilangpatel.blogpress.security;

import com.nilangpatel.blogpress.constants.BlogpressConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@ComponentScan("com.nilangpatel.blogpress.security")
public class WebSecurityConfig implements WebMvcConfigurer {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/**", "/api/listBlogs", "/api/listAllComments", "/viewBlog", "/addComment", "/search").permitAll()
                        .requestMatchers("/controlPage", "/addNewBlog", "/manageBlogs", "/showUpdateBlogPage", "/showAddNew").hasAnyAuthority(BlogpressConstants.ROLE_USER, BlogpressConstants.ROLE_ADMIN)
                        .requestMatchers("/showComments", "/updateCommentStatus", "/replyComment").hasAnyAuthority(BlogpressConstants.ROLE_ADMIN)
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/controlPage", true)
                        .failureUrl("/login?error=true")
                )
                .httpBasic(withDefaults())
                .logout()
                .permitAll()
                .logoutSuccessUrl("/login?logout=true")
                ;
        return http.build();
    }
    @Bean
    public ViewResolver internalResourceViewResolver() {
        InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setViewClass(JstlView.class);
        bean.setPrefix("/WEB-INF/view/");
        bean.setSuffix(".jsp");
        return bean;
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/static/**", "/css/static/**", "/js/static/**")
                .addResourceLocations("/resources/static/");
    }
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("blogUser1")
                .password("password")
                .roles("USER")
                .build();
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("blogAdmin")
                .password("password")
                .roles("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

}
