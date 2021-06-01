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

@org.springframework.beans.factory.annotation.Value("${myflow1.testsftpproducer.ftp.producer.max-retry-attempts}")
java.lang.Integer ftpProducerMaxretryattempts;
@org.springframework.beans.factory.annotation.Value("${myflow1.testsftpproducer.ftp.producer.outputDirectory}")
java.lang.String ftpProducerOutputDirectory;
@org.springframework.beans.factory.annotation.Value("${myflow1.testsftpproducer.ftp.producer.remote-port}")
java.lang.Integer ftpProducerRemoteport;
@org.springframework.beans.factory.annotation.Value("${myflow1.testsftpproducer.ftp.producer.remote-host}")
java.lang.String ftpProducerRemotehost;
@org.springframework.beans.factory.annotation.Value("${myflow1.testsftpproducer.ftp.producer.username}")
java.lang.String ftpProducerUsername;
@org.springframework.beans.factory.annotation.Value("${myflow1.testsftpproducer.ftp.producer.clientID}")
java.lang.String ftpProducerClientID;
@org.springframework.beans.factory.annotation.Value("${myflow1.testsftpproducer.ftp.producer.password}")
java.lang.String ftpProducerPassword;
@javax.annotation.Resource
org.ikasan.endpoint.ftp.producer.FtpProducerConfiguration myConfigurationClass;
@javax.annotation.Resource
org.ikasan.spec.management.ManagedResourceRecoveryManager myManagedResourceRecoveryManagerClass;

public org.ikasan.spec.component.endpoint.Producer getTestSftpProducer() {
return builderFactory.getComponentBuilder().sftpProducer()
.setCriticalOnStartup(true)
.setTempFileName("myTempFileName")
.setCreateParentDirectory(true)
.setConnectionTimeout(300001)
.setMaxRetryAttempts(ftpProducerMaxretryattempts)
.setConfiguration(myConfigurationClass)
.setOutputDirectory(ftpProducerOutputDirectory)
.setRemotePort(ftpProducerRemoteport)
.setChecksumDelivered(true)
.setPreferredKeyExchangeAlgorithm("myPreferredKeyExchangeAlgorithm")
.setUnzip(true)
.setRenameExtension("newExtension")
.setManagedResourceRecoveryManager(myManagedResourceRecoveryManagerClass)
.setPrivateKeyFilename("myPrivateKeyFilename")
.setRemoteHost(ftpProducerRemotehost)
.setConfiguredResourceId("myUniqueConfiguredResourceIdName")
.setUsername(ftpProducerUsername)
.setClientID(ftpProducerClientID)
.setKnownHostsFilename("myKnownHostsFilename")
.setCleanupJournalOnComplete(true)
.setCleanUpChunks(true)
.setPassword(ftpProducerPassword)
.build();
}}