package org.ikasan.studio.ui;

import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import org.ikasan.studio.Context;
import org.ikasan.studio.model.StudioPsiUtils;
import org.ikasan.studio.model.ikasan.IkasanModule;
import org.ikasan.studio.model.psi.PIPSIIkasanModel;
import org.ikasan.studio.ui.component.canvas.CanvasPanel;
import org.ikasan.studio.ui.component.canvas.DesignerCanvas;
import org.ikasan.studio.ui.component.palette.PalettePanel;
import org.ikasan.studio.ui.component.properties.PropertiesPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Create all onscreen components and register inter-thread communication components with Context
 */
public class DesignerUI {
    private String projectKey;
    private Project project;
    private JPanel mainJPanel = new JPanel();

    /**
     * Create the main Designer window
     * @param toolWindow is the Intellij frame in which this resides
     * @param project is the Java project
     */
    public DesignerUI(ToolWindow toolWindow, Project project) {
        this.project = project;
        this.projectKey = project.getName();
        Context.setProject(projectKey, project);
        if (Context.getIkasanModule(projectKey) == null) {
            Context.setIkasanModule(projectKey, new IkasanModule());
        }
        if (Context.getPipsiIkasanModel(projectKey) == null) {
            Context.setPipsiIkasanModel(projectKey, new PIPSIIkasanModel(projectKey));
        }

        PropertiesPanel propertiesPanel = new PropertiesPanel(projectKey, false);
        Context.setPropertiesPanel(projectKey,propertiesPanel);

        JSplitPane propertiesAndCanvasSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                propertiesPanel,
                new CanvasPanel(projectKey));
        propertiesAndCanvasSplitPane.setDividerSize(3);
        propertiesAndCanvasSplitPane.setDividerLocation(0.4);
        Context.setPropertiesAndCanvasPane(projectKey, propertiesAndCanvasSplitPane);

        mainJPanel.setLayout(new BorderLayout());
        mainJPanel.add(propertiesAndCanvasSplitPane, BorderLayout.CENTER);
        mainJPanel.add(new PalettePanel(projectKey), BorderLayout.EAST);
    }

    public JPanel getContent() {
        return mainJPanel;
    }

    /**
     * This will populate the canvas as soon as the indexing service has completed
     * Note, it may result in an IndexNotReadyException but seems tro retry successfully.
     */
    public void initialiseIkasanModel() {
        DumbService dumbService = DumbService.getInstance(project);
        dumbService.runWhenSmart(() -> {
            DesignerCanvas canvasPanel = Context.getDesignerCanvas(projectKey);
            if (canvasPanel != null) {
                StudioPsiUtils.generateModelFromSourceCode(projectKey, false);
            }
        });
    }
}
