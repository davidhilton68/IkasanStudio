<?xml version="1.0" encoding="UTF-8"?>
<project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"
         xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <modelVersion>4.0.0</modelVersion>
    <description>This pom is used to start and stop the H2 database to assist development debugging</description>
    <artifactId>start-h2</artifactId>
    <groupId>com.mizuno.esb.mhi</groupId>
    <version>1.0.0</version>

    <dependencies>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>1.4.200</version>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>exec-maven-plugin</artifactId>
                <version>3.0.0</version>
                <executions>
                    <execution>
                        <id>StopH2</id>
                        <goals>
                            <goal>java</goal>
                        </goals>
                        <configuration>
                            <mainClass>org.h2.tools.Server</mainClass>
                            <arguments>
                                <argument>-tcpShutdown</argument>
                                <argument>tcp://127.0.0.1:${r"${h2DbPortNumber}"}</argument>
                                <argument>-tcpPassword</argument>
                                <argument>sa</argument>
                            </arguments>
                        </configuration>
                    </execution>
                    <execution>
                        <id>StartH2</id>
                        <goals>
                            <goal>java</goal>
                        </goals>
                        <configuration>
                            <mainClass>org.h2.tools.Server</mainClass>
                            <arguments>
                                <argument>-ifNotExists</argument>
                                <argument>-tcp</argument>
                                <argument>-tcpAllowOthers</argument>
                                <argument>-tcpPort</argument>
                                <argument>${r"${h2DbPortNumber}"}</argument>
                                <argument>-tcpPassword</argument>
                                <argument>sa</argument>
                                <argument>-browser</argument>
                                <argument>-web</argument>
                                <argument>-webAllowOthers</argument>
                                <argument>-webPort</argument>
                                <argument>${r"${h2WebPortNumber}"}</argument>
                            </arguments>
                        </configuration>
                    </execution>
                    <execution>
                        <id>H2Query</id>
                        <goals>
                            <goal>java</goal>
                        </goals>
                        <configuration>
                            <mainClass>org.h2.tools.RunScript</mainClass>
                            <arguments>
                                <argument>-url</argument>
                                <argument>jdbc:h2:tcp://localhost:12451/~/demo1-db/esb</argument>
                                <argument>-user</argument>
                                <argument>sa</argument>
                                <argument>-password</argument>
                                <argument>sa</argument>
                                <argument>-script</argument>
                                <argument>h2query.sql</argument>
                                <argument>-showResults</argument>
                            </arguments>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
