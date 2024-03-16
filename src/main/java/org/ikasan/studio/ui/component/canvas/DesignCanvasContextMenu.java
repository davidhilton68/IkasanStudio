package org.ikasan.studio.ui.component.canvas;

import com.intellij.openapi.diagnostic.Logger;
import org.ikasan.studio.core.model.ikasan.instance.BasicElement;
import org.ikasan.studio.ui.actions.*;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class DesignCanvasContextMenu {
    public static final Logger LOG = Logger.getInstance("DesignCanvasContextMenu");

    // Enforce as utility clASS
    private DesignCanvasContextMenu () {}

    public static void showPopupAndNavigateMenu(String projectKey, DesignerCanvas designerCanvas, MouseEvent mouseEvent, BasicElement component) {
        JPopupMenu menu = new JPopupMenu();
        menu.add(createDeleteComponentMenuItem(projectKey, "Delete Component", component));
        menu.add(createSaveAsMenuItem(projectKey, "Save Image"));
        menu.add(createRefreshMenuItem(projectKey, "Refresh Model"));
        menu.add(createLaunchDashboardMenuItem(projectKey, "Launch Browser"));
        menu.add(createLaunchH2MenuItem(projectKey, "Launch H2"));
        menu.add(createHelpTextItem(projectKey, "Description", component, mouseEvent));
        menu.add(createWebHelpTextItem(projectKey, "Web help", component, mouseEvent));
        menu.add(createNavigateToCode(projectKey, "Jump to code", component, false));
        menu.add(createNavigateToCode(projectKey, "Jump to code line", component, true));
        menu.show(designerCanvas, mouseEvent.getX(), mouseEvent.getY());
    }

    public static void showPopupMenu(String projectKey, DesignerCanvas designerCanvas, MouseEvent event) {
        JPopupMenu menu = new JPopupMenu();
        menu.add(createSaveAsMenuItem(projectKey, "Save Image"));
        menu.add(createRefreshMenuItem(projectKey, "Refresh Model"));
        menu.add(createLaunchDashboardMenuItem(projectKey, "Launch Browser"));
        menu.add(createLaunchH2MenuItem(projectKey, "Launch H2"));
        menu.show(designerCanvas, event.getX(), event.getY());
    }

    private static JMenuItem createDeleteComponentMenuItem(String projectKey, String label, BasicElement component) {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(new DeleteComponentAction(projectKey, component));
        return item;
    }

    private static JMenuItem createHelpTextItem(String projectKey, String label, BasicElement component, MouseEvent mouseEvent) {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(new PopupHelpAction(projectKey, component, mouseEvent, false));
        return item;
    }

    private static JMenuItem createWebHelpTextItem(String projectKey, String label, BasicElement component, MouseEvent mouseEvent) {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(new PopupHelpAction(projectKey, component, mouseEvent, true));
        return item;
    }

    private static JMenuItem createSaveAsMenuItem(String projectKey, String label) {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(new SaveAction(projectKey));
        return item;
    }

    private static JMenuItem createRefreshMenuItem(String projectKey, String label) {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(new ModelRefreshAction(projectKey));
        return item;
    }

    private static JMenuItem createLaunchDashboardMenuItem(String projectKey, String label) {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(new LaunchDashboardAction(projectKey));
        return item;
    }

    private static JMenuItem createLaunchH2MenuItem(String projectKey, String label) {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(new LaunchH2Action(projectKey));
        return item;
    }

    private static JMenuItem createNavigateToCode(String projectKey, String label, BasicElement component, boolean jumpToLine) {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(new NavigateToCodeAction(projectKey, component, jumpToLine));
        return item;
    }
}

