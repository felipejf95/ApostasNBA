package br.ufjf.scabapi.api.controller;


import br.ufjf.scabapi.api.dto.ApostaJogoDTO;
import br.ufjf.scabapi.exception.RegraNegocioException;
import br.ufjf.scabapi.model.entity.ApostaJogo;
import br.ufjf.scabapi.model.entity.Jogador;
import br.ufjf.scabapi.service.ApostaJogoService;
import br.ufjf.scabapi.service.JogadorService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/apostasdeejogo")
@RequiredArgsConstructor
@Api("API de Apostas de Jogos")
public class ApostaJogoController {

    private final ApostaJogoService service;
    private final JogadorService jogadorService;
    private final ModelMapper modelMapper = new ModelMapper();

    @GetMapping()
    @ApiOperation("Obter a lista de apostas de jogo")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Apostas de jogo encontradas"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity get() {
        List<ApostaJogo> apostaJogos = service.getApostasJogo();
        return ResponseEntity.ok(apostaJogos.stream().map(ApostaJogoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de uma aposta de jogo pelo ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Aposta de jogo encontrada"),
            @ApiResponse(code = 404, message = "Aposta de jogo não encontrada"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<ApostaJogo> apostaJogos = service.getApostaJogoById(id);
        if (!apostaJogos.isPresent()) {
            return new ResponseEntity("Aposta de jogo não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(apostaJogos.map(ApostaJogoDTO::create));
    }

    @PostMapping()
    @ApiOperation("Cadastrar uma nova aposta de jogo")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Aposta de jogo criada com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao cadastrar aposta de jogo"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity post(@RequestBody ApostaJogoDTO dto) {
        try {
            ApostaJogo apostaJogos = converter(dto);
            apostaJogos = service.salvar(apostaJogos);
            return new ResponseEntity(apostaJogos, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar uma conta aposta de jogo")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Aopsta de jogo atualizada com sucesso"),
            @ApiResponse(code = 404, message = "Aposta de jogo não encontrada"),
            @ApiResponse(code = 400, message = "Erro ao atualizar aposta de jogo"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ApostaJogoDTO dto) {
        if (!service.getApostaJogoById(id).isPresent()) {
            return new ResponseEntity("Aposta de jogo não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            ApostaJogo apostaJogos = converter(dto);
            apostaJogos.setId(id);
            service.salvar(apostaJogos);
            return ResponseEntity.ok(apostaJogos);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Excluir uma aposta de jogo pelo ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Aposta de jogo excluída com sucesso"),
            @ApiResponse(code = 404, message = "Aposta de jogo não encontrada"),
            @ApiResponse(code = 400, message = "Erro ao excluir aposta de jogo"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<String> excluir(@PathVariable("id") Long id) {
        Optional<ApostaJogo> apostaJogos = service.getApostaJogoById(id);
        if (!apostaJogos.isPresent()) {
            return new ResponseEntity<>("Aposta de jogo não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(apostaJogos.get());
            return ResponseEntity.ok("Aposta de jogo excluída com sucesso");
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/jogador/{jogadorId}")
    @ApiOperation("Obter apostas de jogo por ID do jogador")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Apostas de jogo encontradas"),
            @ApiResponse(code = 404, message = "Jogador não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<List<ApostaJogoDTO>> getApostasJogoPorCliente(@PathVariable("jogadorId") Long jogadorId) {
        List<ApostaJogo> apostas = service.getApostaPorJogador(jogadorId);
        return ResponseEntity.ok(apostas.stream().map(ApostaJogoDTO::create).collect(Collectors.toList()));
    }

    private ApostaJogo converter(ApostaJogoDTO dto) {
        ApostaJogo apostaJogos = modelMapper.map(dto, ApostaJogo.class);
        if (dto.getJogadorId() != null) {
            Optional<Jogador> jogador = jogadorService.getJogadorById(dto.getJogadorId());
            if (jogador.isPresent()) {
                apostaJogos.setJogador(jogador.get());
            } else {
                throw new RegraNegocioException("Jogador não encontrado");
            }
        }
        return apostaJogos;
    }
}
