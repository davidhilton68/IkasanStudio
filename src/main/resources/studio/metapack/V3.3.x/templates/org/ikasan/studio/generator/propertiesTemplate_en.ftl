<#assign StudioBuildUtils=statics['org.ikasan.studio.core.StudioBuildUtils']>
# This file is auto-generated by Ikasan Studio, do no edit.
# Logging levels across packages (optional)
module.name=${module.name}
<#if module.getPropertyValue('applicationPackageName')??>
module.package=${module.getPropertyValue('applicationPackageName')}
</#if>

logging.level.com.arjuna=INFO
logging.level.org.springframework=INFO

# Blue console servlet settings (optional)
server.error.whitelabel.enabled=false

# Web Bindings
server.port=${module.getPort()}
h2.db.port=${module.getH2PortNumber()}
server.address=0.0.0.0
server.servlet.context-path=/${StudioBuildUtils.toUrlString(module.getName())}

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

#datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
#datasource.url=jdbc:h2:tcp://localhost:${r"${h2.db.port}"}/${r"${module.name}"}-db/esb;IFEXISTS=FALSE; COMPRESS=TRUE MAX OPERATION MEMORY=102400;CACHE_SIZE=16384;DB_CLOSE_ON_EXIT=FALSE
datasource.url=jdbc:h2:tcp://localhost:${r"${h2.db.port}"}/${r"${module.name}"}-db/esb;DB_CLOSE_DELAY=-1
datasource.dialect=org.hibernate.dialect.H2Dialect
datasource.show-sql=false
datasource.hbm2ddl.auto=none
datasource.validationQuery=select 1
datasource.min.pool.size=8
datasource.max.pool.size=25

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
    <#list flow.ftlGetConsumerAndFlowElements()![] as basicElement>
        <#if (basicElement.getStandardConfiguredProperties())??>
            <#list basicElement.getStandardConfiguredProperties() as componentPropertyName, componentProperty>
                <#if componentProperty?? && componentProperty.getMeta()?? &&
                        componentProperty.getMeta().getPropertyConfigFileLabel()?? &&
                        componentProperty.getMeta().getPropertyConfigFileLabel() != "" &&
                        componentProperty.getValue()?? &&
                        !componentProperty.getMeta().isUserSuppliedClass()>
                    ${StudioBuildUtils.getPropertyLabelPackageStyle(module, flow, basicElement, componentProperty.meta.propertyConfigFileLabel)}=${componentProperty.getValue()}
                </#if>
            </#list>
        </#if>
    </#list>
</#list>
</#compress>