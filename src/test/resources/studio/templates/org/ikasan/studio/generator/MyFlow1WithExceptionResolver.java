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
public class MyFlow1 {
@org.springframework.beans.factory.annotation.Value("${module.name}")
private String moduleName;

@javax.annotation.Resource
org.ikasan.builder.BuilderFactory builderFactory;

@javax.annotation.Resource
ComponentFactoryMyFlow1 componentFactory;

@org.springframework.context.annotation.Bean
public org.ikasan.spec.flow.Flow getMyFlow1()
{
org.ikasan.builder.ModuleBuilder moduleBuilder = builderFactory.getModuleBuilder(moduleName);
org.ikasan.builder.FlowBuilder flowBuilder = moduleBuilder.getFlowBuilder("MyFlow1");

org.ikasan.spec.flow.Flow myFlow1 = flowBuilder
.withDescription("MyFlowDescription")
.withExceptionResolver(builderFactory.getExceptionResolverBuilder()
.addExceptionToAction(org.ikasan.spec.component.endpoint.EndpointException.class, org.ikasan.builder.OnException.retryIndefinitely()),
.addExceptionToAction(org.ikasan.spec.component.splitting.SplitterException.class, org.ikasan.builder.OnException.exclude()),
.addExceptionToAction(org.ikasan.spec.component.filter.FilterException.class, org.ikasan.builder.OnException.retryIndefinitely()),
.addExceptionToAction(org.ikasan.spec.component.transformation.TransformationException.class, org.ikasan.builder.OnException.ignore()),
.addExceptionToAction(org.ikasan.spec.component.routing.RouterException.class, org.ikasan.builder.OnException.retry(200,10)),
.addExceptionToAction(java.util.concurrent.TimeoutException.class, org.ikasan.builder.OnException.retryIndefinitely("* * * * *",100)))
.build();
return myFlow1;
}
}