<#assign StudioBuildUtils=statics['org.ikasan.studio.core.StudioBuildUtils']>
package ${studioPackageTag};

/**
* The component factory defines the details of the components and their configuration.
*
* This file is auto-generated by Ikasan Studio, do no edit.
*/
@org.springframework.context.annotation.Configuration
public class ComponentFactory${flow.getJavaClassName()}
{
@org.springframework.beans.factory.annotation.Value("${r"${module.name}"}")
private String moduleName;

@javax.annotation.Resource
org.ikasan.builder.BuilderFactory builderFactory;

<#compress>
<#list flow.ftlGetAllFlowElementsInAnyRouteNoEndpoints()![] as flowElement>
    <#if flowElement.componentMeta.generatesUserImplementedClass>
        @javax.annotation.Resource
        <#if flowElement.componentMeta.generatesUserImplementedClass>
<#--        ${module.getPropertyValue('applicationPackageName')}.${flow.getJavaPackageName()}.${flowElement.getPropertyValue('userImplementedClassName')} ${flowElement.getJavaVariableName()};-->
        ${module.getPropertyValue('applicationPackageName')}.${flow.getJavaPackageName()}.${StudioBuildUtils.substitutePlaceholderInPascalCase(module, flow, flowElement, flowElement.getPropertyValue('userImplementedClassName'))} ${StudioBuildUtils.substitutePlaceholderInJavaCamelCase(module, flow, flowElement, flowElement.getJavaVariableName())};
        </#if>
    <#else>
    </#if>

    <#list flowElement.getStandardComponentProperties()![] as propKey, componentProperty>
        <#if componentProperty.meta.propertyConfigFileLabel?? && componentProperty.value??>
            <#if componentProperty.meta.usageDataType?starts_with("java.util.List")>
                <#assign f_startTag = r'#{${' >
                <#assign f_endTag = r'}}' >
            <#else>
                <#assign f_startTag = r'${'>
                <#assign f_endTag = r'}'>
            </#if>

            <#assign f_propertyName = '${StudioBuildUtils.substitutePlaceholderInLowerCase(module, flow, flowElement, componentProperty.meta.propertyConfigFileLabel)}'>
            @org.springframework.beans.factory.annotation.Value("${f_startTag}${f_propertyName}${f_endTag}")
            <#if componentProperty.meta.usageDataType?starts_with("java.util.List")>
                <#assign f_dataType = componentProperty.meta.usageDataType >
            <#else>
                <#assign f_dataType = componentProperty.meta.propertyDataType.getCanonicalName()>
            </#if>
            ${f_dataType} ${StudioBuildUtils.substitutePlaceholderInJavaCamelCase(module, flow, flowElement, componentProperty.meta.propertyConfigFileLabel)};
        </#if>
    </#list>
</#list>
</#compress>

<#compress>
    <#list flow.ftlGetAllFlowElementsInAnyRouteNoEndpoints()![] as flowElement>
    <#-- todo - move this to above section -->
    <#list flowElement.getStandardComponentProperties() as propKey, propValue>
        <#if propValue.meta.isUserSuppliedClass()>
            @javax.annotation.Resource
            <#if propValue.meta.usageDataType == "configurationDefined">
                ${module.getPropertyValue('applicationPackageName')}.${flow.getJavaPackageName()}.${propValue.valueString} ${StudioBuildUtils.toJavaIdentifier(propValue.valueString)};
            <#else>
                ${propValue.meta.usageDataType} ${StudioBuildUtils.toJavaIdentifier(propValue.valueString)};
            </#if>
        </#if>
    </#list>
    </#list>
</#compress>


<#compress>
<#list flow.ftlGetAllFlowElementsInAnyRouteNoEndpoints()![] as flowElement>
    public ${flowElement.componentMeta.componentType} get${flowElement.getJavaClassName()}() {
    <#if flowElement.componentMeta.usesBuilderInFactory>
        <#if flowElement.componentMeta.ikasanComponentFactoryMethod??>
            return builderFactory.getComponentBuilder().${flowElement.componentMeta.ikasanComponentFactoryMethod}()
        <#else>
            return builderFactory.getComponentBuilder().${StudioBuildUtils.toJavaIdentifier(flowElement.componentMeta.name)}()
        </#if>
    </#if>
    <#list flowElement.getStandardComponentProperties() as propKey, propValue>
        <#if propValue.value?? && propValue.meta.isSetterProperty() >
            <#if propValue.meta.getSetterMethod()?? && propValue.meta.getSetterMethod()!= "">
                <#assign setter="${propValue.meta.getSetterMethod()}">
            <#else>
                <#assign setter="set${StudioBuildUtils.toPascalCase(propValue.meta.propertyName)}">
            </#if>
            <#if propValue.meta.propertyConfigFileLabel?? &&  propValue.meta.propertyConfigFileLabel!= "">
                <#if flowElement.componentMeta.generatesUserImplementedClass>${flowElement.getJavaVariableName()}</#if>.${setter}(${StudioBuildUtils.substitutePlaceholderInJavaCamelCase(module, flow, flowElement, propValue.meta.propertyConfigFileLabel)})<#if flowElement.componentMeta.generatesUserImplementedClass>;</#if>
            <#else>
                <#if propValue.meta.isUserSuppliedClass()>
                    <#if flowElement.componentMeta.generatesUserImplementedClass>${flowElement.getJavaVariableName()}</#if>.${setter}(${StudioBuildUtils.toJavaIdentifier(propValue.valueString)})<#if flowElement.componentMeta.generatesUserImplementedClass>;</#if>
                <#else>
                    <#if propValue.meta.usageDataType?? && propValue.meta.usageDataType == "java.lang.String">
                        <#if flowElement.componentMeta.generatesUserImplementedClass>${flowElement.getJavaVariableName()}</#if>.${setter}("${StudioBuildUtils.substitutePlaceholderInJavaCamelCase(module, flow, flowElement, propValue.valueString)}")<#if flowElement.componentMeta.generatesUserImplementedClass>;</#if>
                    <#else>
                        <#if flowElement.componentMeta.generatesUserImplementedClass>${flowElement.getJavaVariableName()}</#if>.${setter}(${StudioBuildUtils.substitutePlaceholderInJavaCamelCase(module, flow, flowElement, propValue.valueString)})<#if flowElement.componentMeta.generatesUserImplementedClass>;</#if>
                    </#if>
                </#if>
            </#if>
        </#if>
    </#list>
    <#-- Special case for message filter, set configuredResourceId to default -->
    <#if flowElement.componentMeta.name=="Message Filter" && flowElement.getProperty("IsConfiguredResource")?has_content && flowElement.getProperty("IsConfiguredResource").getValue() && (!flowElement.getProperty("ConfiguredResourceId")?has_content || !flowElement.getProperty("ConfiguredResourceId").getValue()?has_content)>
        ${flowElement.getJavaVariableName()}.setConfiguredResourceId("${StudioBuildUtils.toJavaIdentifier(module.name)}-${StudioBuildUtils.toJavaIdentifier(flow.name)}-${StudioBuildUtils.toJavaIdentifier(flowElement.componentName)}");
    </#if>
    <#if flowElement.componentMeta.generatesUserImplementedClass>
        return ${flowElement.getJavaVariableName()};
    <#elseif flowElement.componentMeta.useImplementingClassInFactory>
        return new ${flowElement.componentMeta.implementingClass}();
    <#else>
        .build();
    </#if>
    }
</#list>
</#compress>
}