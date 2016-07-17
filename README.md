classes-metadata-mojo
========================


A maven plugin to print metadata about classes

# Installation

## From command line

```
mvn org.apache.maven.plugins:maven-dependency-plugin:2.1:get     -DrepoUrl=https://jitpack.io/     -Dartifact=com.github.coder-on-deck:classes-metadata-mojo:0.0.0
```

## Add to your pom.xml


Use jitpack to install directly form github

```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
	
```
	
```
<dependency>
    <groupId>com.github.coder-on-deck</groupId>
    <artifactId>classes-metadata-mojo</artifactId>
    <version>0.0.0</version>
</dependency>
```

# Examples 

```
com.github.coder-on-deck:classes-metadata-mojo:show -Dpkgs=com.github -Dscope=test -Dexcluded=java,org,jodd -DoutputFile=out.json -Dpretty=false
```