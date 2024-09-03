package br.ufjf.scabapi.api.controller;

import br.ufjf.scabapi.api.dto.CampeonatoDTO;
import br.ufjf.scabapi.api.dto.EquipeDTO;
import br.ufjf.scabapi.api.dto.JogoDTO;
import br.ufjf.scabapi.model.entity.Campeonato;
import br.ufjf.scabapi.model.entity.Equipe;
import br.ufjf.scabapi.model.entity.Jogo;
import br.ufjf.scabapi.service.CampeonatoService;
import br.ufjf.scabapi.service.EquipeService;
import br.ufjf.scabapi.service.JogoService;
import io.swagger.annotations.Api;
import br.ufjf.scabapi.exception.RegraNegocioException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/jogos")
@RequiredArgsConstructor
@Api("API de Jogo")
public class JogoController {

    private final JogoService service;
    private final ModelMapper modelMapper = new ModelMapper();

    @GetMapping()
    @ApiOperation("Obter a lista de jogos")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Jogos encontradas"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity get() {
        List<Jogo> jogo = service.getJogos();
        return ResponseEntity.ok(jogo.stream().map(JogoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um jogo pelo ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Jogo encontrado"),
            @ApiResponse(code = 404, message = "Jogo não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Jogo> jogo = service.getJogoById(id);
        if (!jogo.isPresent()) {
            return new ResponseEntity("Jogo não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(jogo.map(JogoDTO::create));
    }

    @PostMapping()
    @ApiOperation("Cadastrar um novo jogo")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Jogo criado com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao cadastrar jogo"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity post(@RequestBody JogoDTO dto) {
        try {
            Jogo jogo = converter(dto);
            jogo = service.salvar(jogo);
            return new ResponseEntity(jogo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar um jogo existente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Jogo atualizado com sucesso"),
            @ApiResponse(code = 404, message = "Jogo não encontrado"),
            @ApiResponse(code = 400, message = "Erro ao atualizar jogo"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody JogoDTO dto) {
        if (!service.getJogoById(id).isPresent()) {
            return new ResponseEntity("Jogo não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Jogo jogo = converter(dto);
            jogo.setId(id);
            service.salvar(jogo);
            return ResponseEntity.ok(jogo);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir um jogo pelo ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Jogo excluído com sucesso"),
            @ApiResponse(code = 404, message = "Jogo não encontrado"),
            @ApiResponse(code = 400, message = "Erro ao excluir jogo"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Jogo> jogo = service.getJogoById(id);
        if (!jogo.isPresent()) {
            return new ResponseEntity("Jogo não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(jogo.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Jogo converter(JogoDTO dto) {
        return modelMapper.map(dto, Jogo.class);
    }
}
