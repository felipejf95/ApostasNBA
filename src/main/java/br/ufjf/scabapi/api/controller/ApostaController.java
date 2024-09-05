package br.ufjf.scabapi.api.controller;

import br.ufjf.scabapi.api.dto.ApostaCampeonatoDTO;
import br.ufjf.scabapi.api.dto.ApostaDTO;
import br.ufjf.scabapi.api.dto.ApostaEquipeDTO;
import br.ufjf.scabapi.api.dto.ApostaJogoDTO;
import br.ufjf.scabapi.exception.RegraNegocioException;
import br.ufjf.scabapi.model.entity.ApostaCampeonato;
import br.ufjf.scabapi.model.entity.ApostaEquipe;
import br.ufjf.scabapi.model.entity.ApostaJogo;
import br.ufjf.scabapi.service.ApostaCampeonatoService;
import br.ufjf.scabapi.service.ApostaEquipeService;
import br.ufjf.scabapi.service.ApostaJogoService;
import br.ufjf.scabapi.service.ApostaService;
import br.ufjf.scabapi.model.entity.Aposta;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/apostas")
@RequiredArgsConstructor
@Api("API de Apostas")
public class ApostaController {

    @Autowired
    private ApostaCampeonatoService apostaCampeonatoService;

    @Autowired
    private ApostaEquipeService apostaEquipeService;

    @Autowired
    private ApostaJogoService apostaJogoService;

    private final ApostaService service;
    private final ModelMapper modelMapper = new ModelMapper();

    @GetMapping()
    @ApiOperation("Obter a lista de apostas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Apostas encontradas"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity get() {
        List<Aposta> apostas = service.getApostas();
        return ResponseEntity.ok(apostas.stream().map(ApostaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de uma aposta pelo ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Aposta encontrada"),
            @ApiResponse(code = 404, message = "Aposta não encontrada"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Aposta> aposta = service.getApostaById(id);
        if (!aposta.isPresent()) {
            return new ResponseEntity("Aposta não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(aposta.map(ApostaDTO::create));
    }

    @PostMapping()
    @ApiOperation("Cadastrar uma nova aposta")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Aposta criada com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao cadastrar aposta"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity post(@RequestBody ApostaDTO dto) {
        try {
            Aposta aposta = converter(dto);
            aposta = service.salvar(aposta);
            return new ResponseEntity(aposta, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar uma aposta existente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Aposta atualizada com sucesso"),
            @ApiResponse(code = 404, message = "Aposta não encontrada"),
            @ApiResponse(code = 400, message = "Erro ao atualizar aposta"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ApostaDTO dto) {
        if (!service.getApostaById(id).isPresent()) {
            return new ResponseEntity("Conta não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Aposta aposta = converter(dto);
            aposta.setId(id);
            service.salvar(aposta);
            return ResponseEntity.ok(aposta);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Excluir uma aposta pelo ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Aposta excluída com sucesso"),
            @ApiResponse(code = 404, message = "Aposta não encontrada"),
            @ApiResponse(code = 400, message = "Erro ao excluir aposta"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<String> excluir(@PathVariable("id") Long id) {
        Optional<Aposta> aposta = service.getApostaById(id);
        if (!aposta.isPresent()) {
            return new ResponseEntity<>("Aposta não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(aposta.get());
            return ResponseEntity.ok("Aposta excluída com sucesso");
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/depositar")
    @ApiOperation("Realizar uma aposta")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Aposta realizado com sucesso"),
            @ApiResponse(code = 404, message = "Aposta não encontrada"),
            @ApiResponse(code = 400, message = "Erro ao realizar aposta"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity depositar(@PathVariable("id") Long id, @RequestParam Double valor) {
        Optional<Aposta> aposta = service.getApostaById(id);
        if (!aposta.isPresent()) {
            return new ResponseEntity("Aposta não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            aposta.get().calcularRetorno(valor);
            service.salvar(aposta.get());
            return ResponseEntity.ok(aposta.get());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/cliente/{jogadorID}")
    @ApiOperation("Obter apostas por ID do Jogador")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Apostas encontradas"),
            @ApiResponse(code = 404, message = "Jogador não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<List<ApostaDTO>> getApostasByJogador(@PathVariable("jogadorId") Long jogadorId) {
        List<ApostaCampeonato> apostasCampeonato = apostaCampeonatoService.getApostasCampeonatoByJogadorId(jogadorId);
        List<ApostaEquipe> apostasEquipe = apostaEquipeService.getApostasEquipeByJogadorId(jogadorId);
        List<ApostaJogo> apostasJogo = apostaJogoService.getApostasJogoByJogadorId(jogadorId);


        List<ApostaDTO> apostas = new ArrayList<>();

        apostas.addAll(apostasCampeonato.stream()
                .map(ApostaCampeonatoDTO::create)
                .collect(Collectors.toList()));

        apostas.addAll(apostasEquipe.stream()
                .map(ApostaEquipeDTO::create)
                .collect(Collectors.toList()));

        apostas.addAll(apostasJogo.stream()
                .map(ApostaJogoDTO::create)
                .collect(Collectors.toList()));

        return ResponseEntity.ok(apostas);
    }


    private Aposta converter(ApostaDTO dto) {
        return modelMapper.map(dto, Aposta.class);
    }
}