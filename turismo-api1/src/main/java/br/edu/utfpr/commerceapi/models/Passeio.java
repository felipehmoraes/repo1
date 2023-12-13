package br.edu.utfpr.commerceapi.models;

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
@Table(name = "tb_passeio")
public class Passeio extends BaseEntity {

    @Column(name = "destino", length = 150, nullable = false)
    private String destino;

    @Column(name = "itinerario", length = 150, nullable = false)
    private String itinerario;
    
    @Column(name = "preco", nullable = false)
    private Double preco;
}
