package org.ikasan.studio.actions;

import org.ikasan.studio.Context;
import org.ikasan.studio.model.StudioPsiUtils;
import org.ikasan.studio.model.ikasan.instance.BasicElement;
import org.ikasan.studio.model.ikasan.instance.Flow;
import org.ikasan.studio.model.ikasan.instance.FlowElement;
import org.ikasan.studio.model.ikasan.instance.Module;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteComponentAction implements ActionListener {
   private final String projectKey;
   BasicElement component;

   public DeleteComponentAction(String projectKey, BasicElement component) {
      this.projectKey = projectKey;
      this.component = component;
   }

   @Override
   public void actionPerformed(ActionEvent actionEvent) {
      if (component instanceof FlowElement ikasanFlowComponentToRemove) {
          Flow parentFlow = ikasanFlowComponentToRemove.getContainingFlow();
         if (parentFlow != null) {
            parentFlow.removeFlowElement(ikasanFlowComponentToRemove);
         }
         StudioPsiUtils.refreshCodeFromModelAndCauseRedraw(projectKey);
      } else if (component instanceof Flow ikasanFlowToRemove) {
          Module ikasanModule = Context.getIkasanModule(projectKey);
         ikasanModule.getFlows().remove(ikasanFlowToRemove);
         StudioPsiUtils.refreshCodeFromModelAndCauseRedraw(projectKey);
      }
   }
}
