package br.ufjf.scabapi.service;

import br.ufjf.scabapi.model.entity.Jogador;
import br.ufjf.scabapi.model.repository.JogadorRepository;

import java.util.List;
import java.util.Optional;

public class JogadorService {

    private JogadorRepository repository;

    public JogadorService(JogadorRepository repository){
        this.repository = repository;
    }

    public List<Jogador> getJogadores(){
        return repository.findAll();
    }

    public Optional<Jogador> getJogadorById (Long id){
        return repository.findById(id);
    }
}
