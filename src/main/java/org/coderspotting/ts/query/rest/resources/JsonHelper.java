/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.coderspotting.ts.query.rest.resources;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;

public class JsonHelper
{
    public static String hashMapListToJson(List<HashMap<String, String>> hashMapList) throws IOException
    {
        JsonFactory jsonFactory = new JsonFactory();
        StringWriter strWriter = new StringWriter();
        try (JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(strWriter))
        {
            jsonGenerator.useDefaultPrettyPrinter();

            jsonGenerator.writeStartArray();

            for (HashMap<String, String> hashMap : hashMapList)
            {
                jsonGenerator.writeStartObject();
                hashMapToJson(jsonGenerator, hashMap);
                jsonGenerator.writeEndObject();
            }

            jsonGenerator.writeEndArray();
        }

        return strWriter.toString();
    }

    public static String hashMapToJson(HashMap<String, String> hashMap) throws IOException
    {
        JsonFactory jsonFactory = new JsonFactory();
        StringWriter strWriter = new StringWriter();
        try (JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(strWriter))
        {
            jsonGenerator.useDefaultPrettyPrinter();

            hashMapToJson(jsonGenerator, hashMap);
        }

        return strWriter.toString();
    }

    public static void hashMapToJson(JsonGenerator jsonGenerator, HashMap<String, String> hashMap) throws IOException
    {
        for (String key : hashMap.keySet())
        {
            jsonGenerator.writeFieldName(key);
            jsonGenerator.writeString(hashMap.get(key));
        }
    }
}
