package org.ikasan;

/**
* The component factory defines the details of the components and their configuration.
*
* This file is auto-generated by Ikasan Studio, do no edit.
*/
@org.springframework.context.annotation.Configuration
public class ComponentFactoryMyFlow1
{
@org.springframework.beans.factory.annotation.Value("${module.name}")
private String moduleName;

@javax.annotation.Resource
org.ikasan.builder.BuilderFactory builderFactory;

@javax.annotation.Resource
co.uk.test.myflow1.AToBConvertMyFlow1SetByIDEEndingInDebugDebug setByIDEEndingInDebug;


public org.ikasan.spec.component.transformation.Converter getSetByIDEEndingInDebug() {
return setByIDEEndingInDebug;
}}