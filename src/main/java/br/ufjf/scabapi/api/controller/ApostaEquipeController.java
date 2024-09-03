package br.ufjf.scabapi.api.controller;

import br.ufjf.scabapi.api.dto.ApostaEquipeDTO;
import br.ufjf.scabapi.exception.RegraNegocioException;
import br.ufjf.scabapi.model.entity.ApostaEquipe;
import br.ufjf.scabapi.model.entity.Jogador;
import br.ufjf.scabapi.service.ApostaEquipeService;
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
@RequestMapping("/api/v1/apostasdeequipe")
@RequiredArgsConstructor
@Api("API de Apostas de Equipes")
public class ApostaEquipeController {

    private final ApostaEquipeService service;
    private final JogadorService jogadorService;
    private final ModelMapper modelMapper = new ModelMapper();

    @GetMapping()
    @ApiOperation("Obter a lista de apostas de equipe")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Apostas de equipe encontradas"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity get() {
        List<ApostaEquipe> apostaEquipes = service.getApostasEquipe();
        return ResponseEntity.ok(apostaEquipes.stream().map(ApostaEquipeDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de uma aposta de equipe pelo ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Aposta de equipe encontrada"),
            @ApiResponse(code = 404, message = "Aposta de equipe não encontrada"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<ApostaEquipe> apostaEquipes = service.getApostaEquipeById(id);
        if (!apostaEquipes.isPresent()) {
            return new ResponseEntity("Aposta de equipe não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(apostaEquipes.map(ApostaEquipeDTO::create));
    }

    @PostMapping()
    @ApiOperation("Cadastrar uma nova aposta de equipe")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Aposta de equipe criada com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao cadastrar aposta de equipe"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity post(@RequestBody ApostaEquipeDTO dto) {
        try {
            ApostaEquipe apostaEquipes = converter(dto);
            apostaEquipes = service.salvar(apostaEquipes);
            return new ResponseEntity(apostaEquipes, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar uma conta aposta de equipe")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Aopsta de equipe atualizada com sucesso"),
            @ApiResponse(code = 404, message = "Aposta de equipe não encontrada"),
            @ApiResponse(code = 400, message = "Erro ao atualizar aposta de equipe"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ApostaEquipeDTO dto) {
        if (!service.getApostaEquipeById(id).isPresent()) {
            return new ResponseEntity("Aposta de equipe não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            ApostaEquipe apostaEquipes = converter(dto);
            apostaEquipes.setId(id);
            service.salvar(apostaEquipes);
            return ResponseEntity.ok(apostaEquipes);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Excluir uma aposta de equipe pelo ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Aposta de equipe excluída com sucesso"),
            @ApiResponse(code = 404, message = "Aposta de equipe não encontrada"),
            @ApiResponse(code = 400, message = "Erro ao excluir aposta de equipe"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<String> excluir(@PathVariable("id") Long id) {
        Optional<ApostaEquipe> apostaEquipes = service.getApostaEquipeById(id);
        if (!apostaEquipes.isPresent()) {
            return new ResponseEntity<>("Aposta de equipe não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(apostaEquipes.get());
            return ResponseEntity.ok("Aposta de equipe excluída com sucesso");
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/jogador/{jogadorId}")
    @ApiOperation("Obter apostas de equipe por ID do jogador")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Apostas de equipe encontradas"),
            @ApiResponse(code = 404, message = "Jogador não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<List<ApostaEquipeDTO>> getApostasEquipePorCliente(@PathVariable("jogadorId") Long jogadorId) {
        List<ApostaEquipe> apostas = service.getApostaPorJogador(jogadorId);
        return ResponseEntity.ok(apostas.stream().map(ApostaEquipeDTO::create).collect(Collectors.toList()));
    }

    private ApostaEquipe converter(ApostaEquipeDTO dto) {
        ApostaEquipe apostaEquipes = modelMapper.map(dto, ApostaEquipe.class);
        if (dto.getJogadorId() != null) {
            Optional<Jogador> jogador = jogadorService.getJogadorById(dto.getJogadorId());
            if (jogador.isPresent()) {
                apostaEquipes.setJogador(jogador.get());
            } else {
                throw new RegraNegocioException("Jogador não encontrado");
            }
        }
        return apostaEquipes;
    }
}
