package org.ikasan.studio.generator;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiJavaFile;
import org.ikasan.studio.StudioUtils;
import org.ikasan.studio.model.ikasan.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowsBespokePropertyTemplate extends Generator {

    public static void create(final Project project, final IkasanModule ikasanModule, final IkasanFlow ikasanFlow, IkasanFlowComponent component) {
        for (IkasanComponentProperty property : component.userImplementedClassProperties()) {
            boolean overwriteClassIfExists = true;

            String newPackageName = GeneratorUtils.getBespokePackageName(ikasanModule, ikasanFlow);
            String clazzName = StudioUtils.toJavaClassName(property.getValueString());
            String templateString = generateContents(newPackageName, clazzName, property);

            PsiJavaFile newFile = createTemplateFile(project, newPackageName, clazzName, templateString, true, overwriteClassIfExists);
        }
    }

    public static String generateContents(String packageName, String clazzName, IkasanComponentProperty property) {
        String interfaceName = property.getMeta().getUsageDataType();
        String templateName = "GenericInterfaceTemplate_en_GB.ftl";
        Map<String, Object> configs = getBasicTemplateConfigs();
        configs.put(STUDIO_PACKAGE_TAG, packageName);
        configs.put(CLASS_NAME_TAG, clazzName);
        configs.put(INTERFACE_NAME_TAG, interfaceName);
        String templateString = FreemarkerUtils.generateFromTemplate(templateName, configs);
        return templateString;
    }
}
