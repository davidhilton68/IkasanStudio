package org.ikasan.studio.generator;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiJavaFile;
import org.ikasan.studio.Context;
import org.ikasan.studio.model.Ikasan.IkasanModule;

import java.util.HashMap;
import java.util.Map;

/**
 * Currently both velocity and embedded strategy are being evaluated
 */
public class ModuleConfigTemplate extends Generator {
    public static String MODULE_CLASS_NAME = "ModuleConfig";
    private static String MODULE_VM = "ModuleConfigTemplate.vm";
    private static String MODULE_CLASS_IMPL =
            "/**\n" +
            " * This is the Spring book start class.\n" +
            " * This file is auto-generated by Ikasan Studio, do no edit..\n" +
            " */\n" +
            "@org.springframework.context.annotation.Configuration(\"ModuleFactory\")" +
                    "@org.springframework.context.annotation.ImportResource({\"classpath:h2-datasource-conf.xml\"})" +
                    "public class ModuleConfig {}";

    public static PsiJavaFile createModule(final Project project) {
        String templateString = "";
        if (GENERATOR_STRATEGY == GeneratorStrategy.VELOCITY) {
            IkasanModule ikasanModule = Context.getIkasanModule(project.getName());
            templateString = createModuleVelocity(ikasanModule);
        } else {
            templateString = MODULE_CLASS_IMPL;
        }
        PsiJavaFile newFile = createTemplateFile(project, DEFAULT_STUDIO_PACKAGE, MODULE_CLASS_NAME, templateString, true);
        return newFile;
    }

    public static String createModuleVelocity(IkasanModule ikasanModule) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(CLASS_NAME_TAG, MODULE_CLASS_NAME);
        configs.put(FLOWS_TAG, ikasanModule.getFlows());
        String templateString = VelocityUtils.generateFromTemplate(MODULE_VM, configs);
        return templateString;
    }
}
