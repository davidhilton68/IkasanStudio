package org.ikasan.studio;

import junit.framework.TestCase;
import org.ikasan.studio.model.ikasan.IkasanComponentPropertyMeta;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;

public class StudioUtilsTest {

    @Test
    public void test_get_last_token_with_multiple_tokens() {
        String actual = StudioUtils.getLastToken("\\.", "this.is.dot.delim");
        Assert.assertThat(actual, is("delim"));

        String actual2 = StudioUtils.getLastToken("\\.", "delim");
        Assert.assertThat(actual2, is("delim"));
    }

    @Test
    public void test_get_all_but_last_token_with_multiple_tokens() {
        String actual = StudioUtils.getAllButLastToken("\\.", "this.is.dot.delim");
        Assert.assertThat(actual, is("this.is.dot"));

        String actual2 = StudioUtils.getAllButLastToken("\\.", "this.delim");
        Assert.assertThat(actual2, is("this"));

        String actual3 = StudioUtils.getAllButLastToken("\\.", "delim");
        Assert.assertThat(actual3, is(""));
    }

    @Test
    public void toJavaClassName() {
        Assert.assertThat(StudioUtils.toJavaClassName(""), is(""));
        Assert.assertThat(StudioUtils.toJavaClassName("a"), is("A"));
        Assert.assertThat(StudioUtils.toJavaClassName("A"), is("A"));
        Assert.assertThat(StudioUtils.toJavaClassName("AS"), is("AS"));
        Assert.assertThat(StudioUtils.toJavaClassName("AS D"), is("ASD"));
        Assert.assertThat(StudioUtils.toJavaClassName("as d"), is("AsD"));
        Assert.assertThat(StudioUtils.toJavaClassName("as d    c"), is("AsDC"));
        Assert.assertThat(StudioUtils.toJavaClassName("Some Text"), is("SomeText"));
        Assert.assertThat(StudioUtils.toJavaClassName("my.package.name"), is("MyPackageName"));
    }

    @Test
    public void testToJavaIdentifier() {
        Assert.assertThat(StudioUtils.toJavaIdentifier(""), is(""));
        Assert.assertThat(StudioUtils.toJavaIdentifier("a"), is("a"));
        Assert.assertThat(StudioUtils.toJavaIdentifier("A"), is("a"));
        Assert.assertThat(StudioUtils.toJavaIdentifier("AS"), is("aS"));
        Assert.assertThat(StudioUtils.toJavaIdentifier("AS D"), is("aSD"));
        Assert.assertThat(StudioUtils.toJavaIdentifier("as d"), is("asD"));
        Assert.assertThat(StudioUtils.toJavaIdentifier("as d    c"), is("asDC"));
        Assert.assertThat(StudioUtils.toJavaIdentifier("Some Text"), is("someText"));
        Assert.assertThat(StudioUtils.toJavaIdentifier("my.package.name"), is("myPackageName"));
    }

    @Test
    public void testToJavaPackageName() {
        Assert.assertThat(StudioUtils.toJavaPackageName(""), is(""));
        Assert.assertThat(StudioUtils.toJavaPackageName("a"), is("a"));
        Assert.assertThat(StudioUtils.toJavaPackageName("A"), is("a"));
        Assert.assertThat(StudioUtils.toJavaPackageName("AS"), is("as"));
        Assert.assertThat(StudioUtils.toJavaPackageName("AS D"), is("asd"));
        Assert.assertThat(StudioUtils.toJavaPackageName("as d"), is("asd"));
        Assert.assertThat(StudioUtils.toJavaPackageName("as d    c"), is("asdc"));
        Assert.assertThat(StudioUtils.toJavaPackageName("Some 1 Text"), is("some1text"));
        Assert.assertThat(StudioUtils.toJavaPackageName("1test"), is("_1test"));
    }

    @Test
    public void testConfigReader() throws IOException {
        Map<String, IkasanComponentPropertyMeta>  properties = StudioUtils.readIkasanComponentProperties("BROKER");
        Assert.assertThat(properties.size(), is(5));
        IkasanComponentPropertyMeta additionalName = properties.get("AdditionalName");
        IkasanComponentPropertyMeta name = properties.get("Name");
        IkasanComponentPropertyMeta other = properties.get("Other");
        IkasanComponentPropertyMeta total = properties.get("Total");
        IkasanComponentPropertyMeta userImplementedClass = properties.get("UserImplementedClass");
        Assert.assertThat(additionalName.toString(), is(
            "IkasanComponentPropertyMeta{mandatory=true, userImplementedClass=false, userDefineResource=false, propertyName='AdditionalName', propertyConfigFileLabel='', propertyDataType=class java.lang.String, usageDataType=java.lang.String, validation=, defaultValue=MyDefault, helpText='The name of the component'}"));
        Assert.assertThat(name.toString(), is(
            "IkasanComponentPropertyMeta{mandatory=true, userImplementedClass=false, userDefineResource=false, propertyName='Name', propertyConfigFileLabel='null', propertyDataType=class java.lang.String, usageDataType=java.lang.String, validation=, defaultValue=, helpText='The name of the component as displayed on diagrams, space are encouraged, succinct is best. The name should be unique for the flow.'}"));
        Assert.assertThat(other.toString(), is(
            "IkasanComponentPropertyMeta{mandatory=false, userImplementedClass=false, userDefineResource=false, propertyName='Other', propertyConfigFileLabel='', propertyDataType=class java.lang.Integer, usageDataType=java.lang.Integer, validation=, defaultValue=null, helpText='Total description'}"));
        Assert.assertThat(total.toString(), is(
            "IkasanComponentPropertyMeta{mandatory=false, userImplementedClass=false, userDefineResource=false, propertyName='Total', propertyConfigFileLabel='my.test.total', propertyDataType=class java.lang.Integer, usageDataType=java.lang.Integer, validation=, defaultValue=2, helpText='Total description'}"));
        Assert.assertThat(userImplementedClass.toString(), is(
            "IkasanComponentPropertyMeta{mandatory=true, userImplementedClass=true, userDefineResource=true, propertyName='UserImplementedClass', propertyConfigFileLabel='', propertyDataType=class java.lang.Object, usageDataType=java.lang.Object, validation=, defaultValue=null, helpText='This type of class will be implemented by the user, typically implementing an Ikasan interface'}"));
    }
}