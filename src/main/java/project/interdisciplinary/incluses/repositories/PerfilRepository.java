package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.interdisciplinary.incluses.models.Perfil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PerfilRepository extends JpaRepository<Perfil, UUID> {
    Optional<Perfil> findPerfilByEmail(String username);

    Optional<List<Perfil>> findPerfilsByNomeContains(String username);

}
