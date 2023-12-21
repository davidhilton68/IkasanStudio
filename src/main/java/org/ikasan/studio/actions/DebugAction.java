package org.ikasan.studio.actions;

import com.intellij.openapi.diagnostic.Logger;
import org.ikasan.studio.Context;
import org.ikasan.studio.StudioUtils;
import org.ikasan.studio.model.ikasan.IkasanModule;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DebugAction implements ActionListener {
    private static final Logger LOG = Logger.getInstance("#DebugAction");
    private final String projectKey;

    public DebugAction(String projectKey) {
        this.projectKey = projectKey;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        IkasanModule module = Context.getIkasanModule(projectKey);
        LOG.info("ikasan module was " + StudioUtils.toJson(module));
    }
}
