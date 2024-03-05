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

@org.springframework.beans.factory.annotation.Value("${myflow1.scheduled.consumer.cron-expression}")
java.lang.String myFlow1ScheduledConsumerCronexpression;
@javax.annotation.Resource
org.ikasan.component.endpoint.filesystem.messageprovider.FileConsumerConfiguration orgIkasanMyflowConfiguration;
@javax.annotation.Resource
org.ikasan.component.endpoint.quartz.consumer.MessageProvider orgIkasanMyflowMyMessageProvider;
@javax.annotation.Resource
org.ikasan.spec.management.ManagedResourceRecoveryManager orgIkasanMyflowMyManagedResourceRecoveryManager;
@javax.annotation.Resource
org.ikasan.spec.event.ManagedEventIdentifierService orgIkasanMyflowMyEventService;
@javax.annotation.Resource
org.ikasan.spec.event.EventFactory orgIkasanMyflowMyEventFactory;

public org.ikasan.spec.component.endpoint.Consumer getMyScheduledConsumer() {
return builderFactory.getComponentBuilder().scheduledConsumer()
.setCriticalOnStartup(true)
.setEager(true)
.setConfiguration(orgIkasanMyflowConfiguration)
.setTimezone(UTC)
.setConfiguredResourceId(bob)
.setIgnoreMisfire(true)
.setCronExpression(myFlow1ScheduledConsumerCronexpression)
.setMessageProvider(orgIkasanMyflowMyMessageProvider)
.setMaxEagerCallbacks(10)
.setManagedResourceRecoveryManager(orgIkasanMyflowMyManagedResourceRecoveryManager)
.setManagedEventIdentifierService(orgIkasanMyflowMyEventService)
.setEventFactory(orgIkasanMyflowMyEventFactory)
.build();
}}