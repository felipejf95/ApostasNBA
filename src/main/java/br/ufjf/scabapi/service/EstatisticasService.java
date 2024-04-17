package br.ufjf.scabapi.service;

import br.ufjf.scabapi.model.entity.Estatisticas;
import br.ufjf.scabapi.model.repository.EstatisticasRepository;

import java.util.List;
import java.util.Optional;

public class EstatisticasService {

    private EstatisticasRepository repository;

    public EstatisticasService(EstatisticasRepository repository){
        this.repository = repository;
    }

    public List<Estatisticas> getEstatisticas(){
        return repository.findAll();
    }

    public Optional<Estatisticas> getEstatisticasById (Long id){
        return repository.findById(id);
    }
}
