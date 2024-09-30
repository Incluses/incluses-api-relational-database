package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.interdisciplinary.incluses.models.Configuracao;

import java.util.UUID;

public interface ConfiguracaoRepository extends JpaRepository<Configuracao, UUID> {
}
