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
org.ikasan.endpoint.ftp.producer.FtpProducerConfiguration myConfigurationClass;
@javax.annotation.Resource
java.util.Map<String, String> key1value1key2value2;

public org.ikasan.spec.component.endpoint.Producer getTestEmailProducer() {
return builderFactory.getComponentBuilder().emailProducer()
.setMailPopPort(100)
.setUser("myUser")
.setConfiguration(myConfigurationClass)
.setMailSubject("myMailSubject")
.setToRecipient("myToRecipient")
.setMailSmtpHost("myMailSmtpHost")
.setEmailFormat("html")
.setMailSmtpClass("myMailSmtpClass")
.setCcRecipients({'cc1','cc2'})
.setMailPopClass("myMailPopClass")
.setMailSmtpUser("myMailSmtpUser")
.setMailRuntimeEnvironment("myMailRuntimeEnvironment")
.setCriticalOnStartup(true)
.setHasAttachments(true)
.setToRecipients({'to1','to2'})
.setMailMimeAddressStrict(true)
.setMailPassword("myMailPassword")
.setMailStoreProtocol("myMailStoreProtocol")
.setFrom("FromAddress")
.setCcRecipient("myCcRecipient")
.setBccRecipient("myBccRecipient")
.setMailSmtpPort(101)
.setMailDebug(true)
.setEmailBody("myEmailBody")
.setConfiguredResourceId("myUniqueConfiguredResourceIdName")
.setBccRecipients({'bcc1','bcc2'})
.setMailPopUser("myMailPopUser")
.setMailhost("myMailhostAddress")
.setTransportProtocol("myTransportProtocol")
.setExtendedMailSessionProperties(key1value1key2value2)
.build();
}}