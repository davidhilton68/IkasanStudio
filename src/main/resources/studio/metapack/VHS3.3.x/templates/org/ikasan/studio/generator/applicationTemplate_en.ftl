package ${studioPackageTag};

/**
* This is the Spring boot start class.
* This file is auto-generated by Ikasan Studio, do no edit.
*/
@org.springframework.boot.autoconfigure.SpringBootApplication
@org.springframework.context.annotation.ComponentScan({"org.ikasan.studio.boot", "${module.getApplicationPackageName()}"})
public class ${className} {
public static void main(String[] args) {
org.springframework.boot.SpringApplication.run(Application.class, args);
}}