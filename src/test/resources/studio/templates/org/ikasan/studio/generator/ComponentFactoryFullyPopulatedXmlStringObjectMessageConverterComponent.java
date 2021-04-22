package org.ikasan;

/**
* The component factory defines the details of the components and their configuration.
*
* This file is auto-generated by Ikasan Studio, do no edit.
*/
@org.springframework.context.annotation.Configuration
public class ComponentFactory
{
@org.springframework.beans.factory.annotation.Value("${module.name}")
private String moduleName;

@javax.annotation.Resource
org.ikasan.builder.BuilderFactory builderFactory;


@javax.annotation.Resource
javax.xml.bind.ValidationEventHandler myValidationEventHandler;
@javax.annotation.Resource
org.ikasan.component.endpoint.filesystem.messageprovider.FileConsumerConfiguration myConfigurationClass;

public org.ikasan.spec.component.transformation.Converter get() {
return builderFactory.getComponentBuilder().xmlStringToObjectConverter()
.setClassesToBeBound({'String.class','String.class'})
.setValidationEventHandler(myValidationEventHandler)
.setConfiguredResourceId("myUniqueConfiguredResourceIdName")
.setConfiguration(myConfigurationClass)
.setClassToBeBound(String.class)
.build();
}}