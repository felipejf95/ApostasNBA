package br.ufjf.scabapi.service;

import br.ufjf.scabapi.exception.RegraNegocioException;
import br.ufjf.scabapi.model.entity.Campeonato;
import br.ufjf.scabapi.model.repository.CampeonatoRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
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

    @Transactional
    public Campeonato salvar(Campeonato campeonato){
        validar(campeonato);
        return repository.save(campeonato);
    }

    @Transactional
    public void  excluir(Campeonato campeonato){
        Objects.requireNonNull(campeonato.getId());
        repository.delete(campeonato);
    }

    public void validar(Campeonato campeonato){
        if (campeonato.getNome() == null){
            throw new RegraNegocioException("O campeonato deve ter um nome");
        }
    }
}
