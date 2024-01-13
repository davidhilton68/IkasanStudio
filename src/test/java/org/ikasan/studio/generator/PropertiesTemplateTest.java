package org.ikasan.studio.generator;

import junit.framework.TestCase;
import org.ikasan.studio.model.ikasan.Flow;
import org.ikasan.studio.model.ikasan.Module;
import org.junit.Before;

public class PropertiesTemplateTest extends TestCase {
    Module testModule;
    Flow ikasanFlow;

    @Before
    public void setUp() {
        testModule = TestFixtures.getIkasanModule();
        testModule.setComponentName("myModule");
        ikasanFlow = new Flow();
        ikasanFlow.setComponentName("newFlow1");
        testModule.addFlow(ikasanFlow);
    }

    //@Test - suspend while this is being redeveloped
//    /**
//     * See also resources/studio/templates/org/ikasan/studio/generator/application_emptyFlow.properties
//     * @throws IOException if the template cant be generated
//     */
//    @Test
//    public void testCreateProperties_emptyFlow_with_non_default_port() throws IOException {
//        testModule.setApplicationPortNumber("8090");
//        testModule.setH2WebPortNumber("8091");
//        String templateString = PropertiesTemplate.generateContents(testModule);
//        Assert.assertThat(templateString, is(notNullValue()));
//        Assert.assertThat(templateString, is(GeneratorTestUtils.getExptectedFreemarkerOutputFromTestFile(PropertiesTemplate.MODULE_PROPERTIES_FILENAME + "_emptyFlow.properties")));
//    }
//
//    /**
//     * See also resources/studio/templates/org/ikasan/studio/generator/application_fullyPopulatedFtpConsumerComponent.properties
//     * @throws IOException if the template cant be generated
//     */
//    @Test
//    public void testCreateProperties_fullyPopulatedFtpConsumerComponent() throws IOException {
//        ikasanFlow.getFlowComponentList().add(TestFixtures.getFullyPopulatedFtpConsumerComponent(ikasanFlow));
//
//        String templateString = PropertiesTemplate.generateContents(testModule);
//        Assert.assertThat(templateString, is(notNullValue()));
//        Assert.assertThat(templateString, is(GeneratorTestUtils.getExptectedFreemarkerOutputFromTestFile(PropertiesTemplate.MODULE_PROPERTIES_FILENAME + "_fullyPopulatedFtpConsumerComponent.properties")));
//    }
//
//    /**
//     * See also resources/studio/templates/org/ikasan/studio/generator/application_fullyPopulatedFtpProducerComponent.properties
//     * @throws IOException if the template cant be generated
//     */
//    @Test
//    public void testCreateProperties_fullyPopulatedFtpProducerComponent() throws IOException {
//        ikasanFlow.getFlowComponentList().add(TestFixtures.getFullyPopulatedFtpProducerComponent(ikasanFlow));
//
//        String templateString = PropertiesTemplate.generateContents(testModule);
//        Assert.assertThat(templateString, is(notNullValue()));
//        Assert.assertThat(templateString, is(GeneratorTestUtils.getExptectedFreemarkerOutputFromTestFile(PropertiesTemplate.MODULE_PROPERTIES_FILENAME + "_fullyPopulatedFtpProducerComponent.properties")));
//    }
//
//    /**
//     * See also resources/studio/templates/org/ikasan/studio/generator/application_fullyPopulatedSftpConsumerComponent.properties
//     * @throws IOException if the template cant be generated
//     */
//    @Test
//    public void testCreateProperties_fullyPopulatedSftpConsumerComponent() throws IOException {
//        ikasanFlow.getFlowComponentList().add(TestFixtures.getFullyPopulatedSftpConsumerComponent(ikasanFlow));
//
//        String templateString = PropertiesTemplate.generateContents(testModule);
//        Assert.assertThat(templateString, is(notNullValue()));
//        Assert.assertThat(templateString, is(GeneratorTestUtils.getExptectedFreemarkerOutputFromTestFile(PropertiesTemplate.MODULE_PROPERTIES_FILENAME + "_fullyPopulatedSftpConsumerComponent.properties")));
//    }
//
//    /**
//     * See also resources/studio/templates/org/ikasan/studio/generator/application_fullyPopulatedSftpProducerComponent.properties
//     * @throws IOException if the template cant be generated
//     */
//    @Test
//    public void testCreateProperties_fullyPopulatedSftpProducerComponent() throws IOException {
//        ikasanFlow.getFlowComponentList().add(TestFixtures.getFullyPopulatedSftpProducerComponent(ikasanFlow));
//
//        String templateString = PropertiesTemplate.generateContents(testModule);
//        Assert.assertThat(templateString, is(notNullValue()));
//        Assert.assertThat(templateString, is(GeneratorTestUtils.getExptectedFreemarkerOutputFromTestFile(PropertiesTemplate.MODULE_PROPERTIES_FILENAME + "_fullyPopulatedSftpProducerComponent.properties")));
//    }
//
//    /**
//     * See also resources/studio/templates/org/ikasan/studio/generator/application_fullyPopulatedLocalFileConsumerComponent.properties
//     * @throws IOException if the template cant be generated
//     */
//    @Test
//    public void testCreateProperties_fullyPopulatedLocalFileConsumerComponent() throws IOException {
//        ikasanFlow.getFlowComponentList().add(TestFixtures.getFullyPopulatedLocalFileConsumerComponent(ikasanFlow));
//
//        String templateString = PropertiesTemplate.generateContents(testModule);
//        Assert.assertThat(templateString, is(notNullValue()));
//        Assert.assertThat(templateString, is(GeneratorTestUtils.getExptectedFreemarkerOutputFromTestFile(PropertiesTemplate.MODULE_PROPERTIES_FILENAME + "_fullyPopulatedLocalFileConsumerComponent.properties")));
//    }
//
//    /**
//     * See also resources/studio/templates/org/ikasan/studio/generator/application_fullyPopulatedScheduledConsumerComponent.properties
//     * @throws IOException if the template cant be generated
//     */
//    @Test
//    public void testCreateProperties_fullyPopulatedScheduledConsumerComponent() throws IOException {
//        ikasanFlow.getFlowComponentList().add(TestFixtures.getFullyPopulatedScheduledConsumerComponent(ikasanFlow));
//
//        String templateString = PropertiesTemplate.generateContents(testModule);
//        Assert.assertThat(templateString, is(notNullValue()));
//        Assert.assertThat(templateString, is(GeneratorTestUtils.getExptectedFreemarkerOutputFromTestFile(PropertiesTemplate.MODULE_PROPERTIES_FILENAME + "_fullyPopulatedScheduledConsumerComponent.properties")));
//    }
//
//    /**
//     * See also resources/studio/templates/org/ikasan/studio/generator/application_fullyPopulatedSpringJmsConsumerComponent.properties
//     * @throws IOException if the template cant be generated
//     */
//    @Test
//    public void testCreateProperties_fullyPopulatedJmsConsumerComponent() throws IOException {
//        ikasanFlow.getFlowComponentList().add(TestFixtures.getFullyPopulatedSpringJmsConsumerComponent(ikasanFlow));
//
//        String templateString = PropertiesTemplate.generateContents(testModule);
//        Assert.assertThat(templateString, is(notNullValue()));
//        Assert.assertThat(templateString, is(GeneratorTestUtils.getExptectedFreemarkerOutputFromTestFile(PropertiesTemplate.MODULE_PROPERTIES_FILENAME + "_fullyPopulatedSpringJmsConsumerComponent.properties")));
//    }
//
//     /**
//     * See also resources/studio/templates/org/ikasan/studio/generator/application_fullyPopulatedJmsProducerComponent.properties
//     * @throws IOException if the template cant be generated
//     */
//    @Test
//    public void testCreateProperties_fullyPopulatedJmsProducerComponent() throws IOException {
//        ikasanFlow.getFlowComponentList().add(TestFixtures.getFullyPopulatedJmsProducerComponent(ikasanFlow));
//
//        String templateString = PropertiesTemplate.generateContents(testModule);
//        Assert.assertThat(templateString, is(notNullValue()));
//        Assert.assertThat(templateString, is(GeneratorTestUtils.getExptectedFreemarkerOutputFromTestFile(PropertiesTemplate.MODULE_PROPERTIES_FILENAME + "_fullyPopulatedJmsProducerComponent.properties")));
//    }
//
//    /**
//     * See also resources/studio/templates/org/ikasan/studio/generator/application_fullyPopulatedDevNullProducerComponent.properties
//     * @throws IOException if the template cant be generated
//     */
//    @Test
//    public void testCreateProperties_fullyPopulatedDevNullProducerComponent() throws IOException {
//        ikasanFlow.getFlowComponentList().add(TestFixtures.getFullyPopulatedDevNullProducerComponent(ikasanFlow));
//
//        String templateString = PropertiesTemplate.generateContents(testModule);
//        Assert.assertThat(templateString, is(notNullValue()));
//        Assert.assertThat(templateString, is(GeneratorTestUtils.getExptectedFreemarkerOutputFromTestFile(PropertiesTemplate.MODULE_PROPERTIES_FILENAME + "_fullyPopulatedDevNullProducerComponent.properties")));
//    }
//
//    /**
//     * See also resources/studio/templates/org/ikasan/studio/generator/application_fullyPopulatedEmailProducerComponent.properties
//     * @throws IOException if the template cant be generated
//     */
//    @Test
//    public void testCreateProperties_fullyPopulatedEmailProducerComponent() throws IOException {
//        ikasanFlow.getFlowComponentList().add(TestFixtures.getFullyPopulatedEmailProducerComponent(ikasanFlow));
//
//        String templateString = PropertiesTemplate.generateContents(testModule);
//        Assert.assertThat(templateString, is(notNullValue()));
//        Assert.assertThat(templateString, is(GeneratorTestUtils.getExptectedFreemarkerOutputFromTestFile(PropertiesTemplate.MODULE_PROPERTIES_FILENAME + "_fullyPopulatedEmailProducerComponent.properties")));
//    }
//
//    /**
//     * See also resources/studio/templates/org/ikasan/studio/generator/application_fullyPopulatedObjectMessageToXmlStringConverterComponent.properties
//     * @throws IOException if the template cant be generated
//     */
//    @Test
//    public void testCreateProperties_fullyPopulatedObjectMessageToXmlStringConverterComponent() throws IOException {
//        ikasanFlow.getFlowComponentList().add(TestFixtures.getFullyPopulatedObjectMessageToXmlStringConverterComponent(ikasanFlow));
//
//        String templateString = PropertiesTemplate.generateContents(testModule);
//        Assert.assertThat(templateString, is(notNullValue()));
//        Assert.assertThat(templateString, is(GeneratorTestUtils.getExptectedFreemarkerOutputFromTestFile(PropertiesTemplate.MODULE_PROPERTIES_FILENAME + "_fullyPopulatedObjectMessageToXmlStringConverterComponent.properties")));
//    }
//
//    /**
//     * See also resources/studio/templates/org/ikasan/studio/generator/application_fullyPopulatedXmlStringObjectMessageConverterComponent.properties
//     * @throws IOException if the template cant be generated
//     */
//    @Test
//    public void testCreateProperties_fullyPopulatedXmlStringObjectMessageConverterComponent() throws IOException {
//        ikasanFlow.getFlowComponentList().add(TestFixtures.getFullyPopulatedXmlStringObjectMessageConverterComponent(ikasanFlow));
//
//        String templateString = PropertiesTemplate.generateContents(testModule);
//        Assert.assertThat(templateString, is(notNullValue()));
//        Assert.assertThat(templateString, is(GeneratorTestUtils.getExptectedFreemarkerOutputFromTestFile(PropertiesTemplate.MODULE_PROPERTIES_FILENAME + "_fullyPopulatedXmlStringObjectMessageConverterComponent.properties")));
//    }

}