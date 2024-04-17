package br.ufjf.scabapi.service;

import br.ufjf.scabapi.model.entity.Campeonato;
import br.ufjf.scabapi.model.repository.CampeonatoRepository;

import java.util.List;
import java.util.Optional;

public class CampeonatoService {

    private CampeonatoRepository repository;

    public CampeonatoService(CampeonatoRepository repository){
        this.repository = repository;
    }

    public List<Campeonato> getCampeonato(){
        return repository.findAll();
    }

    public Optional<Campeonato> getCampeonatoById (Long id){
        return repository.findById(id);
    }
}
