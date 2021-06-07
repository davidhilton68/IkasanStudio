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

@org.springframework.beans.factory.annotation.Value("${myflow1.ftp.producer.max-retry-attempts}")
java.lang.Integer myFlow1FtpProducerMaxretryattempts;
@org.springframework.beans.factory.annotation.Value("${myflow1.ftp.producer.outputDirectory}")
java.lang.String myFlow1FtpProducerOutputDirectory;
@org.springframework.beans.factory.annotation.Value("${myflow1.ftp.producer.ftps-protocol}")
java.lang.String myFlow1FtpProducerFtpsprotocol;
@org.springframework.beans.factory.annotation.Value("${myflow1.ftp.producer.password}")
java.lang.String myFlow1FtpProducerPassword;
@org.springframework.beans.factory.annotation.Value("${myflow1.ftp.producer.remote-port}")
java.lang.Integer myFlow1FtpProducerRemoteport;
@org.springframework.beans.factory.annotation.Value("${myflow1.ftp.producer.ftps-port}")
java.lang.Integer myFlow1FtpProducerFtpsport;
@org.springframework.beans.factory.annotation.Value("${myflow1.ftp.producer.system-key}")
java.lang.String myFlow1FtpProducerSystemkey;
@org.springframework.beans.factory.annotation.Value("${myflow1.ftp.producer.remote-host}")
java.lang.String myFlow1FtpProducerRemotehost;
@org.springframework.beans.factory.annotation.Value("${myflow1.ftp.producer.username}")
java.lang.String myFlow1FtpProducerUsername;
@org.springframework.beans.factory.annotation.Value("${myflow1.ftp.producer.clientID}")
java.lang.String myFlow1FtpProducerClientID;
@javax.annotation.Resource
org.ikasan.endpoint.ftp.producer.FtpProducerConfiguration myConfigurationClass;
@javax.annotation.Resource
org.ikasan.spec.management.ManagedResourceRecoveryManager myManagedResourceRecoveryManagerClass;
@javax.annotation.Resource
org.springframework.transaction.jta.JtaTransactionManager myTransactionManagerClass;

public org.ikasan.spec.component.endpoint.Producer getTestFtpProducer() {
return builderFactory.getComponentBuilder().ftpProducer()
.setTempFileName("myTempFileName")
.setMaxRetryAttempts(myFlow1FtpProducerMaxretryattempts)
.setConfiguration(myConfigurationClass)
.setOutputDirectory(myFlow1FtpProducerOutputDirectory)
.setChecksumDelivered(true)
.setManagedResourceRecoveryManager(myManagedResourceRecoveryManagerClass)
.setFtpsProtocol(myFlow1FtpProducerFtpsprotocol)
.setFtpsKeyStoreFilePassword("myFtpsKeyStoreFilePassword")
.setSocketTimeout(22)
.setCleanupJournalOnComplete(true)
.setPassword(myFlow1FtpProducerPassword)
.setCriticalOnStartup(true)
.setCreateParentDirectory(true)
.setOverwrite(true)
.setFtpsKeyStoreFilePath("/test/ftps/keystore")
.setFtpsIsImplicit(true)
.setRemotePort(myFlow1FtpProducerRemoteport)
.setUnzip(true)
.setFtpsPort(myFlow1FtpProducerFtpsport)
.setSystemKey(myFlow1FtpProducerSystemkey)
.setRenameExtension("newExtension")
.setFTPS(true)
.setActive(true)
.setRemoteHost(myFlow1FtpProducerRemotehost)
.setConfiguredResourceId("myUniqueConfiguredResourceIdName")
.setUsername(myFlow1FtpProducerUsername)
.setTransactionManager(myTransactionManagerClass)
.setDataTimeout(300001)
.setClientID(myFlow1FtpProducerClientID)
.build();
}}