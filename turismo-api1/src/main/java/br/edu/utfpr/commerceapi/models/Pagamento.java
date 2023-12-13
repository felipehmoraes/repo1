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
@Table(name = "tb_pagamento")
public class Pagamento extends BaseEntity {

    @Column(name = "valor", nullable = false)
    private Integer valor;

    
  

}
