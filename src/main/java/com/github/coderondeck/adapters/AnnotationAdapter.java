package com.github.coderondeck.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

/**
 * Created by home on 7/16/16.
 */
public class AnnotationAdapter extends TypeAdapter<Annotation> {
    @Override
    public void write(JsonWriter jsonWriter, Annotation annotation) throws IOException {
//        System.out.println(" annotation adapter : " + annotation  + " " + annotation.annotationType().getAnnotations().length);
        Class<? extends Annotation> aClass = annotation.annotationType();
        jsonWriter.beginObject();
        
        
        jsonWriter.name("name");
        jsonWriter.value(aClass.getCanonicalName());
        
        jsonWriter.name("_metadata");
        jsonWriter.beginObject();
        if ( aClass.isAnnotationPresent(Target.class)){
            jsonWriter.name("target");
            new AnnotationTargetAdapter().write(jsonWriter, aClass.getAnnotation(Target.class));    
        }
        
        if (aClass.isAnnotationPresent(Retention.class)){
            jsonWriter.name("retention");
            new AnnotationRetentionAdapter().write(jsonWriter, aClass.getAnnotation(Retention.class));
        }
        jsonWriter.endObject();
        
        jsonWriter.name("values");
        jsonWriter.beginObject();
        // values
        for (Method method : aClass.getDeclaredMethods()) {
            try{
                Object invoke = method.invoke(annotation);
                jsonWriter.name(method.getName());
                if ( Boolean.class.isAssignableFrom(invoke.getClass())){
                    jsonWriter.value((Boolean)invoke);
                } else if (Number.class.isAssignableFrom(invoke.getClass())) {
                    jsonWriter.value((Number)invoke);
                } else {
                    jsonWriter.value(invoke.toString());
                }
            }catch(Exception e){
                throw new RuntimeException("unable to get annotation values", e);
            }
        }
        jsonWriter.endObject();
        
        jsonWriter.endObject();
    }

    @Override
    public Annotation read(JsonReader jsonReader) throws IOException {
        return null;
    }
}
