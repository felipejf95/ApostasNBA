package br.ufjf.scabapi.service;

import br.ufjf.scabapi.exception.RegraNegocioException;
import br.ufjf.scabapi.model.entity.Equipe;
import br.ufjf.scabapi.model.repository.EquipeRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class EquipeService {

    private EquipeRepository repository;

    public EquipeService(EquipeRepository repository){
        this.repository = repository;
    }

    public List<Equipe> getEquipe(){
        return repository.findAll();
    }

    public Optional<Equipe> getEquipeById (Long id){
        return repository.findById(id);
    }

    @Transactional
    public Equipe salvar(Equipe equipe){
        validar(equipe);
        return repository.save(equipe);
    }

    @Transactional
    public void excluir(Equipe equipe){
        Objects.requireNonNull(equipe.getId());
        repository.delete(equipe);
    }

    public void validar(Equipe equipe){
        if (equipe.getNome() == null){
            throw new RegraNegocioException("A equipe deve ter um nome");
        }
    }
}
