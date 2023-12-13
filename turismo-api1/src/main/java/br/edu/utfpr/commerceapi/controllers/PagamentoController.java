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

import br.edu.utfpr.commerceapi.dto.PagamentoDTO;
import br.edu.utfpr.commerceapi.models.Pagamento;
import br.edu.utfpr.commerceapi.models.Pessoa;
import br.edu.utfpr.commerceapi.repositories.PagamentoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;


@RestController

@RequestMapping("/pagamento")

public class PagamentoController {
    @Autowired
    PagamentoRepository PagamentoRepository;

    @Operation(summary = "Obeter pagamento Paginas ", 
      description = "Obetem todos os pagamentos porem em paginas", tags = {
            "Pagamento" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Pessoa.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/pages")
    public ResponseEntity<Page<Pagamento>> getAllPage(
            @PageableDefault(page = 0, size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok()
                .body(PagamentoRepository.findAll(pageable));
    }

    /**
     * Obter todas as Pagamentos
     */
      @Operation(summary = "Obter todos os pagamentos", 
      description = "Lista todos os pagamentos", tags = {
            "Pagamento" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Pessoa.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    })
    @SecurityRequirement(name = "Authorization")
    @GetMapping(value = { "", "/" })
    public List<Pagamento> getAll() {
        return PagamentoRepository.findAll();
    }

    /**
     * Obter 1 Pagamento pelo ID
     */
    @Operation(summary = "Obeter Pagamento", description = "Obetem um pagamento na api atraves da ID", tags = {
            "Pagamento" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Pessoa.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    })
    @SecurityRequirement(name = "Authorization")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        Optional<Pagamento> PagamentoOpt = PagamentoRepository
                .findById(UUID.fromString(id));

        return PagamentoOpt.isPresent()
                ? ResponseEntity.ok(PagamentoOpt.get())
                : ResponseEntity.notFound().build();
    }

    /**
     * Criar uma Pagamento na API
     */
    @Operation(summary = "Gravar pagamento ", description = "Grava um pagamento na api", tags = { "Pagamento" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Pessoa.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    })
    @PostMapping("")
    public ResponseEntity<Object> create(@Valid @RequestBody PagamentoDTO PagamentoDTO) {
        var pes = new Pagamento(); // Pessoa para persistir no DB
        BeanUtils.copyProperties(PagamentoDTO, pes);

        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(PagamentoRepository.save(pes));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Falha ao criar pessoa");
        }
    }

    /**
     * Atualizar 1 Pagamento pelo ID
     */
    @Operation(summary = "Alterar pagamento ", description = "Altera dados de  um pagamento na api utilizando uma ID", tags = {
            "Pagamento" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Pessoa.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable String id,
            @RequestBody PagamentoDTO PagamentoDTO) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Formato de UUID inválido");
        }

        // Buscando a Pagamento no banco de dados
        var Pagamento = PagamentoRepository.findById(uuid);

        // Verifica se ela existe
        if (Pagamento.isEmpty())
            return ResponseEntity.notFound().build();

        var PagamentoToUpdate = Pagamento.get();
        BeanUtils.copyProperties(PagamentoDTO, PagamentoToUpdate);
        PagamentoToUpdate.setUpdatedAt(LocalDateTime.now());

        try {
            return ResponseEntity.ok()
                    .body(PagamentoRepository.save(PagamentoToUpdate));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Falha ao atualizar pessoa");
        }
    }

    /**
     * Deletar uma Pagamento pelo ID
     */
    @Operation(summary = "Apagar pagamento ", description = "Apaga  pagamento na api utilizando uma ID", tags = {
            "Pagamento" })
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

        var Pagamento = PagamentoRepository.findById(uuid);

        if (Pagamento.isEmpty())
            return ResponseEntity.notFound().build();

        try {
            PagamentoRepository.delete(Pagamento.get());
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
