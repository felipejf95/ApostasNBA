package br.ufjf.scabapi.api.controller;

import br.ufjf.scabapi.api.dto.JogadorDTO;
import br.ufjf.scabapi.exception.RegraNegocioException;
import br.ufjf.scabapi.service.JogadorService;
import br.ufjf.scabapi.model.entity.Jogador;
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
@RequestMapping("/api/v1/jogadores")
@RequiredArgsConstructor
@Api("API de Jogadores")
public class JogadorController {

    private final JogadorService service;
    private final ModelMapper modelMapper = new ModelMapper();

    @GetMapping()
    @ApiOperation("Obter a lista de Jogadores")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Jogadores encontrados"),
            @ApiResponse(code = 404, message = "Jogadores não encontrados"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity get() {
        List<Jogador> jogadores = service.getJogadores();
        return ResponseEntity.ok(jogadores.stream().map(JogadorDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um Jogador pelo ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Jogador encontrado"),
            @ApiResponse(code = 404, message = "Jogador não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Jogador> jogadores = service.getJogadorById(id);
        if (!jogadores.isPresent()) {
            return new ResponseEntity("Jogador não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(jogadores.map(JogadorDTO::create));
    }

    @PostMapping()
    @ApiOperation("Cadastrar um novo Jogador")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Jogador criado com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao cadastrar Jogador"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity post(@RequestBody JogadorDTO dto) {
        try {
            Jogador jogador = converter(dto);
            jogador = service.salvar(jogador);
            return new ResponseEntity(jogador, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar um jogador existente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Jogador atualizado com sucesso"),
            @ApiResponse(code = 404, message = "Jogador não encontrado"),
            @ApiResponse(code = 400, message = "Erro ao atualizar jogador"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody JogadorDTO dto) {
        if (!service.getJogadorById(id).isPresent()) {
            return new ResponseEntity("Jogador não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Jogador jogador = converter(dto);
            jogador.setId(id);
            service.salvar(jogador);
            return ResponseEntity.ok(jogador);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Excluir um jogador pelo ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Jogador excluído com sucesso"),
            @ApiResponse(code = 404, message = "Jogador não encontrado"),
            @ApiResponse(code = 400, message = "Erro ao excluir jogador"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<String> excluir(@PathVariable("id") Long id) {
        Optional<Jogador> cliente = service.getJogadorById(id);
        if (!cliente.isPresent()) {
            return new ResponseEntity<>("Jogador não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(cliente.get());
            return ResponseEntity.ok("Jogador excluído com sucesso");
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Jogador converter(JogadorDTO dto) {
        return modelMapper.map(dto, Jogador.class);
    }
}
