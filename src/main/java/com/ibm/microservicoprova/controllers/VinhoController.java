package com.ibm.microservicoprova.controllers;

import com.ibm.microservicoprova.models.Carrinho;
import com.ibm.microservicoprova.models.Cliente;
import com.ibm.microservicoprova.services.CarrinhoService;
import com.ibm.microservicoprova.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
public class VinhoController {

    @Autowired
    ClienteService clienteService;

    @Autowired
    CarrinhoService carrinhoService;

    @GetMapping("/compras")
    public List<Carrinho> crescentePorValor() throws IOException {
        return carrinhoService.ordenaCrescente(carrinhoService.deserialize());
    }

    @GetMapping("/maior-compra/{ano}")
    public Carrinho maiorCompraPorAno(@PathVariable int ano) throws IOException {
        return carrinhoService.getMaiorCompra(carrinhoService.deserialize(), ano);
    }

    @GetMapping("/clientes-fieis")
    public Map<String, Double> clientesFieis() throws IOException {
        return carrinhoService.clientesFieis();
    }

    @GetMapping("/recomendacao/{cliente}/tipo")
    public String recomendacao(@PathVariable String cliente) throws IOException {
        return carrinhoService.recomendacaoCliente(cliente);
    }
}
