package br.ufjf.scabapi.api.controller;

import br.ufjf.scabapi.api.dto.ApostaCampeonatoDTO;
import br.ufjf.scabapi.api.dto.ApostaJogoDTO;
import br.ufjf.scabapi.exception.RegraNegocioException;
import br.ufjf.scabapi.model.entity.ApostaCampeonato;
import br.ufjf.scabapi.model.entity.ApostaJogo;
import br.ufjf.scabapi.model.entity.Jogador;
import br.ufjf.scabapi.service.ApostaCampeonatoService;
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
@RequestMapping("/api/v1/apostasdeecampeonato")
@RequiredArgsConstructor
@Api("API de Apostas de Campeonatos")
public class ApostaCampeonatoController {

    private final ApostaCampeonatoService service;
    private final JogadorService jogadorService;
    private final ModelMapper modelMapper = new ModelMapper();

    @GetMapping()
    @ApiOperation("Obter a lista de apostas de campeonato")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Apostas de campeonato encontradas"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity get() {
        List<ApostaCampeonato> apostaCampeonatos = service.getApostasCampeonato();
        return ResponseEntity.ok(apostaCampeonatos.stream().map(ApostaCampeonatoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de uma aposta de campeonato pelo ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Aposta de campeonato encontrada"),
            @ApiResponse(code = 404, message = "Aposta de campeonato não encontrada"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<ApostaCampeonato> apostaCampeonatos = service.getApostaCampeonatoById(id);
        if (!apostaCampeonatos.isPresent()) {
            return new ResponseEntity("Aposta de campeonato não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(apostaCampeonatos.map(ApostaCampeonatoDTO::create));
    }

    @PostMapping()
    @ApiOperation("Cadastrar uma nova aposta de campeonato")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Aposta de campeonato criada com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao cadastrar aposta de campeonato"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity post(@RequestBody ApostaCampeonatoDTO dto) {
        try {
            ApostaCampeonato apostaCampeonatos = converter(dto);
            apostaCampeonatos = service.salvar(apostaCampeonatos);
            return new ResponseEntity(apostaCampeonatos, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar uma conta aposta de campeonato")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Aopsta de campeonato atualizada com sucesso"),
            @ApiResponse(code = 404, message = "Aposta de campeonato não encontrada"),
            @ApiResponse(code = 400, message = "Erro ao atualizar aposta de campeonato"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ApostaCampeonatoDTO dto) {
        if (!service.getApostaCampeonatoById(id).isPresent()) {
            return new ResponseEntity("Aposta de campeonato não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            ApostaCampeonato apostaCampeonatos = converter(dto);
            apostaCampeonatos.setId(id);
            service.salvar(apostaCampeonatos);
            return ResponseEntity.ok(apostaCampeonatos);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Excluir uma aposta de campeonato pelo ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Aposta de campeonato excluída com sucesso"),
            @ApiResponse(code = 404, message = "Aposta de campeonato não encontrada"),
            @ApiResponse(code = 400, message = "Erro ao excluir aposta de campeonato"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<String> excluir(@PathVariable("id") Long id) {
        Optional<ApostaCampeonato> apostaCampeonatos = service.getApostaCampeonatoById(id);
        if (!apostaCampeonatos.isPresent()) {
            return new ResponseEntity<>("Aposta de campeonato não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(apostaCampeonatos.get());
            return ResponseEntity.ok("Aposta de campeonato excluída com sucesso");
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/jogador/{jogadorId}")
    @ApiOperation("Obter apostas de campeonato por ID do jogador")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Apostas de campeonato encontradas"),
            @ApiResponse(code = 404, message = "Jogador não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<List<ApostaCampeonatoDTO>> getApostasCampeonatoPorCliente(@PathVariable("jogadorId") Long jogadorId) {
        List<ApostaCampeonato> apostas = service.getApostaPorJogador(jogadorId);
        return ResponseEntity.ok(apostas.stream().map(ApostaCampeonatoDTO::create).collect(Collectors.toList()));
    }

    private ApostaCampeonato converter(ApostaCampeonatoDTO dto) {
        ApostaCampeonato apostaCampeonatos = modelMapper.map(dto, ApostaCampeonato.class);
        if (dto.getJogadorId() != null) {
            Optional<Jogador> jogador = jogadorService.getJogadorById(dto.getJogadorId());
            if (jogador.isPresent()) {
                apostaCampeonatos.setJogador(jogador.get());
            } else {
                throw new RegraNegocioException("Jogador não encontrado");
            }
        }
        return apostaCampeonatos;
    }
}
