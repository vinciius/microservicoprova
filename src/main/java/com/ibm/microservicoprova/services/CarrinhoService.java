package com.ibm.microservicoprova.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ibm.microservicoprova.models.Carrinho;
import com.ibm.microservicoprova.models.Cliente;
import com.ibm.microservicoprova.models.Item;
import com.ibm.microservicoprova.utils.DeserializerHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarrinhoService {

    @Autowired
    DeserializerHelper deserializerHelper;
    @Autowired
    ClienteService clienteService;


    private static final String url = "http://www.mocky.io/v2/598b16861100004905515ec7";

    public List<Carrinho> deserialize() throws IOException {
        String json = deserializerHelper.returnJson(url);
        Gson gson = new Gson();
        Type objectListType = new TypeToken<ArrayList<Carrinho>>() {}.getType();
        List<Carrinho> carrinhoList = gson.fromJson(json, objectListType);
        return carrinhoList;
    }

    public List<Carrinho> ordenaCrescente(List<Carrinho> carrinhoList) {
        carrinhoList.sort(Comparator.comparing(c -> c.getValorTotal()));
        return carrinhoList;
    }

    public Carrinho getMaiorCompra(List<Carrinho> carrinhoList, Integer ano) {
        List<Carrinho> carrinhoListFiltered = carrinhoList.stream()
                .filter(c -> c.getData().substring(6).equals(ano.toString()))
                .collect(Collectors.toList());

        carrinhoListFiltered.sort(Comparator.comparing(c -> c.getValorTotal()));

        Carrinho carrinho = carrinhoListFiltered.get(carrinhoListFiltered.size() - 1);
        return carrinho;
    }

    public Map<String, Double> clientesFieis() throws IOException {

        List<Cliente> clienteList = clienteService.deserialize();
        List<Carrinho> carrinhoList = deserialize();
        Map<String, Double> ranking = new HashMap<>();

        for (Cliente cliente : clienteList) {
            double valorTotal = 0;
            cliente.setCpf(cliente.getCpf().replace("-", "."));
            String clienteCpf = cliente.getCpf().substring(cliente.getCpf().length() - 2);

            for (Carrinho carrinho: carrinhoList) {
                if (carrinho.getCliente().substring(carrinho.getCliente().length() - 2).equals(clienteCpf)) {
                    valorTotal += carrinho.getValorTotal();
                }
            }
            ranking.put(cliente.getNome(), valorTotal);
        }



        Map<String, Double> filteredRank = ranking.entrySet().stream()
                .sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        return filteredRank;
    }

    public String recomendacaoCliente (String cliente) throws IOException {

        List<Carrinho> carrinhoList = deserialize();
        Map<String, Integer> topVinhos = new HashMap<>();
        String cpf = cliente.substring(cliente.length() - 2);
        Set<String> tiposVinho =  new HashSet<>();

        for (Carrinho carrinho : carrinhoList) {
            for (Item item : carrinho.getItens()) {
                tiposVinho.add(item.getVariedade());
            }
        }

        for(String vinho : tiposVinho) {
            topVinhos.put(vinho, 0);
        }

        for (Carrinho carrinho : carrinhoList) {
            if (carrinho.getCliente().substring(carrinho.getCliente().length() - 2).equals(cpf)) {
                for(Item item : carrinho.getItens()) {
                    for(Map.Entry<String, Integer> entry : topVinhos.entrySet()) {
                        if(item.getVariedade().equals(entry.getKey())) {
                            entry.setValue(entry.getValue() + 1);
                        }
                    }
                }
            }
        }

        Map<String, Integer> filteredRank = topVinhos.entrySet().stream()
                .sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        String firstKey = filteredRank.keySet().stream().findFirst().get();

        for(Carrinho carrinho : carrinhoList) {
            for(Item item : carrinho.getItens()) {
                if (firstKey.equals(item.getVariedade())) {
                    return item.getProduto();
                }
            }
        }
        return "Não foi possível recomendar um vinho.";
    }
}
