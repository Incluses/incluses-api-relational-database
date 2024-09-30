package project.interdisciplinary.incluses.controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import project.interdisciplinary.incluses.models.LoginRequest;
import project.interdisciplinary.incluses.models.Perfil;
import project.interdisciplinary.incluses.repositories.PerfilRepository;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.Optional;


@RestController
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    public final SecretKey secretKey;
    private final PerfilRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(PerfilRepository userRepository, PasswordEncoder passwordEncoder
            , SecretKey secretKey) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.secretKey = secretKey;
    }

    @PostMapping("/api/auth/login")
    public Map<String, String> login(@RequestBody LoginRequest loginRequest) {
        Optional<Perfil> users = userRepository.findPerfilByEmail(loginRequest.getEmail());
        if (users.isPresent()){
            Perfil user = users.get();

            if (loginRequest.getSenha().equals( user.getSenha())) {
                try {
                    String token = Jwts.builder()
                            .setSubject(loginRequest.getEmail())
                            .claim("role", user.getTipoPerfil().getNome())
                            .setExpiration(new Date(System.currentTimeMillis() + 86_400_000))
                            .signWith(secretKey, SignatureAlgorithm.HS512)
                            .compact();
                    logger.info("Generated Token: {}", token);
                    return Map.of("token", "Bearer " + token, "perfil", user.getTipoPerfil().getNome());
                } catch (Exception e) {
                    logger.error("Erro ao gerar o token JWT: ", e);
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao gerar o token JWT", e);
                }
            } else {
                logger.error("Usuário ou senha inválidos: {} {} {} {}", loginRequest.getEmail(), user.getEmail(), user.getSenha(), loginRequest.getSenha() );
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos");
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário inexistente");
        }

    }

}
