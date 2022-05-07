package com.ibm.microservicoprova.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Carrinho {

    private String codigo;
    private String data;
    private String cliente;
    private List<Item> itens;
    private double valorTotal;
}
