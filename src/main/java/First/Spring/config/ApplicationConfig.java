package First.Spring.config;

import First.Spring.model.User;
import First.Spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@RequiredArgsConstructor
@Configuration
public class ApplicationConfig {

    private final UserRepository repository;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                Optional<User> byEmail = repository.findByEmail(username);
                if(byEmail.isEmpty()){
                    throw new UsernameNotFoundException("user not found");
                }
                First.Spring.model.User dbUser = byEmail.get();
//                List<Role> roles = new ArrayList<>(Arrays.asList(Role.USER,Role.ADMIN));
//
//                List<SimpleGrantedAuthority> authorities = new ArrayList<>(); //= List.of(new SimpleGrantedAuthority("USER"), new SimpleGrantedAuthority("ADMIN"));
//                roles.forEach(e->{
//                   authorities.add( new SimpleGrantedAuthority(e.name()));
//                });
//                dbUser.eee(e->{
//                    return e;
//                });
//                UserDetails userDetails = new User(dbUser.getEmail(), dbUser.getPassword(), authorities);
                return dbUser;
            }
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
