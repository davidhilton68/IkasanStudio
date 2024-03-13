<#assign StudioUtils=statics['org.ikasan.studio.StudioUtils']>
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
<#list flow.ftlGetConsumerAndFlowElements()![] as flowElement>
    <#if flowElement.componentMeta.generatesUserImplementedClass>
        @javax.annotation.Resource
        <#if flowElement.componentMeta.generatesUserImplementedClass>
        ${module.getPropertyValue('applicationPackageName')}.${flow.getJavaPackageName()}.${flowElement.getPropertyValue('userImplementedClassName')} ${flowElement.getJavaVariableName()};
        </#if>
    <#else>
    </#if>

    <#list flowElement.getStandardConfiguredProperties()![] as propKey, componentProperty>
        <#if componentProperty.meta.propertyConfigFileLabel?? && componentProperty.value??>
            <#if componentProperty.meta.usageDataType?starts_with("java.util.List")>
                <#assign f_startTag = r'#{${' >
                <#assign f_endTag = r'}}' >
            <#else>
                <#assign f_startTag = r'${'>
                <#assign f_endTag = r'}'>
            </#if>

            <#assign f_propertyName = '${StudioUtils.getPropertyLabelPackageStyle(module, flow, flowElement, componentProperty.meta.propertyConfigFileLabel)}'>
            @org.springframework.beans.factory.annotation.Value("${f_startTag}${f_propertyName}${f_endTag}")
            <#if componentProperty.meta.usageDataType?starts_with("java.util.List")>
                <#assign f_dataType = componentProperty.meta.usageDataType >
            <#else>
                <#assign f_dataType = componentProperty.meta.propertyDataType.getCanonicalName()>
            </#if>
            ${f_dataType} ${StudioUtils.getPropertyLabelVariableStyle(module, flow, flowElement, componentProperty.meta.propertyConfigFileLabel)};
        </#if>
    </#list>
</#list>
</#compress>

<#compress>
    <#list flow.ftlGetConsumerAndFlowElements()![] as flowElement>
    <#-- todo - move this to above section -->
    <#list flowElement.getStandardConfiguredProperties() as propKey, propValue>
        <#if propValue.meta.isUserSuppliedClass()>
            @javax.annotation.Resource
            <#if propValue.meta.usageDataType == "configurationDefined">
                ${module.getPropertyValue('applicationPackageName')}.${flow.getJavaPackageName()}.${propValue.valueString} ${StudioUtils.toJavaIdentifier(propValue.valueString)};
            <#else>
                ${propValue.meta.usageDataType} ${StudioUtils.toJavaIdentifier(propValue.valueString)};
            </#if>
        </#if>
    </#list>
    </#list>
</#compress>


<#compress>
<#list flow.ftlGetConsumerAndFlowElements()![] as flowElement>
    public ${flowElement.componentMeta.componentType} get${flowElement.getJavaClassName()}() {
    <#if flowElement.componentMeta.usesBuilder>
        <#if flowElement.componentMeta.ikasanComponentFactoryMethod??>
            return builderFactory.getComponentBuilder().${flowElement.componentMeta.ikasanComponentFactoryMethod}()
        <#else>
            return builderFactory.getComponentBuilder().${StudioUtils.toJavaIdentifier(flowElement.componentMeta.name)}()
        </#if>
    </#if>
    <#list flowElement.getStandardConfiguredProperties() as propKey, propValue>
        <#if propValue.value?? && propValue.meta.isSetterProperty() >
            <#if propValue.meta.propertyConfigFileLabel?? &&  propValue.meta.propertyConfigFileLabel!= "">
                <#if flowElement.componentMeta.generatesUserImplementedClass>${flowElement.getJavaVariableName()}</#if>.set${StudioUtils.toPascalCase(propValue.meta.propertyName)}(${StudioUtils.getPropertyLabelVariableStyle(module, flow, flowElement, propValue.meta.propertyConfigFileLabel)})<#if flowElement.componentMeta.generatesUserImplementedClass>;</#if>
            <#else>
                <#if propValue.meta.isUserSuppliedClass()>
                    <#if flowElement.componentMeta.generatesUserImplementedClass>${flowElement.getJavaVariableName()}</#if>.set${StudioUtils.toPascalCase(propValue.meta.propertyName)}(${StudioUtils.toJavaIdentifier(propValue.valueString)})<#if flowElement.componentMeta.generatesUserImplementedClass>;</#if>
                <#else>
                    <#if propValue.meta.usageDataType?? && propValue.meta.usageDataType == "java.lang.String">
                        <#if flowElement.componentMeta.generatesUserImplementedClass>${flowElement.getJavaVariableName()}</#if>.set${StudioUtils.toPascalCase(propValue.meta.propertyName)}("${propValue.valueString}")<#if flowElement.componentMeta.generatesUserImplementedClass>;</#if>
                    <#else>
                    <#if flowElement.componentMeta.generatesUserImplementedClass>${flowElement.getJavaVariableName()}</#if>.set${StudioUtils.toPascalCase(propValue.meta.propertyName)}(${propValue.valueString})<#if flowElement.componentMeta.generatesUserImplementedClass>;x</#if>
                    </#if>
                </#if>
            </#if>
        </#if>
    </#list>

<#-- Special case for message filter, set configuredResourceId to default -->
    <#if flowElement.componentMeta.name=="Message Filter" && flowElement.getProperty("IsConfiguredResource")?has_content && flowElement.getProperty("IsConfiguredResource").getValue() && (!flowElement.getProperty("ConfiguredResourceId")?has_content || !flowElement.getProperty("ConfiguredResourceId").getValue()?has_content)>
        ${flowElement.getJavaVariableName()}.setConfiguredResourceId("${StudioUtils.toJavaIdentifier(module.name)}-${StudioUtils.toJavaIdentifier(flow.name)}-${StudioUtils.toJavaIdentifier(flowElement.componentName)}");
    </#if>
    <#if flowElement.componentMeta.generatesUserImplementedClass>
        return ${flowElement.getJavaVariableName()};
    <#elseif !flowElement.componentMeta.usesBuilder>
        return new ${flowElement.componentMeta.ikasanComponentFactoryMethod}();
    <#else>
        .build();
    </#if>
    }
</#list>
</#compress>
}