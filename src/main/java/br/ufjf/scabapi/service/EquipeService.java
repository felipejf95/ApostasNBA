package br.ufjf.scabapi.service;

import br.ufjf.scabapi.model.entity.Equipe;
import br.ufjf.scabapi.model.repository.EquipeRepository;

import java.util.List;
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
}
