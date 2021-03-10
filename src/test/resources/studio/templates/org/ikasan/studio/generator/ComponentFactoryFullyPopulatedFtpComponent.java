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

@org.springframework.beans.factory.annotation.Value("${myflow1.testftpconsumer.ftp.consumer.max-retry-attempts}")
java.lang.Integer ftpConsumerMaxretryattempts;
@org.springframework.beans.factory.annotation.Value("${myflow1.testftpconsumer.ftp.consumer.configuration}")
org.ikasan.endpoint.ftp.consumer.FtpConsumerConfiguration ftpConsumerConfiguration;
@org.springframework.beans.factory.annotation.Value("${myflow1.testftpconsumer.ftp.consumer.ftps-protocol}")
java.lang.String ftpConsumerFtpsprotocol;
@org.springframework.beans.factory.annotation.Value("${myflow1.testftpconsumer.ftp.consumer.cron-expression}")
java.lang.String ftpConsumerCronexpression;
@org.springframework.beans.factory.annotation.Value("${myflow1.testftpconsumer.ftp.consumer.filename-pattern}")
java.lang.String ftpConsumerFilenamepattern;
@org.springframework.beans.factory.annotation.Value("${myflow1.testftpconsumer.ftp.consumer.password}")
java.lang.String ftpConsumerPassword;
@org.springframework.beans.factory.annotation.Value("${myflow1.testftpconsumer.ftp.consumer.remote-port}")
java.lang.Integer ftpConsumerRemoteport;
@org.springframework.beans.factory.annotation.Value("${myflow1.testftpconsumer.ftp.consumer.move-on-success-new-path}")
java.lang.String ftpConsumerMoveonsuccessnewpath;
@org.springframework.beans.factory.annotation.Value("${myflow1.testftpconsumer.ftp.consumer.ftps-port}")
java.lang.Integer ftpConsumerFtpsport;
@org.springframework.beans.factory.annotation.Value("${myflow1.testftpconsumer.ftp.consumer.source-directory-url-factory}")
org.ikasan.framework.factory.DirectoryURLFactory ftpConsumerSourcedirectoryurlfactory;
@org.springframework.beans.factory.annotation.Value("${myflow1.testftpconsumer.ftp.consumer.system-key}")
java.lang.String ftpConsumerSystemkey;
@org.springframework.beans.factory.annotation.Value("${myflow1.testftpconsumer.ftp.consumer.remote-host}")
java.lang.String ftpConsumerRemotehost;
@org.springframework.beans.factory.annotation.Value("${myflow1.testftpconsumer.ftp.consumer.username}")
java.lang.String ftpConsumerUsername;
@org.springframework.beans.factory.annotation.Value("${myflow1.testftpconsumer.ftp.consumer.transactionManager}")
org.springframework.transaction.jta.JtaTransactionManager ftpConsumerTransactionmanager;
@org.springframework.beans.factory.annotation.Value("${myflow1.testftpconsumer.ftp.consumer.password-filepath}")
java.lang.String ftpConsumerPasswordfilepath;
@org.springframework.beans.factory.annotation.Value("${myflow1.testftpconsumer.ftp.consumer.source-directory}")
java.lang.String ftpConsumerSourcedirectory;

public org.ikasan.spec.component.endpoint.Consumer getTestftpconsumer() {
return builderFactory.getComponentBuilder().ftpConsumer()
.setConnectionTimeout(600001)
.setMaxRetryAttempts(ftpConsumerMaxretryattempts)
.setConfiguration(ftpConsumerConfiguration)
.setFtpsProtocol(ftpConsumerFtpsprotocol)
.setFtpsKeyStoreFilePassword(myFtpsKeyStoreFilePassword)
.setMoveOnSuccess(true)
.setChecksum(true)
.setScheduledJobName(myScheduledJobName)
.setCronExpression(ftpConsumerCronexpression)
.setSocketTimeout(22)
.setRenameOnSuccessExtension(ok)
.setCleanupJournalOnComplete(true)
.setFilenamePattern(ftpConsumerFilenamepattern)
.setPassword(ftpConsumerPassword)
.setFilterOnLastModifiedDate(true)
.setFtpsKeyStoreFilePath(/test/ftps/keystore)
.setFtpsIsImplicit(true)
.setRemotePort(ftpConsumerRemoteport)
.setMoveOnSuccessNewPath(ftpConsumerMoveonsuccessnewpath)
.setRenameOnSuccess(true)
.setAgeOfFiles(10)
.setIsRecursive(true)
.setChunking(true)
.setFtpsPort(ftpConsumerFtpsport)
.setSourceDirectoryURLFactory(ftpConsumerSourcedirectoryurlfactory)
.setSystemKey(ftpConsumerSystemkey)
.setFTPS(true)
.setChronological(true)
.setActive(true)
.setChunkSize(1048577)
.setMinAge(12)
.setRemoteHost(ftpConsumerRemotehost)
.setDestructive(true)
.setUsername(ftpConsumerUsername)
.setTransactionManager(ftpConsumerTransactionmanager)
.setDataTimeout(300001)
.setPasswordFilePath(ftpConsumerPasswordfilepath)
.setClientID(myClientId)
.setScheduledJobGroupName(myScheduledJobGroupName)
.setSourceDirectory(ftpConsumerSourcedirectory)
.setMaxRows(11)
.setFilterDuplicates(true)
.build();
}}