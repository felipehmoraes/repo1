package br.edu.utfpr.commerceapi.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Entity
@Table(name = "tb_reserva")
public class Reserva extends BaseEntity {

    @Column(name = "quantidadePessoas", nullable = false)
    private Integer quantidadePessoas;

    @Column(name = "dataReserva")
    private LocalDateTime dataReserva;

  

}
