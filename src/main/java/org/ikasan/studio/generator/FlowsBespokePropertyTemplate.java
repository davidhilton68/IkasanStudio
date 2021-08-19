package org.ikasan.studio.generator;

import com.intellij.openapi.project.Project;
import org.ikasan.studio.StudioUtils;
import org.ikasan.studio.model.ikasan.IkasanComponentProperty;
import org.ikasan.studio.model.ikasan.IkasanFlow;
import org.ikasan.studio.model.ikasan.IkasanFlowComponent;
import org.ikasan.studio.model.ikasan.IkasanModule;

import java.util.Map;

public class FlowsBespokePropertyTemplate extends Generator {

    public static void create(final Project project, final IkasanModule ikasanModule, final IkasanFlow ikasanFlow, IkasanFlowComponent component) {
        for (IkasanComponentProperty property : component.getUserImplementedClassProperties()) {
            String newPackageName = GeneratorUtils.getBespokePackageName(ikasanModule, ikasanFlow);
            String clazzName = StudioUtils.toJavaClassName(property.getValueString());
            String templateString = generateContents(newPackageName, clazzName, property);
            createTemplateFile(project, newPackageName, clazzName, templateString, true, true);
            property.setRegenerateAllowed(false);
        }
    }

    public static String generateContents(String packageName, String clazzName, IkasanComponentProperty property) {
        String interfaceName = property.getMeta().getUsageDataType();
        String templateName = "genericInterfaceTemplate_en.ftl";
        Map<String, Object> configs = getBasicTemplateConfigs();
        configs.put(STUDIO_PACKAGE_TAG, packageName);
        configs.put(CLASS_NAME_TAG, clazzName);
        configs.put(INTERFACE_NAME_TAG, interfaceName);
        return FreemarkerUtils.generateFromTemplate(templateName, configs);
    }
}
