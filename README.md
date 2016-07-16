classes-metadata-mojo
========================


A maven plugin to print metadata about classes

# Installation

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
    <groupId>com.github.guymograbi</groupId>
    <artifactId>classes-metadata-mojo</artifactId>
    <version>0.0.0</version>
</dependency>
```

# Examples 

```
com.github.guymograbi:classes-metadata-mojo:show -Dpkgs=com.github -Dscope=test -Dexcluded=java,org,jodd -DoutputFile=out.json -Dpretty=false
```