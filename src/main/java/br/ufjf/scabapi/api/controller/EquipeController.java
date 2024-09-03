package br.ufjf.scabapi.api.controller;

import br.ufjf.scabapi.api.dto.EquipeDTO;
import br.ufjf.scabapi.model.entity.Equipe;
import br.ufjf.scabapi.service.EquipeService;
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
@RequestMapping("/api/v1/equipes")
@RequiredArgsConstructor
@Api("API de Equipe")
public class EquipeController {

    private final EquipeService service;
    private final ModelMapper modelMapper = new ModelMapper();

    @GetMapping()
    @ApiOperation("Obter a lista de equipes")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Equipes encontradas"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity get() {
        List<Equipe> equipe = service.getEquipe();
        return ResponseEntity.ok(equipe.stream().map(EquipeDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de uma equipe pelo ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Equipe encontrada"),
            @ApiResponse(code = 404, message = "Equipe não encontrada"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Equipe> equipe = service.getEquipeById(id);
        if (!equipe.isPresent()) {
            return new ResponseEntity("Equipe não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(equipe.map(EquipeDTO::create));
    }

    @PostMapping()
    @ApiOperation("Cadastrar uma nova equipe")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Equipe criada com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao cadastrar equipe"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity post(@RequestBody EquipeDTO dto) {
        try {
            Equipe equipe = converter(dto);
            equipe = service.salvar(equipe);
            return new ResponseEntity(equipe, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar uma equipe existente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Equipe atualizada com sucesso"),
            @ApiResponse(code = 404, message = "Equipe não encontrada"),
            @ApiResponse(code = 400, message = "Erro ao atualizar equipe"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody EquipeDTO dto) {
        if (!service.getEquipeById(id).isPresent()) {
            return new ResponseEntity("equipe não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Equipe equipe = converter(dto);
            equipe.setId(id);
            service.salvar(equipe);
            return ResponseEntity.ok(equipe);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir uma equipe pelo ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Equipe excluída com sucesso"),
            @ApiResponse(code = 404, message = "Equipe não encontrada"),
            @ApiResponse(code = 400, message = "Erro ao excluir equipe"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Equipe> equipe = service.getEquipeById(id);
        if (!equipe.isPresent()) {
            return new ResponseEntity("Equipe não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(equipe.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Equipe converter(EquipeDTO dto) {
        return modelMapper.map(dto, Equipe.class);
    }
}
