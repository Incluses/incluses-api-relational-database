package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.interdisciplinary.incluses.models.TipoArquivo;

import java.util.Optional;
import java.util.UUID;

public interface TipoArquivoRepository extends JpaRepository<TipoArquivo, UUID> {
    Optional<TipoArquivo> findByNomeIgnoreCase(String nome);
}
