<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>tester</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>tester</name>
    <url>http://maven.apache.org</url>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.target>1.8</maven.compiler.target>
        <maven.compiler.source>1.8</maven.compiler.source>
    </properties>
    <build>
        <plugins>
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>exec-maven-plugin</artifactId>
                <version>3.1.1</version>
                <executions>
                    <execution>
                        <phase>generate-sources</phase>
                        <goals>
                            <goal>java</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <includePluginDependencies>true</includePluginDependencies>
                    <mainClass>fmpp.tools.CommandLine</mainClass>
                    <sourceRoot>${basedir}/target/generated-sources/</sourceRoot>
                    <arguments>
                        <argument>-C</argument>
                        <argument>${basedir}/src/main/templates/config.fmpp</argument>
                        <argument>-S</argument>
                        <argument>${basedir}/src/main/templates/com/</argument>
                        <argument>-O</argument>
                        <argument>${basedir}/target/generated-sources/com/</argument>
                    </arguments>
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>net.sourceforge.fmpp</groupId>
                        <artifactId>fmpp</artifactId>
                        <version>0.9.16</version>
                    </dependency>
                    <dependency>
                        <groupId>org.freemarker</groupId>
                        <artifactId>freemarker</artifactId>
                        <version>2.3.32</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>

    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>3.8.1</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>net.sourceforge.fmpp</groupId>
            <artifactId>fmpp</artifactId>
            <version>0.9.16</version>
        </dependency>
    </dependencies>
</project>
