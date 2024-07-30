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

@org.springframework.beans.factory.annotation.Value("#{'${myflow1.email.producer.bccRecipients}'.split(',')}")
java.util.List<String> myFlow1EmailProducerBccRecipients;
@org.springframework.beans.factory.annotation.Value("#{'${myflow1.email.producer.ccRecipients}'.split(',')}")
java.util.List<String> myFlow1EmailProducerCcRecipients;
@javax.annotation.Resource
org.ikasan.component.endpoint.filesystem.messageprovider.FileConsumerConfiguration myConfigurationClass;

public org.ikasan.spec.component.endpoint.Producer getMyEmailProducer() {
return builderFactory.getComponentBuilder().emailProducer()
.setBccRecipient(myBccRecipient)
.setBccRecipients(myFlow1EmailProducerBccRecipients)
.setCcRecipient(myCcRecipient)
.setCcRecipients(myFlow1EmailProducerCcRecipients)
.setConfiguration(myConfigurationClass)
.setConfiguredResourceId("myUniqueConfiguredResourceIdName")
.setCriticalOnStartup(true)
.setEmailBody(myEmailBody)
.setEmailFormat(html)
.setExtendedMailSessionProperties(key1value1key2value2)
.setFrom(FromAddress)
.setHasAttachments(true)
.setMailDebug(true)
.setMailMimeAddressStrict(true)
.setMailPassword(myMailPassword)
.setMailPopClass(myMailPopClass)
.setMailPopPort(100)
.setMailPopUser(myMailPopUser)
.setMailRuntimeEnvironment(myMailRuntimeEnvironment)
.setMailSmtpClass(myMailSmtpClass)
.setMailSmtpHost(myMailSmtpHost)
.setMailSmtpPort(101)
.setMailSmtpUser(myMailSmtpUser)
.setMailStoreProtocol(myMailStoreProtocol)
.setMailSubject(myMailSubject)
.setMailhost(myMailhostAddress)
.setToRecipient(myToRecipient)
.setToRecipients(to1,to2)
.setTransportProtocol(myTransportProtocol)
.setUser(myUser)
.build();
}}