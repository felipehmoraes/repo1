package br.edu.utfpr.commerceapi.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReservaDTO {

    private Integer quantidadePessoas;

    @PastOrPresent
    private LocalDateTime dataReserva;


}
