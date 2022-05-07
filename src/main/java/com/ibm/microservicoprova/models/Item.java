package com.ibm.microservicoprova.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    private String produto;
    private String variedade;
    private String pais;
    private String categoria;
    private String safra;
    private double preco;


}
