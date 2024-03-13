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

@org.springframework.beans.factory.annotation.Value("${myflow1.ftp.consumer.cron-expression}")
java.lang.String myFlow1FtpConsumerCronExpression;
@org.springframework.beans.factory.annotation.Value("${myflow1.ftp.consumer.filename-pattern}")
java.lang.String myFlow1FtpConsumerFilenamePattern;
@org.springframework.beans.factory.annotation.Value("${myflow1.ftp.consumer.ftps-port}")
java.lang.Integer myFlow1FtpConsumerFtpsPort;
@org.springframework.beans.factory.annotation.Value("${myflow1.ftp.consumer.ftps-protocol}")
java.lang.Integer myFlow1FtpConsumerFtpsProtocol;
@org.springframework.beans.factory.annotation.Value("${myflow1.ftp.consumer.max-retry-attempts}")
java.lang.Integer myFlow1FtpConsumerMaxRetryAttempts;
@org.springframework.beans.factory.annotation.Value("${myflow1.ftp.consumer.move-on-success-new-path}")
java.lang.String myFlow1FtpConsumerMoveOnSuccessNewPath;
@org.springframework.beans.factory.annotation.Value("${myflow1.ftp.consumer.password}")
java.lang.String myFlow1FtpConsumerPassword;
@org.springframework.beans.factory.annotation.Value("${myflow1.ftp.consumer.password-filepath}")
java.lang.String myFlow1FtpConsumerPasswordFilepath;
@org.springframework.beans.factory.annotation.Value("${myflow1.ftp.consumer.remote-host}")
java.lang.String myFlow1FtpConsumerRemoteHost;
@org.springframework.beans.factory.annotation.Value("${myflow1.ftp.consumer.remote-port}")
java.lang.String myFlow1FtpConsumerRemotePort;
@org.springframework.beans.factory.annotation.Value("${myflow1.ftp.consumer.source-directory}")
java.lang.String myFlow1FtpConsumerSourceDirectory;
@org.springframework.beans.factory.annotation.Value("${myflow1.ftp.consumer.system-key}")
java.lang.String myFlow1FtpConsumerSystemKey;
@org.springframework.beans.factory.annotation.Value("${myflow1.ftp.consumer.username}")
java.lang.String myFlow1FtpConsumerUsername;
@javax.annotation.Resource
org.ikasan.component.endpoint.filesystem.messageprovider.FileConsumerConfiguration myConfigurationClass;
@javax.annotation.Resource
org.ikasan.spec.event.ManagedEventIdentifierService myManagedEventIdentifierServiceClass;
@javax.annotation.Resource
org.ikasan.spec.management.ManagedResourceRecoveryManager myManagedResourceRecoveryManagerClass;
@javax.annotation.Resource
org.ikasan.component.endpoint.quartz.consumer.MessageProvider myMessageProviderClass;
@javax.annotation.Resource
org.ikasan.framework.factory.DirectoryURLFactory myDirectoryURLFactoryClass;
@javax.annotation.Resource
org.springframework.transaction.jta.JtaTransactionManager myTransactionManagerClass;

public org.ikasan.spec.component.endpoint.Consumer getMyFTPConsumer() {
return builderFactory.getComponentBuilder().fTPConsumer()
.setActive(true)
.setAgeOfFiles(10)
.setChecksum(true)
.setChronological(true)
.setChunkSize(1048577)
.setChunking(true)
.setCleanupJournalOnComplete(true)
.setClientID(myClientId)
.setConfiguration(myConfigurationClass)
.setConfiguredResourceId("myUniqueConfiguredResourceIdName")
.setConnectionTimeout(600001)
.setCronExpression(myFlow1FtpConsumerCronExpression)
.setDataTimeout(300001)
.setDestructive(true)
.setFilenamePattern(myFlow1FtpConsumerFilenamePattern)
.setFilterDuplicates(true)
.setFilterOnFilename(true)
.setFilterOnLastModifiedDate(true)
.setFtps(true)
.setFtpsIsImplicit(true)
.setFtpsKeyStoreFilePassword(myFtpsKeyStoreFilePassword)
.setFtpsKeyStoreFilePath(/test/ftps/keystore)
.setFtpsPort(myFlow1FtpConsumerFtpsPort)
.setFtpsProtocol(myFlow1FtpConsumerFtpsProtocol)
.setIgnoreMisfire(true)
.setIsRecursive(true)
.setManagedEventIdentifierService(myManagedEventIdentifierServiceClass)
.setManagedResourceRecoveryManager(myManagedResourceRecoveryManagerClass)
.setMaxEagerCallbacks(1)
.setMaxRetryAttempts(myFlow1FtpConsumerMaxRetryAttempts)
.setMaxRows(11)
.setMessageProvider(myMessageProviderClass)
.setMaxRows(12)
.setMoveOnSuccess(true)
.setMoveOnSuccessNewPath(myFlow1FtpConsumerMoveOnSuccessNewPath)
.setPassword(myFlow1FtpConsumerPassword)
.setPasswordFilePath(myFlow1FtpConsumerPasswordFilepath)
.setRemoteHost(myFlow1FtpConsumerRemoteHost)
.setRemotePort(myFlow1FtpConsumerRemotePort)
.setRenameOnSuccess(true)
.setRenameOnSuccessExtension(newExtension)
.setScheduledJobGroupName(myScheduledJobGroupName)
.setScheduledJobName(myScheduledJobName)
.setSocketTimeout(22)
.setSourceDirectory(myFlow1FtpConsumerSourceDirectory)
.setSourceDirectoryURLFactory(myDirectoryURLFactoryClass)
.setSystemKey(myFlow1FtpConsumerSystemKey)
.setTransactionManager(myTransactionManagerClass)
.setUsername(myFlow1FtpConsumerUsername)
.build();
}}