package com.github.coderondeck.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.annotation.Retention;

/**
 * Created by home on 7/16/16.
 */
public class AnnotationRetentionAdapter extends TypeAdapter<Retention> {
    @Override
    public void write(JsonWriter jsonWriter, Retention retention) throws IOException {
        if ( retention != null && retention.value() != null ){
            jsonWriter.beginObject();
            jsonWriter.name("value");
            jsonWriter.value(retention.value().toString());
            jsonWriter.endObject();
        } else{
            jsonWriter.nullValue();
        }
        
    }

    @Override
    public Retention read(JsonReader jsonReader) throws IOException {
        return null;
    }
}
