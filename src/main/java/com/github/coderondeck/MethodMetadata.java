package com.github.coderondeck;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by home on 7/16/16.
 */
public class MethodMetadata {
    
    public String name;
    public Annotation[] annotations;
    
    public MethodMetadata(Method method){
        name = method.getName();
        annotations = method.getAnnotations();
    }
    
}
