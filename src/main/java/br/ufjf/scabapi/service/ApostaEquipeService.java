package br.ufjf.scabapi.service;


import br.ufjf.scabapi.exception.RegraNegocioException;
import br.ufjf.scabapi.model.entity.ApostaCampeonato;
import br.ufjf.scabapi.model.entity.ApostaEquipe;
import br.ufjf.scabapi.model.repository.ApostaEquipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ApostaEquipeService {

    @Autowired
    private ApostaEquipeRepository repository;

    public ApostaEquipeService(ApostaEquipeRepository repository) {
        this.repository = repository;
    }

    public List<ApostaEquipe> getApostas() {
        return repository.findAll();
    }

    public Optional<ApostaEquipe> getApostaById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public ApostaEquipe salvar(ApostaEquipe aposta) {
        validar(aposta);
        return repository.save(aposta);
    }

    @Transactional
    public void excluir(ApostaEquipe aposta) {
        Objects.requireNonNull(aposta.getId());
        repository.delete(aposta);
    }


    public void validar(ApostaEquipe aposta) {
        if (aposta.getValor() == 0 || aposta.getOdd() == 0) {
            throw new RegraNegocioException("O valor da aposta nao pode ser 0");
        }
    }

    public List<ApostaEquipe> getApostasEquipeByJogadorId(Long jogadorId) {
        return repository.findByJogadorId(jogadorId);
    }

    public List<ApostaEquipe> getApostaPorJogador(Long jogadorId) {
        return repository.findByJogadorId(jogadorId);
    }
}