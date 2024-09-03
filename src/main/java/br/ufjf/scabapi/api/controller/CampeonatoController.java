package br.ufjf.scabapi.api.controller;

import br.ufjf.scabapi.api.dto.CampeonatoDTO;
import br.ufjf.scabapi.exception.RegraNegocioException;
import br.ufjf.scabapi.model.entity.Campeonato;
import br.ufjf.scabapi.service.CampeonatoService;
import io.swagger.annotations.Api;
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
@RequestMapping("/api/v1/campeonatos")
@RequiredArgsConstructor
@Api("API de Campeonato")
public class CampeonatoController {

    private final CampeonatoService service;
    private final ModelMapper modelMapper = new ModelMapper();

    @GetMapping()
    @ApiOperation("Obter a lista de campeonatos")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Campeonatos encontradas"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity get() {
        List<Campeonato> campeonato = service.getCampeonato();
        return ResponseEntity.ok(campeonato.stream().map(CampeonatoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um campeonato pelo ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Campeonato encontrado"),
            @ApiResponse(code = 404, message = "Campeonato não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Campeonato> campeonato = service.getCampeonatoById(id);
        if (!campeonato.isPresent()) {
            return new ResponseEntity("Campeonato não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(campeonato.map(CampeonatoDTO::create));
    }

    @PostMapping()
    @ApiOperation("Cadastrar um novo campeonato")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Campeonato criado com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao cadastrar campeonato"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity post(@RequestBody CampeonatoDTO dto) {
        try {
            Campeonato campeonato = converter(dto);
            campeonato = service.salvar(campeonato);
            return new ResponseEntity(campeonato, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar um campeonato existente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Campeonato atualizado com sucesso"),
            @ApiResponse(code = 404, message = "Campeonato não encontrado"),
            @ApiResponse(code = 400, message = "Erro ao atualizar campeonato"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody CampeonatoDTO dto) {
        if (!service.getCampeonatoById(id).isPresent()) {
            return new ResponseEntity("Campeonato não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Campeonato campeonato = converter(dto);
            campeonato.setId(id);
            service.salvar(campeonato);
            return ResponseEntity.ok(campeonato);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir um campeonato pelo ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Campeonato excluído com sucesso"),
            @ApiResponse(code = 404, message = "Campeonato não encontrado"),
            @ApiResponse(code = 400, message = "Erro ao excluir campeonato"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Campeonato> campeonato = service.getCampeonatoById(id);
        if (!campeonato.isPresent()) {
            return new ResponseEntity("Campeonato não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(campeonato.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Campeonato converter(CampeonatoDTO dto) {
        return modelMapper.map(dto, Campeonato.class);
    }
}
