package org.ikasan.studio.generator;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiJavaFile;
import org.ikasan.studio.Context;
import org.ikasan.studio.StudioUtils;
import org.ikasan.studio.model.Ikasan.IkasanFlow;
import org.ikasan.studio.model.Ikasan.IkasanModule;

import java.util.HashMap;
import java.util.Map;

/**
 * Currently both velocity and embedded strategy are being evaluated
 */
public class FlowTemplate extends Generator {
    public static String FLOW_CLASS_NAME = "Flow";
    private static String FLOW_VM = "FlowTemplate.vm";

    public static void create(final Project project) {
        IkasanModule ikasanModule = Context.getIkasanModule(project.getName());
        for (IkasanFlow ikasanFlow : ikasanModule.getFlows()) {
            String className = getFlowClassName(ikasanFlow);
            String templateString = generateContents(ikasanModule, ikasanFlow, className);
            PsiJavaFile newFile = createTemplateFile(project, STUDIO_BOOT_PACKAGE, className, templateString, true, true);
            ikasanFlow.getViewHandler().setPsiJavaFile(newFile);
        }
    }

    public static String generateContents(IkasanModule ikasanModule, IkasanFlow ikasanFow, String className) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(CLASS_NAME_TAG, className);
        configs.put(MODULE_TAG, ikasanModule);
        configs.put(FLOW_TAG, ikasanFow);
        String templateString = VelocityUtils.generateFromTemplate(FLOW_VM, configs);
        return templateString;
    }

    protected static String getFlowClassName(IkasanFlow ikasanFlow) {
        return StudioUtils.toJavaClassName(ikasanFlow.getName());
    }
}
