package ${studioPackageTag};

/**
* The component factory defines the details of the components and their configuration.
*
* This file is auto-generated by Ikasan Studio, do no edit.
*/
@org.springframework.context.annotation.Configuration
public class ComponentFactory
{
@org.springframework.beans.factory.annotation.Value("${r"${module.name}"}")
private String moduleName;

@javax.annotation.Resource
org.ikasan.builder.BuilderFactory builderFactory;

<#compress>
<#list flow.flowComponentList![] as ikasanFlowComponent>
    <#if !ikasanFlowComponent.type.bespokeClass>
        <#list ikasanFlowComponent.getStandardProperties()![] as propName, propValue>
            <#if propValue.meta.propertyConfigFileLabel != "" && propValue.value??>
                <#if ikasanFlowComponent.getJavaPackageName() != "">
                    @org.springframework.beans.factory.annotation.Value("${r"${"}${flow.getJavaPackageName()}.${ikasanFlowComponent.getJavaPackageName()}.${propValue.meta.propertyConfigFileLabel}}")
                <#else>
                    @org.springframework.beans.factory.annotation.Value("${r"${"}${flow.getJavaPackageName()}.${propValue.meta.propertyConfigFileLabel}}")
                </#if>
                ${propValue.meta.dataType.getCanonicalName()}  ${propValue.meta.getPropertyConfigFileLabelAsVariable()};
            </#if>
        </#list>
        <#else>
            @javax.annotation.Resource
            ${module.properties.ApplicationPackageName.value}.${flow.getJavaPackageName()}.${ikasanFlowComponent.properties.BespokeClassName.value} ${ikasanFlowComponent.getJavaVariableName()};
    </#if>
</#list>
</#compress>


<#compress>
<#list flow.flowComponentList![] as ikasanFlowComponent>
    public ${ikasanFlowComponent.type.elementCategory.baseClass} get${ikasanFlowComponent.getJavaClassName()}() {
    <#if ikasanFlowComponent.type.bespokeClass>
        return ${ikasanFlowComponent.getJavaVariableName()};
    <#else>
        return builderFactory.getComponentBuilder().${ikasanFlowComponent.type.associatedMethodName}()
        <#list ikasanFlowComponent.getStandardProperties() as propName, propValue>
            <#if propValue.value??>
                <#if propValue.meta.propertyConfigFileLabel != "">
                    .set${propName}(${propValue.meta.getPropertyConfigFileLabelAsVariable()})
                <#else>
                    .set${propName}(${propValue.getValueString()})
                </#if>
            </#if>
        </#list>
        .build();
    </#if>
    }
</#list>
</#compress>
}