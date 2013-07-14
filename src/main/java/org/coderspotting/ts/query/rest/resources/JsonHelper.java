package org.coderspotting.ts.query.rest.resources;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.json.spi.JsonProvider;
import org.codehaus.jackson.map.SerializationConfig;

public class JsonHelper
{
    public static JsonArrayBuilder hashMapListToJson(List<HashMap<String, String>> hashMapList)
    {
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();


        for (HashMap<String, String> hashMap : hashMapList)
        {
            arrBuilder.add(hashMapToJson(hashMap));
        }

        return arrBuilder;
    }

    public static JsonObjectBuilder hashMapToJson(HashMap<String, String> hashMap)
    {
        JsonObjectBuilder objBuilder = Json.createObjectBuilder();

        for (String key : hashMap.keySet())
        {
            objBuilder.add(key, hashMap.get(key));
        }

        return objBuilder;
    }

    public static String buildJsonData(JsonObjectBuilder objBuilder)
    {
        StringWriter stWriter = new StringWriter();

        try (JsonWriter jsonWriter = createJsonWriter(stWriter))
        {
            jsonWriter.writeObject(objBuilder.build());
        }

        return stWriter.toString();
    }

    public static String buildJsonData(JsonArrayBuilder arrBuilder)
    {
        StringWriter stWriter = new StringWriter();

        try (JsonWriter jsonWriter = createJsonWriter(stWriter))
        {
            jsonWriter.writeArray(arrBuilder.build());
        }

        return stWriter.toString();
    }
    
    private static JsonWriter createJsonWriter(Writer writer)
    {
        JsonProvider provider = JsonProvider.provider();

        System.out.println("JSON Provider:" + provider.toString());
        
        HashMap<String,Boolean> config = new HashMap<>();
        
        config.put(SerializationConfig.Feature.INDENT_OUTPUT.name(), true);
        
        return Json.createWriterFactory(config).createWriter(writer);
    }
}
