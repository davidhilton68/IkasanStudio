<#assign StudioUtils=statics['org.ikasan.studio.StudioUtils']>
# This file is auto-generated by Ikasan Studio, do no edit.
# Logging levels across packages (optional)
module.name=${module.name}
<#if module.getPropertyValue('ApplicationPackageName')??>
module.package=${module.getPropertyValue('ApplicationPackageName')}
</#if>

logging.level.com.arjuna=INFO
logging.level.org.springframework=INFO

# Blue console servlet settings (optional)
server.error.whitelabel.enabled=false

# Web Bindings
server.port=${module.getApplicationPortNumber()}
server.address=0.0.0.0
server.servlet.context-path=/${StudioUtils.toUrlString(module.getName())}

# Spring config
server.tomcat.additional-tld-skip-patterns=xercesImpl.jar,xml-apis.jar,serializer.jar
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration,org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration,me.snowdrop.boot.narayana.autoconfigure.NarayanaConfiguration,org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration
spring.liquibase.change-log=classpath:db-changelog-master.xml
spring.liquibase.enabled=true

# health probs and remote management (optional)
management.endpoints.web.expose=*
management.server.servlet.context-path=/manage
management.endpoint.shutdown.enabled=true

# Ikasan persistence store
datasource.username=sa
datasource.password=sa
datasource.driver-class-name=org.h2.Driver
datasource.xadriver-class-name=org.h2.jdbcx.JdbcDataSource
datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
datasource.dialect=org.hibernate.dialect.H2Dialect
datasource.show-sql=false
datasource.hbm2ddl.auto=none
datasource.validationQuery=select 1
# Dashboard data extraction settings
ikasan.dashboard.extract.enabled=false
ikasan.dashboard.extract.base.url=http://localhost:9080/ikasan-dashboard
ikasan.dashboard.extract.username=
ikasan.dashboard.extract.password=

<#list properties![] as property>
    ${property.getName()}=${property.getValue()}
</#list>
<#--properties for all components-->
<#compress>
<#list module.getFlows()![] as flow>
    <#list flow.getFlowComponentList()![] as ikasanComponent>
        <#if (ikasanComponent.getStandardConfiguredProperties())??>
            <#list ikasanComponent.getStandardConfiguredProperties() as propKey, propValue>
                <#if propValue.getMeta().getPropertyConfigFileLabel() != "" &&
                        propValue.getValue()?? &&
                        ! propValue.getMeta().getUserImplementedClass()>
<#--                    ${StudioUtils.toJavaIdentifier(propValue.valueString)}-->
<#--                    ${StudioUtils.getPropertyLabel(module, flow, ikasanComponent, propValue.meta.propertyConfigFileLabel)}=${propValue.getValue()}-->
                    ${StudioUtils.getPropertyLabelPackageStyle(module, flow, ikasanComponent, propValue.meta.propertyConfigFileLabel)}=${propValue.getValue()}
<#--                    <#if ikasanComponent.getJavaPackageName() != "">-->
<#--                        ${flow.getJavaPackageName()}.${ikasanComponent.getJavaPackageName()}.${propValue.getMeta().propertyConfigFileLabel}=${propValue.getValue()}-->
<#--                    <#else>-->
<#--                        ${flow.getJavaPackageName()}.${propValue.getMeta().propertyConfigFileLabel}=${propValue.getValue()}-->
<#--                    </#if>-->
                </#if>
            </#list>
        </#if>
    </#list>
</#list>
</#compress>