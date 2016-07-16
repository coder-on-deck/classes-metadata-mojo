package com.github.coderondeck.adapters;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by home on 7/16/16.
 */
public class AnnotationTargetAdapter extends TypeAdapter<Target> {

    @Override
    public void write(JsonWriter jsonWriter, Target target) throws IOException {
        if ( target != null && target.value() != null ) {
            jsonWriter.beginObject();
            jsonWriter.name("value");
            jsonWriter.beginArray();
            for (ElementType type : target.value()) {
                jsonWriter.value(type.toString());
            }
            jsonWriter.endArray();
            jsonWriter.endObject();
        }else{
            jsonWriter.nullValue();
        }
    }

    @Override
    public Target read(JsonReader jsonReader) throws IOException {
        return null;
    }
}
