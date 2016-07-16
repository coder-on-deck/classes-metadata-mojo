package com.github.coderondeck;

import jodd.util.ClassLoaderUtil;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Using this thread to load depdendencies
 * http://stackoverflow.com/questions/2659048/add-maven-build-classpath-to-plugin-execution-classpath
 * http://aether.jcabi.com/
 * 
 * https://vzurczak.wordpress.com/2016/01/08/finding-dependencies-artifacts-in-your-maven-plug-in/
 * 
 */
@Mojo(name = "show", requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME, requiresProject = true)
public class ClassesMetadataMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(required = true, property = "pkgs")
    private List<String> pkgs;

    @Parameter(required = true, property = "scope")
    private List<String> scope;
    
    @Parameter(required=false, property="excluded")
    private List<String> excluded = new ArrayList<>();
    
    @Parameter(required = false, defaultValue = "true", property="pretty" )
    private boolean pretty;
    
    @Parameter(required = false, defaultValue = "false", property="verbose")
    private boolean verbose;
    
    @Parameter(required = false,  property = "outputFile")
    private File outputFile;

    

    public void execute() throws MojoExecutionException, MojoFailureException {
        Log logger = getLog();
        
        if ( verbose ) {
            logger.info("printing metadata about classes at : " + pkgs);
            logger.info("pretty = " + pretty + ", outputFile = " + outputFile + ", scope = " + scope);
        }
        
        if (project == null) {
            project = new MavenProject();
        }
        try {
            
            if ( verbose ) {
                logger.info("loading classpath matching scope [" + scope + "]");
            }

            List<String> urls = Arrays.stream(((URLClassLoader) (Thread.currentThread().getContextClassLoader())).getURLs()).map( url -> url.getFile().trim() ).collect(Collectors.toList());
            
            if (scope.contains("test")) {
                for (String str : (Collection<String>) project.getTestClasspathElements()) {
                    if ( !urls.contains(str) && FileUtils.fileExists(str)){
                        if ( verbose ) {
                            logger.info("adding to classpath = " + str);
                        }
                        ClassLoaderUtil.addFileToClassPath(str);
                    }else{
                        if ( verbose ) {
                            logger.info("already in classpath or does not exist : " + str);
                        }
                    }
                }
            } else {
                for (Artifact obj : (List<Artifact>) project.getDependencyArtifacts()) {
                    if ( verbose ) {
                        logger.info("adding to classpath = " + obj);
                    }
                    if (scope.contains(obj.getScope())) {
                        ClassLoaderUtil.addFileToClassPath(obj.getFile());
                    }
                }
            }
            if ( verbose ){
                logger.info("classpath is ready");
            }


            MetaDataScanner.Options options = new MetaDataScanner.Options();
            options.prettyPrint = pretty;
            options.outputFile = outputFile;
            options.excludePackages = excluded;
            options.includedPackages = pkgs;
            options.verbose = verbose;
            MetaDataScanner metaDataScanner = new MetaDataScanner(options);
            List<ClassMetadata> metadata = metaDataScanner.scan();
            metaDataScanner.printJson(metadata);

        } catch (Exception e) {
            logger.error(e);
        }
    }
    
  
}
