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

@org.springframework.beans.factory.annotation.Value("${myflow1.testsftpconsumer.sftp.consumer.max-retry-attempts}")
java.lang.Integer sftpConsumerMaxretryattempts;
@org.springframework.beans.factory.annotation.Value("${myflow1.testsftpconsumer.sftp.consumer.cron-expression}")
java.lang.String sftpConsumerCronexpression;
@org.springframework.beans.factory.annotation.Value("${myflow1.testsftpconsumer.sftp.consumer.filename-pattern}")
java.lang.String sftpConsumerFilenamepattern;
@org.springframework.beans.factory.annotation.Value("${myflow1.testsftpconsumer.sftp.consumer.password}")
java.lang.String sftpConsumerPassword;
@org.springframework.beans.factory.annotation.Value("${myflow1.testsftpconsumer.sftp.consumer.remote-port}")
java.lang.Integer sftpConsumerRemoteport;
@org.springframework.beans.factory.annotation.Value("${myflow1.testsftpconsumer.sftp.consumer.preferred-key-exchange-algorithm}")
java.lang.String sftpConsumerPreferredkeyexchangealgorithm;
@org.springframework.beans.factory.annotation.Value("${myflow1.testsftpconsumer.sftp.consumer.move-on-success-new-path}")
java.lang.String sftpConsumerMoveonsuccessnewpath;
@org.springframework.beans.factory.annotation.Value("${myflow1.testsftpconsumer.sftp.consumer.source-directory-url-factory}")
org.ikasan.framework.factory.DirectoryURLFactory sftpConsumerSourcedirectoryurlfactory;
@org.springframework.beans.factory.annotation.Value("${myflow1.testsftpconsumer.sftp.consumer.private-key-filename}")
java.lang.String sftpConsumerPrivatekeyfilename;
@org.springframework.beans.factory.annotation.Value("${myflow1.testsftpconsumer.sftp.consumer.remote-host}")
java.lang.String sftpConsumerRemotehost;
@org.springframework.beans.factory.annotation.Value("${myflow1.testsftpconsumer.sftp.consumer.username}")
java.lang.String sftpConsumerUsername;
@org.springframework.beans.factory.annotation.Value("${myflow1.testsftpconsumer.sftp.consumer.source-directory}")
java.lang.String sftpConsumerSourcedirectory;
@org.springframework.beans.factory.annotation.Value("${myflow1.testsftpconsumer.sftp.consumer.known-hosts-filenam}")
java.lang.String sftpConsumerKnownhostsfilenam;

public org.ikasan.spec.component.endpoint.Consumer getTestsftpconsumer() {
return builderFactory.getComponentBuilder().sftpConsumer()
.setTimezone(GMT)
.setConnectionTimeout(600001)
.setMaxRetryAttempts(sftpConsumerMaxretryattempts)
.setIgnoreMisfire(true)
.setMoveOnSuccess(true)
.setChecksum(true)
.setScheduledJobName(myScheduledJobName)
.setCronExpression(sftpConsumerCronexpression)
.setRenameOnSuccessExtension(ok)
.setCleanupJournalOnComplete(true)
.setFilenamePattern(sftpConsumerFilenamepattern)
.setPassword(sftpConsumerPassword)
.setMaxEagerCallbacks(1)
.setCriticalOnStartup(true)
.setEager(true)
.setFilterOnFilename(true)
.setFilterOnLastModifiedDate(true)
.setRemotePort(sftpConsumerRemoteport)
.setPreferredKeyExchangeAlgorithm(sftpConsumerPreferredkeyexchangealgorithm)
.setMoveOnSuccessNewPath(sftpConsumerMoveonsuccessnewpath)
.setRenameOnSuccess(true)
.setAgeOfFiles(10)
.setIsRecursive(true)
.setChunking(true)
.setSourceDirectoryURLFactory(sftpConsumerSourcedirectoryurlfactory)
.setChronological(true)
.setPrivateKeyFilename(sftpConsumerPrivatekeyfilename)
.setChunkSize(1048577)
.setMinAge(12)
.setRemoteHost(sftpConsumerRemotehost)
.setDestructive(true)
.setUsername(sftpConsumerUsername)
.setClientID(myClientId)
.setScheduledJobGroupName(myScheduledJobGroupName)
.setSourceDirectory(sftpConsumerSourcedirectory)
.setMaxRows(11)
.setFilterDuplicates(true)
.setKnownHostsFilename(sftpConsumerKnownhostsfilenam)
.build();
}}