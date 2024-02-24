package ${studioPackageTag};

/**
* The flow is the container for the components.
*
* Some details of the components are defined here, but the majority of the configuration for components is delegated
* to the ComponentFactory
*
* This file is auto-generated by Ikasan Studio, do no edit.
*/
@org.springframework.stereotype.Component
public class ${flow.getJavaClassName()} {
@org.springframework.beans.factory.annotation.Value("${r"${module.name}"}")
private String moduleName;

@javax.annotation.Resource
org.ikasan.builder.BuilderFactory builderFactory;

@javax.annotation.Resource
ComponentFactory${flow.getJavaClassName()} componentFactory;

@org.springframework.context.annotation.Bean
public org.ikasan.spec.flow.Flow get${flow.getJavaClassName()}()
{
org.ikasan.builder.ModuleBuilder moduleBuilder = builderFactory.getModuleBuilder(moduleName);
org.ikasan.builder.FlowBuilder flowBuilder = moduleBuilder.getFlowBuilder("${flow.name}");

<#compress>
org.ikasan.spec.flow.Flow ${flow.getJavaVariableName()} = flowBuilder
<#if flow.description?has_content >
.withDescription("${flow.description}")
</#if>

<#if flow.hasExceptionResolver()>
.withExceptionResolver(builderFactory.getExceptionResolverBuilder()
<#list flow.exceptionResolver.ikasanExceptionResolutionList![] as exceptionResolution>
.addExceptionToAction(${exceptionResolution.theException}, org.ikasan.builder.OnException.${exceptionResolution.theAction}(<#list exceptionResolution.params![] as param><#if param.value??>${param.templateRepresentationOfValue!xx}<#sep>,</#if></#list>))<#sep>
</#list>)
</#if>

<#list flow.flowComponentList![] as ikasanFlowComponent>
    .${ikasanFlowComponent.type.elementCategory.associatedMethodName}("${ikasanFlowComponent.componentName}",
    componentFactory.get${ikasanFlowComponent.getJavaClassName()}())
</#list>
</#compress>

.build();
return ${flow.getJavaVariableName()};
}
}