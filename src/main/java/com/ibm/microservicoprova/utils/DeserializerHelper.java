package com.ibm.microservicoprova.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ibm.microservicoprova.models.Carrinho;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class DeserializerHelper {

    public String returnJson(String url) throws IOException {
        return IOUtils.toString(new URL(url));
    }

    public List<Object> deserialize(Object object, String url) throws IOException {
        String json = returnJson(url);
        Gson gson = new Gson();
        Type objectListType = new TypeToken<ArrayList<Object>>() {}.getType();
        List<Object> objectList = gson.fromJson(json, objectListType);
        return objectList;
    }
}
