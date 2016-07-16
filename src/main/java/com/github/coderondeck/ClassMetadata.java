package com.github.coderondeck;


import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by home on 7/16/16.
 */
public class ClassMetadata {
    public List<MethodMetadata> methods;
    public Annotation[] annotations;
    public String name;
    public ClassMetadata superClass = null;

    public ClassMetadata(Class clzz){
        name = clzz.getCanonicalName();
        annotations = clzz.getAnnotations();
        methods = Arrays
                .stream(clzz.getMethods()).map(m -> new MethodMetadata(m)).collect(Collectors.toList());
        if ( clzz.getSuperclass() != null ){
            superClass = new ClassMetadata(clzz.getSuperclass());
        }
    }
}
