package br.ufjf.scabapi.service;

import br.ufjf.scabapi.model.entity.Jogo;
import br.ufjf.scabapi.model.repository.JogoRepository;

import java.util.List;
import java.util.Optional;

public class JogoService {

    private JogoRepository repository;

    public JogoService(JogoRepository repository){
        this.repository = repository;
    }

    public List<Jogo> getJogos(){
        return repository.findAll();
    }

    public Optional<Jogo> getJogoById(Long id){
        return repository.findById(id);
    }
}
