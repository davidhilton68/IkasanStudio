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
<#list flow.flowComponentList![] as ikasanComponent>
    <#if !ikasanComponent.type.bespokeClass>
        <#list ikasanComponent.getStandardProperties()![] as propName, propValue>
            <#if propValue.meta.propertyConfigFileLabel != "" && propValue.value??>
                <#if ikasanComponent.javaPackageName != "">
                    @org.springframework.beans.factory.annotation.Value("${r"${"}${flow.javaPackageName}.${ikasanComponent.javaPackageName}.${propValue.meta.propertyConfigFileLabel}}")
                <#else>
                    @org.springframework.beans.factory.annotation.Value("${r"${"}${flow.javaPackageName}.${propValue.meta.propertyConfigFileLabel}}")
                </#if>
                ${propValue.meta.dataType.getCanonicalName()}  ${propValue.meta.getPropertyConfigFileLabelAsVariable()};
            </#if>
        </#list>
    </#if>
</#list>
</#compress>
\n
<#compress>
<#list flow.flowComponentList![] as ikasanComponent>
    public ${ikasanComponent.type.elementCategory.baseClass} get${ikasanComponent.type.associatedMethodName}() {
    <#if ikasanComponent.type.bespokeClass>
        return new ${ikasanComponent.properties.BaseGroupName.value}.${flow.JavaPackageName}.${ikasanComponent.properties.BespokeClassName.value}();
    <#else>
        return builderFactory.getComponentBuilder().${ikasanComponent.type.associatedMethodName}()
        <#list ikasanComponent.getStandardProperties() as propName, propValue>
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