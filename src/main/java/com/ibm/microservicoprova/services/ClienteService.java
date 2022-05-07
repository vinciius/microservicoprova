package com.ibm.microservicoprova.services;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ibm.microservicoprova.models.Carrinho;
import com.ibm.microservicoprova.models.Cliente;
import com.ibm.microservicoprova.utils.DeserializerHelper;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteService {

    @Autowired
    DeserializerHelper deserializerHelper;

    private static final String url = "http://www.mocky.io/v2/598b16291100004705515ec5";

    public List<Cliente> deserialize() throws IOException {
        String json = deserializerHelper.returnJson(url);
        Gson gson = new Gson();
        Type objectListType = new TypeToken<ArrayList<Cliente>>() {}.getType();
        List<Cliente> clienteList = gson.fromJson(json, objectListType);
        return clienteList;
    }
}
