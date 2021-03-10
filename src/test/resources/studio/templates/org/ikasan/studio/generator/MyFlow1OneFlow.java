package org.ikasan;

/**
* The flow is the container for the components.
*
* Some details of the components are defined here, but the majority of the configuration for components is delegated
* to the ComponentFactory
*
* This file is auto-generated by Ikasan Studio, do no edit.
*/
@org.springframework.stereotype.Component
public class Myflow1 {
@org.springframework.beans.factory.annotation.Value("${module.name}")
private String moduleName;

@javax.annotation.Resource
org.ikasan.builder.BuilderFactory builderFactory;

@javax.annotation.Resource
ComponentFactory componentFactory;

@org.springframework.context.annotation.Bean
public org.ikasan.spec.flow.Flow getMyflow1()
{
org.ikasan.builder.ModuleBuilder moduleBuilder = builderFactory.getModuleBuilder(moduleName);
org.ikasan.builder.FlowBuilder flowBuilder = moduleBuilder.getFlowBuilder("MyFlow1");

org.ikasan.spec.flow.Flow myflow1 = flowBuilder
.withDescription("MyFlowDescription")
.build();
return myflow1;
}
}