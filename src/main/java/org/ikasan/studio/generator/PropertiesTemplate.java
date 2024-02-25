package org.ikasan.studio.generator;

import com.intellij.openapi.project.Project;
import org.ikasan.studio.Context;
import org.ikasan.studio.model.ikasan.instance.Module;

import java.util.Map;

public class PropertiesTemplate extends Generator {
    public static final String MODULE_PROPERTIES_FILENAME = "application";
    public static final String MODULE_PROPERTIES_FILENAME_WITH_EXTENSION = "application.properties";
    private static final String MODULE_PROPERTIES_FTL = "propertiesTemplate.ftl";

    public static void create(final Project project) {
        Module ikasanModule = Context.getIkasanModule(project.getName());
        String templateString = generateContents(ikasanModule);

        createResourceFile(project, null, MODULE_PROPERTIES_FILENAME_WITH_EXTENSION, templateString, false);
    }

    //@todo it might be more efficient to have 1 properties file per flow
    protected static String generateContents(Module ikasanModule) {
        Map<String, Object> configs = getBasicTemplateConfigs();
        configs.put(MODULE_TAG, ikasanModule);
        return FreemarkerUtils.generateFromTemplate(MODULE_PROPERTIES_FTL, configs);
    }
}
