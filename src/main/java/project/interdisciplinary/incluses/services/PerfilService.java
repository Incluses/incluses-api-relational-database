package project.interdisciplinary.incluses.services;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.interdisciplinary.incluses.models.Perfil;
import project.interdisciplinary.incluses.repositories.PerfilRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PerfilService implements UserDetailsService {
    private final PerfilRepository perfilRepository;
    public PerfilService(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }
    public List<Perfil> listarPerfis() {
        return perfilRepository.findAll();
    }

    public Perfil salvarPerfil(Perfil perfil){
        return perfilRepository.save(perfil);
    }

    public Perfil buscarPerfilPorId(UUID id){
        return perfilRepository.findById(id).orElseThrow(() -> new RuntimeException("Perfil não encontrado"));
    }
    public Perfil excluirPerfil(UUID id){
        Optional<Perfil> perf = perfilRepository.findById(id);
        if(perf.isPresent()){
            perfilRepository.deleteById(id);
            return perf.get();
        }
        return null;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Perfil users = perfilRepository.findPerfilByEmail(username).
                orElseThrow(()->new UsernameNotFoundException("Usuário não existe"));
        return new org.springframework.security.core.userdetails.User(
                users.getEmail(),
                users.getSenha(),
                true,
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority(users.getTipoPerfil().getNome()))
        );
    }
}
