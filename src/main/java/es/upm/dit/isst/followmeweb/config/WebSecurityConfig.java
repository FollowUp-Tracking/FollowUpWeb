package es.upm.dit.isst.followmeweb.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
    DataSource ds;

    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().passwordEncoder(new BCryptPasswordEncoder())
			.dataSource(ds)
			.usersByUsernameQuery("select username, password, enable from usuarios where username=?")
			.authoritiesByUsernameQuery("select username, rol from usuarios where username=?");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
        .authorizeRequests()
			.antMatchers("/css/**", "/img/**", "/layouts/**").permitAll()
			.antMatchers("/usuarios").hasAnyRole("ADM")
			.antMatchers("/historico/**", "/mapa/**").hasAnyRole("ADM", "EMP", "CLI", "REP")
			.antMatchers("/", "/inicio", "/register", "/guardar", "/login").permitAll()
			.anyRequest().authenticated()
        .and()
            .formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/inicio", true)
				.permitAll()
		.and()
            .logout()
			.permitAll();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}