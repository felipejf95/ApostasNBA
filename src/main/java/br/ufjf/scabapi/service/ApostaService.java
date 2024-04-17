package br.ufjf.scabapi.service;

import br.ufjf.scabapi.model.entity.Aposta;
import br.ufjf.scabapi.model.repository.ApostaRepository;

import java.util.List;
import java.util.Optional;

public class ApostaService {

    private ApostaRepository repository;

    public ApostaService(ApostaRepository repository){
        this.repository = repository;
    }

    public List<Aposta> getAposta(){
        return repository.findAll();
    }

    public Optional<Aposta> getApostaById (Long id){
        return repository.findById(id);
    }
}
