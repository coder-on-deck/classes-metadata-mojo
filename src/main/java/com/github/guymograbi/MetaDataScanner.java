package com.github.guymograbi;

import com.github.guymograbi.adapters.AnnotationAdapter;
import com.github.guymograbi.adapters.AnnotationRetentionAdapter;
import com.github.guymograbi.adapters.AnnotationTargetAdapter;
import com.google.common.reflect.ClassPath;
import com.google.gson.GsonBuilder;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by home on 7/16/16.
 */
public class MetaDataScanner {

    public Options options = new Options();
    public Logger logger = Logger.getLogger(MetaDataScanner.class.getCanonicalName());

    public MetaDataScanner(Options options) {
        this.options = options;
    }

    public List<ClassMetadata> scan() {
        final List<ClassMetadata> metadata = new ArrayList<>();

        Collection<ClassPath.ClassInfo> allClasses = new ArrayList<>();
        try {

            allClasses = ClassPath.from(getClass().getClassLoader()).getTopLevelClasses();
        } catch (Exception e) {

        }


        for (ClassPath.ClassInfo info : allClasses) {
            if ( options.verbose) {
                logger.info("loading " + info.getName());
            }
            if (!startsWithAny(info.getName(), options.excludePackages) && startsWithAny(info.getName(), options.includedPackages)) {
                metadata.add(new ClassMetadata(info.load()));
            }
        }

        if ( options.verbose) {
            logger.info("finished");
        }
        return metadata;
    }

    public void printJson(List<ClassMetadata> metadata) {
        GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls()
                .registerTypeAdapter(Annotation.class, new AnnotationAdapter())
                .registerTypeAdapter(Target.class, new AnnotationTargetAdapter())
                .registerTypeAdapter(Retention.class, new AnnotationRetentionAdapter());

        if (options.prettyPrint) {
            gsonBuilder.setPrettyPrinting();
        }


        String json = gsonBuilder.create().toJson(metadata);

        if (options.outputFile != null) {
            try {
                if ( options.verbose){
                    logger.info("writing file to : " + options.outputFile.getAbsolutePath());
                }
                FileUtils.fileWrite(options.outputFile.getAbsolutePath(), json);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println(json);
        }
    }


    private boolean startsWithAny(String className, List<String> list) {
        for (String excludePackage : list) {
            if (className.startsWith(excludePackage)) {
                return true;
            }
        }
        return false;
    }


    static public class Options {
        public boolean prettyPrint = true;
        public boolean verbose = true;
        public List<String> excludePackages = new ArrayList<>();
        public List<String> includedPackages = new ArrayList<>();
        public File outputFile = null;
    }
}
