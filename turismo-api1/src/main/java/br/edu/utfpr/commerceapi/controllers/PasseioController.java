package br.edu.utfpr.commerceapi.controllers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.commerceapi.dto.PasseioDTO;
import br.edu.utfpr.commerceapi.models.Passeio;
import br.edu.utfpr.commerceapi.models.Pessoa;
import br.edu.utfpr.commerceapi.repositories.PasseioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;


@RestController

@RequestMapping("/passeio")

public class PasseioController {
    @Autowired
    PasseioRepository PasseioRepository;

    @Operation(summary = "Obeter passeio Paginas ", 
      description = "Obetem todos os passeios porem em paginas", tags = {
            "Passeio" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Pessoa.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/pages")
    public ResponseEntity<Page<Passeio>> getAllPage(
            @PageableDefault(page = 0, size = 10, sort = "destino", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok()
                .body(PasseioRepository.findAll(pageable));
    }

    /**
     * Obter todas as Passeios
     */
      @Operation(summary = "Obter todos os passeios", 
      description = "Lista todos os passeios", tags = {
            "Passeio" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Pessoa.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    })
    @SecurityRequirement(name = "Authorization")
    @GetMapping(value = { "", "/" })
    public List<Passeio> getAll() {
        return PasseioRepository.findAll();
    }

    /**
     * Obter 1 Passeio pelo ID
     */
    @Operation(summary = "Obeter Passeio", description = "Obetem um passeio na api atraves da ID", tags = {
            "Passeio" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Pessoa.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    })
    @SecurityRequirement(name = "Authorization")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        Optional<Passeio> PasseioOpt = PasseioRepository
                .findById(UUID.fromString(id));

        return PasseioOpt.isPresent()
                ? ResponseEntity.ok(PasseioOpt.get())
                : ResponseEntity.notFound().build();
    }

    /**
     * Criar uma Passeio na API
     */
    @Operation(summary = "Gravar passeio ", description = "Grava um passeio na api", tags = { "Passeio" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Pessoa.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    })
    @PostMapping("")
    public ResponseEntity<Object> create(@Valid @RequestBody PasseioDTO PasseioDTO) {
        var pes = new Passeio(); // Pessoa para persistir no DB
        BeanUtils.copyProperties(PasseioDTO, pes);

        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(PasseioRepository.save(pes));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Falha ao criar pessoa");
        }
    }

    /**
     * Atualizar 1 Passeio pelo ID
     */
    @Operation(summary = "Alterar passeio ", description = "Altera dados de  um passeio na api utilizando uma ID", tags = {
            "Passeio" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Pessoa.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable String id,
            @RequestBody PasseioDTO PasseioDTO) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Formato de UUID inválido");
        }

        // Buscando a Passeio no banco de dados
        var Passeio = PasseioRepository.findById(uuid);

        // Verifica se ela existe
        if (Passeio.isEmpty())
            return ResponseEntity.notFound().build();

        var PasseioToUpdate = Passeio.get();
        BeanUtils.copyProperties(PasseioDTO, PasseioToUpdate);
        PasseioToUpdate.setUpdatedAt(LocalDateTime.now());

        try {
            return ResponseEntity.ok()
                    .body(PasseioRepository.save(PasseioToUpdate));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Falha ao atualizar pessoa");
        }
    }

    /**
     * Deletar uma Passeio pelo ID
     */
    @Operation(summary = "Apagar passeio ", description = "Apaga  passeio na api utilizando uma ID", tags = {
            "Passeio" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Pessoa.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Formato de UUID inválido");
        }

        var Passeio = PasseioRepository.findById(uuid);

        if (Passeio.isEmpty())
            return ResponseEntity.notFound().build();

        try {
            PasseioRepository.delete(Passeio.get());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }
}
