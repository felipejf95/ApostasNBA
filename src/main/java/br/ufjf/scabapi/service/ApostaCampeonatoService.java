package br.ufjf.scabapi.service;

import br.ufjf.scabapi.exception.RegraNegocioException;
import br.ufjf.scabapi.model.entity.ApostaCampeonato;
import br.ufjf.scabapi.model.repository.ApostaCampeonatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ApostaCampeonatoService {

    @Autowired
    private ApostaCampeonatoRepository repository;

    public ApostaCampeonatoService(ApostaCampeonatoRepository repository) {
        this.repository = repository;
    }

    public List<ApostaCampeonato> getApostasCampeonato() {
        return repository.findAll();
    }

    public Optional<ApostaCampeonato> getApostaCampeonatoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public ApostaCampeonato salvar(ApostaCampeonato aposta) {
        validar(aposta);
        return repository.save(aposta);
    }

    @Transactional
    public void excluir(ApostaCampeonato aposta) {
        Objects.requireNonNull(aposta.getId());
        repository.delete(aposta);
    }


    public void validar(ApostaCampeonato aposta) {
        if (aposta.getValor() == 0 || aposta.getOdd() == 0) {
            throw new RegraNegocioException("O valor da aposta nao pode ser 0");
        }
    }


    public List<ApostaCampeonato> getApostasCampeonatoByJogadorId(Long jogadorId) {
        return repository.findByJogadorId(jogadorId);
    }

    public List<ApostaCampeonato> getApostaPorJogador(Long jogadorId) {
        return repository.findByJogadorId(jogadorId);
    }
}
