package org.ikasan.studio.generator;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiJavaFile;
import org.ikasan.studio.model.ikasan.*;

import java.util.Map;

/**
 * Template to create the bespoke classes i.e. those with property for
 *      'BespokeClassName' e.g. Filter, Converter
 *      'Configuration'
 *
 * The type of template used depends on the component hence each component can have at most 1 BespokeClassName
 */
public class FlowsBespokeComponentTemplate extends Generator {

    public static void create(final Project project, final IkasanModule ikasanModule, final IkasanFlow ikasanFlow) {
        for (IkasanFlowComponent component : ikasanFlow.getFlowComponentList()) {
            if (component.hasUserImplementedClass()) {
                FlowsBespokePropertyTemplate.create(project, ikasanModule, ikasanFlow, component);
            }

            if (component instanceof IkasanFlowBeskpokeComponent && ((IkasanFlowBeskpokeComponent)component).isOverrideEnabled()) {
                String clazzName = (String)component.getProperty(IkasanComponentPropertyMeta.BESPOKE_CLASS_NAME).getValue();
                createSourceFile(clazzName, component, project, ikasanModule, ikasanFlow);
//                String newPackageName = GeneratorUtils.getBespokePackageName(ikasanModule, ikasanFlow);
//                String templateString = generateContents(newPackageName, component);
//                boolean overwriteClassIfExists = ((IkasanFlowBeskpokeComponent)component).isOverrideEnabled();
//                PsiJavaFile newFile = createTemplateFile(project, newPackageName, clazzName, templateString, true, overwriteClassIfExists);
//                ((IkasanFlowBeskpokeComponent)component).setOverrideEnabled(false);
//
//                component.getViewHandler().setPsiJavaFile(newFile);
            }

            if (component.getProperty(IkasanComponentPropertyMeta.CONFIGURATION) != null) {

            }

        }
    }

    private static void createSourceFile(String newClassName, IkasanFlowComponent component, Project project, IkasanModule ikasanModule, IkasanFlow ikasanFlow) {
        String clazzName = newClassName;
        String newPackageName = GeneratorUtils.getBespokePackageName(ikasanModule, ikasanFlow);
        String templateString = generateContents(newPackageName, component);
        boolean overwriteClassIfExists = ((IkasanFlowBeskpokeComponent)component).isOverrideEnabled();
        PsiJavaFile newFile = createTemplateFile(project, newPackageName, clazzName, templateString, true, overwriteClassIfExists);
        ((IkasanFlowBeskpokeComponent)component).setOverrideEnabled(false);

        component.getViewHandler().setPsiJavaFile(newFile);
    }

    public static String generateContents(String packageName, IkasanFlowComponent ikasanFlowComponent) {
        String templateName = ikasanFlowComponent.getType().getElementCategory().toString().toLowerCase() + "Template.ftl";
        Map<String, Object> configs = getBasicTemplateConfigs();
        configs.put(STUDIO_PACKAGE_TAG, packageName);
        configs.put(COMPONENT_TAG, ikasanFlowComponent);
        return FreemarkerUtils.generateFromTemplate(templateName, configs);
    }
}
