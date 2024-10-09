package project.interdisciplinary.incluses.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import project.interdisciplinary.incluses.filter.JwtAuthenticationFilter;
import project.interdisciplinary.incluses.services.PerfilService;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final PerfilService userDetailsService;
    public SecurityConfig(PerfilService userDetailsService){
        this.userDetailsService = userDetailsService;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoints públicos
                        .requestMatchers("/api/auth/login", "/usuario/public/inserir", "/empresa/public/inserir", "/swagger-ui/**", "v3/api-docs/**").permitAll()

                        // Endpoints com roles específicas
                        .requestMatchers("/**").hasRole("EMPRESA")
                        .requestMatchers("/**").hasRole("USUARIO")

                        // Todas as outras requisições devem ser autenticadas
                        .anyRequest().authenticated()
                )
                // Desabilita a página de login padrão e CSRF (para APIs REST)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)

                // Adiciona o filtro de JWT antes do UsernamePasswordAuthenticationFilter
                .addFilterBefore(new JwtAuthenticationFilter(userDetailsService, secretKey()), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecretKey secretKey(){
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }
}

