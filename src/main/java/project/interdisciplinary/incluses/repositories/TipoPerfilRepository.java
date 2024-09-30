package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.interdisciplinary.incluses.models.TipoPerfil;

import java.util.UUID;

public interface TipoPerfilRepository extends JpaRepository<TipoPerfil, UUID> {
}
