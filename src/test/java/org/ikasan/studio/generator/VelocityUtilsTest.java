package org.ikasan.studio.generator;

import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class VelocityUtilsTest extends LightJavaCodeInsightFixtureTestCase {

    @Test
    public void test_generateFromTemplate() throws IOException {
        Map<String, Object> configs = new HashMap<>();
        configs.putIfAbsent("className","Application");
        String templateString = VelocityUtils.generateFromTemplate("BasicVelocityTest.vm", configs);
        Assert.assertThat(templateString, is(notNullValue()));
        Assert.assertThat(templateString, is("Basic Test Application\n"));
    }
}