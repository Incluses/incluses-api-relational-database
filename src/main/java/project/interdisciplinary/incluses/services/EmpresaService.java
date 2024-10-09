package project.interdisciplinary.incluses.services;

import org.springframework.stereotype.Service;
import project.interdisciplinary.incluses.models.Empresa;
import project.interdisciplinary.incluses.models.dto.CriarEmpresaDTO;
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
    public boolean excluirEmpresa(UUID id){
        empresaRepository.deletarEmpresa(id);
        Optional<Empresa> empr = empresaRepository.findById(id);
        if(empr.isPresent()){
            return false;
        }
        else {
            return true;
        }
    }
    public void criarEmpresa(CriarEmpresaDTO criarEmpresaDTO){
        empresaRepository.criarEmpresa(criarEmpresaDTO.getCnpj(), criarEmpresaDTO.getRazaoSocial(), criarEmpresaDTO.getWebsite(),
                criarEmpresaDTO.getSetor(), criarEmpresaDTO.getRua(), criarEmpresaDTO.getEstado(), criarEmpresaDTO.getCidade(),
                criarEmpresaDTO.getCep(), criarEmpresaDTO.getNumero(), criarEmpresaDTO.getNome(), criarEmpresaDTO.getSenha(),
                criarEmpresaDTO.getEmail(), criarEmpresaDTO.getTelefone());
    }
}
