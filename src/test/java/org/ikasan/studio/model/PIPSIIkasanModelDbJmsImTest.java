package org.ikasan.studio.model;

import com.intellij.psi.PsiClass;
import com.intellij.psi.search.ProjectScope;
import org.ikasan.studio.Context;
import org.ikasan.studio.model.Ikasan.IkasanFlow;
import org.ikasan.studio.model.Ikasan.IkasanModule;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class PIPSIIkasanModelDbJmsImTest extends PIPSIIkasanModelAbstractTest {
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
        return "/dbJmsIm/";
    }
    @Test
    public void test_parse_of_dbJmsIm_standard_module() {
//        final PsiMethod moduleConfigPsiFile = StudioPsiUtils.findFirstMethodByReturnType(myProject, "Module");
        IkasanModule ikasanModule = Context.getIkasanModule(TEST_PROJECT_KEY);
        final PsiClass moduleConfigClass = myJavaFacade.findClass("com.ikasan.sample.spring.boot.ModuleFactory", ProjectScope.getAllScope(myProject));
        Assert.assertThat(moduleConfigClass, is(notNullValue()));
        pipsiIkasanModel.setModuleConfigClazz(moduleConfigClass);
        pipsiIkasanModel.updateIkasanModule();

//
//        final PsiClass moduleConfigClass = myJavaFacade.findClass("com.ikasan.sample.spring.boot.ModuleFactory", ProjectScope.getAllScope(myProject));
//        moduleConfigPsiFile = moduleConfigClass.getContainingFile();
//
//        IkasanModule ikasanModule = pipsiIkasanModel.buildIkasanModule(moduleConfigPsiFile);
        Assert.assertThat(ikasanModule.getName(), is("myIntegrationModule"));
        Assert.assertThat(ikasanModule.getDescription(), is("Sample DB consumer / producer module."));
        Assert.assertThat(ikasanModule.getFlows().size(), is(2));
        Assert.assertThat(ikasanModule.getViewHandler(), is(notNullValue()));

        List<IkasanFlow> flows = ikasanModule.getFlows();

        {   // scope protection
            IkasanFlow flow1 = flows.get(0);
            Assert.assertThat(flow1.getViewHandler(), is(notNullValue()));
            Assert.assertThat(flow1.getName(), is("dbToJMSFlow"));
            Assert.assertThat(flow1.getDescription(), is("Sample DB to JMS flow"));
            Assert.assertThat(flow1.getOutput().getDescription(), is("jms.topic.test"));

            Assert.assertThat(flow1.getFlowComponentList().size(), is(5));
            Assert.assertThat(flow1.getFlowComponentList().get(0).getName(), is("DB Consumer"));
            Assert.assertThat(flow1.getFlowComponentList().get(0).getProperties().size(), is(3));
            Assert.assertThat(flow1.getFlowComponentList().get(1).getName(), is("My Filter"));
            Assert.assertThat(flow1.getFlowComponentList().get(1).getProperties().size(), is(2));
            Assert.assertThat(flow1.getFlowComponentList().get(2).getName(), is("Split list"));
            Assert.assertThat(flow1.getFlowComponentList().get(2).getProperties().size(), is(0));
            Assert.assertThat(flow1.getFlowComponentList().get(3).getName(), is("Person to XML"));
            Assert.assertThat(flow1.getFlowComponentList().get(3).getProperties().size(), is(1));
            Assert.assertThat(flow1.getFlowComponentList().get(4).getName(), is("JMS Producer"));
            Assert.assertThat(flow1.getFlowComponentList().get(4).getProperties().size(), is(11));
        }
        {   // scope protection
            IkasanFlow flow2 = flows.get(1);
            Assert.assertThat(flow2.getViewHandler(), is(notNullValue()));
            Assert.assertThat(flow2.getName(), is("jmsToDbFlow"));
            Assert.assertThat(flow2.getDescription(), is("Sample JMS to DB flow"));

            Assert.assertThat(flow2.getInput().getDescription(), is("jms.topic.test"));
            Assert.assertThat(flow2.getFlowComponentList().size(), is(3));
            Assert.assertThat(flow2.getFlowComponentList().get(0).getName(), is("JMS Consumer"));
            Assert.assertThat(flow2.getFlowComponentList().get(0).getProperties().size(), is(14));
            Assert.assertThat(flow2.getFlowComponentList().get(1).getName(), is("XML to Person"));
            Assert.assertThat(flow2.getFlowComponentList().get(1).getProperties().size(), is(1));
            Assert.assertThat(flow2.getFlowComponentList().get(2).getName(), is("DB Producer"));
            Assert.assertThat(flow2.getFlowComponentList().get(2).getProperties().size(), is(0));
        }
    }

}