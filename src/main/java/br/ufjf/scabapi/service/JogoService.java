package br.ufjf.scabapi.service;

import br.ufjf.scabapi.exception.RegraNegocioException;
import br.ufjf.scabapi.model.entity.Jogador;
import br.ufjf.scabapi.model.entity.Jogo;
import br.ufjf.scabapi.model.repository.JogoRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
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

    @Transactional
    public Jogo salvar(Jogo jogo){
        validar(jogo);
        return repository.save(jogo);
    }

    @Transactional
    public void excluir(Jogo jogo){
        Objects.requireNonNull(jogo.getId());
        repository.delete(jogo);
    }

    public void validar(Jogo jogo){
        if (jogo.getEquipeA() == null){
            throw new RegraNegocioException("A equipe A deve existir");
        }
        if (jogo.getEquipeB() == null){
            throw new RegraNegocioException("A equipe B deve existir");
        }
    }
}
