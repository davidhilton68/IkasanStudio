package org.ikasan.studio.generator;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import java.util.HashMap;
import java.util.Map;

/**
 * Currently both velocity and embedded strategy are being evaluated
 */
public class PropertiesTemplate extends Generator {
    public static String MODULE_PROPERTIES_FILENAME = "application";
    private static String MODULE_PROPERTIES_VM = "PropertiesTemplate.vm";

    public static void create(final Project project) {
        String templateString =  createPropertiesVelocity();
        createResourceFile(project, null, MODULE_PROPERTIES_FILENAME, templateString, false);
    }

    public static String createPropertiesVelocity() {
        Map<String, Object> configs = new HashMap<>();
        String templateString = VelocityUtils.generateFromTemplate(MODULE_PROPERTIES_VM, configs);
        return templateString;
    }
}
