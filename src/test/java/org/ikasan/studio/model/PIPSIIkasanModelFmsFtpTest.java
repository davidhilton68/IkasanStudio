package org.ikasan.studio.model;

import com.intellij.psi.PsiClass;
import com.intellij.psi.search.ProjectScope;
import org.hamcrest.Matchers;
import org.ikasan.studio.Context;
import org.ikasan.studio.model.ikasan.IkasanFlow;
import org.ikasan.studio.model.ikasan.IkasanFlowComponent;
import org.ikasan.studio.model.ikasan.IkasanModule;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.ikasan.studio.generator.TestUtils.getConfiguredPropertyValues;

public class PIPSIIkasanModelFmsFtpTest extends PIPSIIkasanModelAbstractTest {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    // This callback will set the path to the correct data file used in the test
    public String getTestDataDir() {
        return "/ikasanStandardSampleApps/fmsFtp/";
    }
    @Test
    public void test_parse_of_FmsFtp_standard_module() {
        IkasanModule ikasanModule = Context.getIkasanModule(TEST_PROJECT_KEY);
        final PsiClass moduleConfigClass = myJavaFacade.findClass("com.ikasan.sample.spring.boot.ModuleConfig", ProjectScope.getAllScope(myProject));
        Assert.assertThat(moduleConfigClass, is(notNullValue()));
        pipsiIkasanModel.setModuleConfigClazz(moduleConfigClass);
        pipsiIkasanModel.updateIkasanModule();

//
//        final PsiClass moduleConfigClass = myJavaFacade.findClass("com.ikasan.sample.spring.boot.ModuleConfig", ProjectScope.getAllScope(myProject));
//        moduleConfigPsiFile = moduleConfigClass.getContainingFile();
//
//        IkasanModule ikasanModule = pipsiIkasanModel.buildIkasanModule(moduleConfigPsiFile);
        Assert.assertThat(ikasanModule.getName(), is("fms-ftp"));
        Assert.assertThat(ikasanModule.getDescription(), is("Ftp Jms Sample Module"));
        Assert.assertThat(ikasanModule.getFlows().size(), is(2));
        Assert.assertThat(ikasanModule.getViewHandler(), is(notNullValue()));

        List<IkasanFlow> flows = ikasanModule.getFlows();
        IkasanFlow flow1 = flows.get(0);

        Assert.assertThat(flow1.getViewHandler(), is(notNullValue()));
        Assert.assertThat(flow1.getDescription(), is("Ftp to Jms"));
        Assert.assertThat(flow1.getName(), is("sourceFlow"));

        Assert.assertThat(flow1.getFlowComponentList().size(), is(3));

        IkasanFlowComponent ftpConsumer = flow1.getFlowComponentList().get(0);
        Assert.assertThat(ftpConsumer.getName(), is("Ftp Consumer"));
        Assert.assertThat(getConfiguredPropertyValues(ftpConsumer.getConfiguredProperties()),
                Matchers.is("AgeOfFiles->30,Chronological->true,Chunking->false,ClientID->null,ConfiguredResourceId->configuredResourceId,CronExpression->null," +
                        "Destructive->false,FilenamePattern->null,FilterDuplicates->true,FilterOnLastModifiedDate->true,MinAge->1l,Name->Ftp Consumer,Password->null," +
                        "RemoteHost->null,RemotePort->null,RenameOnSuccess->false,RenameOnSuccessExtension->.tmp,ScheduledJobGroupName->FtpToLogFlow,ScheduledJobName->FtpConsumer,SourceDirectory->null,Username->null"));

        IkasanFlowComponent payloadToMap = flow1.getFlowComponentList().get(1);
        Assert.assertThat(payloadToMap.getName(), is("Ftp Payload to Map Converter"));
        Assert.assertThat(getConfiguredPropertyValues(payloadToMap.getConfiguredProperties()),
                Matchers.is("Name->Ftp Payload to Map Converter"));

        IkasanFlowComponent jmsProducer = flow1.getFlowComponentList().get(2);
        Assert.assertThat(jmsProducer.getName(), is("Ftp Jms Producer"));
        Assert.assertThat(getConfiguredPropertyValues(jmsProducer.getConfiguredProperties()),
                Matchers.is("ConfiguredResourceId->ftpJmsProducer,ConnectionFactory->producerConnectionFactory,ConnectionFactoryJndiPropertyFactoryInitial->org.apache.activemq.jndi.ActiveMQInitialContextFactory," +
                        "ConnectionFactoryName->ConnectionFactory,ConnectionFactoryPassword->admin,ConnectionFactoryUsername->admin,DestinationJndiName->ftp.private.jms.queue,Name->Ftp Jms Producer"));

        IkasanFlow flow2 = flows.get(1);
        Assert.assertThat(flow2.getViewHandler(), is(notNullValue()));
        Assert.assertThat(flow2.getName(), is("targetFlow"));
        Assert.assertThat(flow2.getDescription(), is("Receives Text Jms message and sends it to FTP as file"));

        Assert.assertThat(flow2.getFlowComponentList().size(), is(3));

        IkasanFlowComponent jmsConsumer = flow2.getFlowComponentList().get(0);
        Assert.assertThat(jmsConsumer.getName(), is("Ftp Jms Consumer"));
        Assert.assertThat(getConfiguredPropertyValues(jmsConsumer.getConfiguredProperties()),
                Matchers.is("ConfiguredResourceId->ftpJmsConsumer,ConnectionFactory->consumerConnectionFactory,DestinationJndiName->ftp.private.jms.queue,Name->Ftp Jms Consumer"));

        IkasanFlowComponent mapToPayload = flow2.getFlowComponentList().get(1);
        Assert.assertThat(mapToPayload.getName(), is("MapMessage to FTP Payload Converter"));
        Assert.assertThat(getConfiguredPropertyValues(mapToPayload.getConfiguredProperties()),
                Matchers.is("Name->MapMessage to FTP Payload Converter"));

        IkasanFlowComponent ftpProdcuer = flow2.getFlowComponentList().get(2);
        Assert.assertThat(ftpProdcuer.getName(), is("Ftp Producer"));
        Assert.assertThat(getConfiguredPropertyValues(ftpProdcuer.getConfiguredProperties()),
                Matchers.is("ClientID->null,ConfiguredResourceId->ftpProducerConfiguration,Name->Ftp Producer,OutputDirectory->null,Overwrite->true,Password->null,RemoteHost->null,RemotePort->null,Username->null"));
    }
}