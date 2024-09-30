package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.interdisciplinary.incluses.models.SituacaoTrabalhista;

import java.util.UUID;

public interface SituacaoTrabalhistaRepository extends JpaRepository<SituacaoTrabalhista, UUID> {
}
