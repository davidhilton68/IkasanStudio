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

@org.springframework.beans.factory.annotation.Value("${jms.myintegrationmodule.myflow1.testjmsconsumer.destination.jndi.user}")
java.lang.String jmsMyIntegrationModuleMyFlow1TestJmsConsumerDestinationJndiUser;
@org.springframework.beans.factory.annotation.Value("${jms.myintegrationmodule.myflow1.testjmsconsumer.destination.jndi.initial}")
java.lang.String jmsMyIntegrationModuleMyFlow1TestJmsConsumerDestinationJndiInitial;
@org.springframework.beans.factory.annotation.Value("${jms.myintegrationmodule.myflow1.testjmsconsumer.destination.jndi.name}")
java.lang.String jmsMyIntegrationModuleMyFlow1TestJmsConsumerDestinationJndiName;
@org.springframework.beans.factory.annotation.Value("${jms.myintegrationmodule.myflow1.testjmsconsumer.connection.factory.name}")
java.lang.String jmsMyIntegrationModuleMyFlow1TestJmsConsumerConnectionFactoryName;
@org.springframework.beans.factory.annotation.Value("${jms.myintegrationmodule.myflow1.testjmsconsumer.connection.factory.jndi.provider.url}")
java.lang.String jmsMyIntegrationModuleMyFlow1TestJmsConsumerConnectionFactoryJndiProviderUrl;
@org.springframework.beans.factory.annotation.Value("${jms.myintegrationmodule.myflow1.testjmsconsumer.destination.jndi.provider.url}")
java.lang.String jmsMyIntegrationModuleMyFlow1TestJmsConsumerDestinationJndiProviderUrl;
@org.springframework.beans.factory.annotation.Value("${jms.myintegrationmodule.myflow1.testjmsconsumer.destination.jndi.password}")
java.lang.String jmsMyIntegrationModuleMyFlow1TestJmsConsumerDestinationJndiPassword;
@org.springframework.beans.factory.annotation.Value("${jms.myintegrationmodule.myflow1.testjmsconsumer.connection.factory.password}")
java.lang.String jmsMyIntegrationModuleMyFlow1TestJmsConsumerConnectionFactoryPassword;
@org.springframework.beans.factory.annotation.Value("${jms.myintegrationmodule.myflow1.testjmsconsumer.connection.factory.jndi.initial}")
java.lang.String jmsMyIntegrationModuleMyFlow1TestJmsConsumerConnectionFactoryJndiInitial;
@org.springframework.beans.factory.annotation.Value("${jms.myintegrationmodule.myflow1.testjmsconsumer.connection.factory.user}")
java.lang.String jmsMyIntegrationModuleMyFlow1TestJmsConsumerConnectionFactoryUser;
@javax.annotation.Resource
org.ikasan.spec.event.EventFactory myEventFactoryClassName;
@javax.annotation.Resource
javax.jms.ConnectionFactory myConnectionFactory;
@javax.annotation.Resource
org.ikasan.component.endpoint.quartz.consumer.MessageProvider myMessageProvider;
@javax.annotation.Resource
org.ikasan.component.endpoint.filesystem.messageprovider.FileConsumerConfiguration myConfigurationClass;
@javax.annotation.Resource
org.springframework.transaction.jta.JtaTransactionManager myTransactionManagerClass;
@javax.annotation.Resource
org.ikasan.spec.event.ManagedRelatedEventIdentifierService myManagedIdentifierService;

public org.ikasan.spec.component.endpoint.Consumer getTestJmsConsumer() {
return builderFactory.getComponentBuilder().jmsConsumer()
.setEventFactory(myEventFactoryClassName)
.setConnectionFactory(myConnectionFactory)
.setDestinationJndiPropertyUrlPkgPrefixes("org.myapp")
.setMessageProvider(myMessageProvider)
.setConnectionPassword("myConnectionPassword")
.setMaxConcurrentConsumers(11)
.setAutoSplitBatch(true)
.setDestinationJndiPropertySecurityPrincipal(jmsMyIntegrationModuleMyFlow1TestJmsConsumerDestinationJndiUser)
.setDestinationJndiPropertyFactoryInitial(jmsMyIntegrationModuleMyFlow1TestJmsConsumerDestinationJndiInitial)
.setConnectionUsername("myConnectionUsername")
.setSessionTransacted(true)
.setBatchSize(10)
.setDestinationJndiName(jmsMyIntegrationModuleMyFlow1TestJmsConsumerDestinationJndiName)
.setBatchMode(true)
.setDurableSubscriptionName("myDurableSubscriptionName")
.setConcurrentConsumers(10)
.setCacheLevel(1)
.setConnectionFactoryName(jmsMyIntegrationModuleMyFlow1TestJmsConsumerConnectionFactoryName)
.setConnectionFactoryJndiPropertyProviderUrl(jmsMyIntegrationModuleMyFlow1TestJmsConsumerConnectionFactoryJndiProviderUrl)
.setDestinationJndiProperties("myDestinationJndiProperties")
.setConfiguredResourceId("myUniqueConfiguredResourceIdName")
.setSessionAcknowledgeMode(1)
.setConnectionFactoryJNDIProperties({key1:'value1',key2:'value2'})
.setReceiveTimeout(1000)
.setConnectionFactoryJndiPropertySecurityCredentials("myConnectionFactoryJndiPropertySecurityCredentials")
.setDestinationJndiPropertyProviderUrl(jmsMyIntegrationModuleMyFlow1TestJmsConsumerDestinationJndiProviderUrl)
.setConnectionFactoryJndiPropertySecurityPrincipal("myConnectionFactoryJndiPropertySecurityPrincipal")
.setConfiguration(myConfigurationClass)
.setDestinationJndiPropertySecurityCredentials(jmsMyIntegrationModuleMyFlow1TestJmsConsumerDestinationJndiPassword)
.setPubSubDomain(myPubSubDomain)
.setDurable(true)
.setConnectionFactoryPassword(jmsMyIntegrationModuleMyFlow1TestJmsConsumerConnectionFactoryPassword)
.setConnectionFactoryJndiPropertyUrlPkgPrefixes("myConnectionFactoryJndiPropertyUrlPkgPrefixes")
.setAutoContentConversion(true)
.setConnectionFactoryJndiPropertyFactoryInitial(jmsMyIntegrationModuleMyFlow1TestJmsConsumerConnectionFactoryJndiInitial)
.setConnectionFactoryUsername(jmsMyIntegrationModuleMyFlow1TestJmsConsumerConnectionFactoryUser)
.setTransactionManager(myTransactionManagerClass)
.setManagedIdentifierService(myManagedIdentifierService)
.build();
}}