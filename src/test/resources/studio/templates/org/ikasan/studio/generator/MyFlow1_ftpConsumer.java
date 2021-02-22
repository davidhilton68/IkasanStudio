/**
* Encapsulate the create of a flow
* This file is auto-generated by Ikasan Studio, do no edit.
*/
@org.springframework.stereotype.Component
public class Myflow1 {
@org.springframework.beans.factory.annotation.Value("${module.name}")
@org.springframework.beans.factory.annotation.Value("${ftp.consumer.cronExpression}")
java.lang.String  ftpConsumerCronexpression
@org.springframework.beans.factory.annotation.Value("${ftp.consumer.FilenamePattern}")
java.lang.String  ftpConsumerFilenamepattern
private String moduleName;

@javax.annotation.Resource
org.ikasan.builder.BuilderFactory builderFactory;

@org.springframework.context.annotation.Bean
public org.ikasan.spec.flow.Flow getMyflow1()
{
org.ikasan.builder.ModuleBuilder moduleBuilder = builderFactory.getModuleBuilder(moduleName);
org.ikasan.builder.FlowBuilder flowBuilder = moduleBuilder.getFlowBuilder("MyFlow1");
org.ikasan.builder.component.ComponentBuilder componentBuilder = builderFactory.getComponentBuilder();

org.ikasan.spec.flow.Flow myflow1 = flowBuilder
.consumer("testFtpConsumer",
componentBuilder.ftpConsumer()
.setCronExpression(ftpConsumerCronexpression)
.setFilenamePattern(ftpConsumerFilenamepattern)
.build())
.build();
return myflow1;
}
}