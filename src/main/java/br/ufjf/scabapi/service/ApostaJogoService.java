package br.ufjf.scabapi.service;

import br.ufjf.scabapi.exception.RegraNegocioException;
import br.ufjf.scabapi.model.entity.ApostaEquipe;
import br.ufjf.scabapi.model.entity.ApostaJogo;
import br.ufjf.scabapi.model.repository.ApostaJogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ApostaJogoService {

    @Autowired
    private ApostaJogoRepository repository;

    public ApostaJogoService(ApostaJogoRepository repository) {
        this.repository = repository;
    }

    public List<ApostaJogo> getApostasJogo() {
        return repository.findAll();
    }

    public Optional<ApostaJogo> getApostaJogoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public ApostaJogo salvar(ApostaJogo aposta) {
        validar(aposta);
        return repository.save(aposta);
    }

    @Transactional
    public void excluir(ApostaJogo aposta) {
        Objects.requireNonNull(aposta.getId());
        repository.delete(aposta);
    }


    public void validar(ApostaJogo aposta) {
        if (aposta.getValor() == 0 || aposta.getOdd() == 0) {
            throw new RegraNegocioException("O valor da aposta nao pode ser 0");
        }
    }

    public List<ApostaJogo> getApostasJogoByJogadorId(Long jogadorId) {
        return repository.findByJogadorId(jogadorId);
    }

    public List<ApostaJogo> getApostaPorJogador(Long jogadorId) {
        return repository.findByJogadorId(jogadorId);
    }

}
