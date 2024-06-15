package travel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

// enables configuration
@Configuration
//forgot to allow for web security access (added line)
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http // ALL PAGES NEED MAPPING IN ORDER TO BE RESTRICTED OR UNRESTRICTED. Default is restricted.
        .csrf(AbstractHttpConfigurer::disable) // added line should allow for POST MAPPING
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                .requestMatchers("/admin").authenticated() // secured pages (admin doesn't exist for now, it is a placeholder)
                .requestMatchers("/", "/index", "/register", "/login", "/error_page").permitAll() // non secured
                .anyRequest().authenticated()
            )
            .httpBasic(withDefaults());

        return http.build();
    }
	
	// interface creates user data along with encrypting the password
    @Bean
    public InMemoryUserDetailsManager userDetailsService() { 
        //not needed password encoder
       // PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails user = User.withUsername("Host")
        //
            // don't do this encode online  .password(encoder.encode("WiThTheMosT!23"))
            //withthemost23
            .password("$2a$12$hNr862qHiNdMvYA50R2houEOeqleDs1NIQ/UFNcMKvnpco98v/xzm")
            .roles("USER", "ADMIN")
            .build();
        
        // Log the user details ... might be redundant
        System.out.println("Created user: " + user.getUsername() + ", Password: " + user.getPassword());

        return new InMemoryUserDetailsManager(user);
    }

	
	// Allows for passwords to be encrypted
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    
    
}
	
