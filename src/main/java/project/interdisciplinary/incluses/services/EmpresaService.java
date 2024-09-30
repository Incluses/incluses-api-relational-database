package project.interdisciplinary.incluses.services;

import org.springframework.stereotype.Service;
import project.interdisciplinary.incluses.models.Empresa;
import project.interdisciplinary.incluses.repositories.EmpresaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmpresaService {
    private final EmpresaRepository empresaRepository;
    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }
    public List<Empresa> listarEmpresas() {
        return empresaRepository.findAll();
    }

    public Empresa salvarEmpresa(Empresa empresa){
        return empresaRepository.save(empresa);
    }

    public Empresa buscarEmpresaPorId(UUID id){
        return empresaRepository.findById(id).orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
    }
    public Empresa excluirEmpresa(UUID id){
        Optional<Empresa> empr = empresaRepository.findById(id);
        if(empr.isPresent()){
            empresaRepository.deleteById(id);
            return empr.get();
        }
        return null;
    }
}
