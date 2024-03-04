package org.ikasan.studio.ui.component.properties;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import org.ikasan.studio.Context;
import org.ikasan.studio.model.ikasan.instance.BasicElement;
import org.ikasan.studio.model.ikasan.instance.ComponentProperty;
import org.ikasan.studio.model.ikasan.instance.FlowBeskpokeElement;
import org.ikasan.studio.model.ikasan.meta.ComponentPropertyMeta;
import org.ikasan.studio.model.psi.PIPSIIkasanModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Encapsulate the properties entry from a UI and validity perspective.
 */
public class ComponentPropertiesPanel extends PropertiesPanel implements EditBoxContainer {
    public static final Logger LOG = Logger.getInstance("ComponentPropertiesPanel");
    private transient List<ComponentPropertyEditBox> componentPropertyEditBoxList;
    private JCheckBox beskpokeComponentOverwriteCheckBox;

    /**
     * Create the ComponentPropertiesPanel
     * Note that this panel could be reused for different ComponentPropertiesPanel, it is the super.updateTargetComponent
     * that will set the property to be exposed / edited.
     * @param projectKey for this project
     * @param componentInitialisation true if this is for the popup version i.e. the first configuration of this component,
     *                                false if this is for the canvas sidebar.
     */
    public ComponentPropertiesPanel(String projectKey, boolean componentInitialisation) {
        super(projectKey, componentInitialisation);
    }

    @Override
    public void editBoxChangeListener() {
        LOG.info("editBoxChangeListener has data changed " + dataHasChanged());
        if (dataHasChanged()) {
            okButton.setEnabled(true);
        } else {
            okButton.setEnabled(false);
        }
     }
    /**
     * This method is invoked when we have checked its OK to process the panel i.e. all items are valid
     */
    protected void doOKAction() {
        // Some items may affect the bespoke / autogenerated class, in which case we only take action if the regenerateCode checkbox is selected.
        // If the user has selected the checkbox, that indicates they wish to force a re-write

        if (beskpokeComponentOverwriteCheckBox == null) {
            LOG.warn("Call to doOKAction but beskpokeComponentOverrideCheckBox was null");
//        } else if (beskpokeComponentOverrideCheckBox.isSelected() || dataHasChanged()) {
        } else if (dataHasChanged()) {
            processEditedFlowComponents();
            // This will force a regeneration of the component
            if (beskpokeComponentOverwriteCheckBox.isSelected()) {
                if (getSelectedComponent() instanceof FlowBeskpokeElement) {
                    ((FlowBeskpokeElement)getSelectedComponent()).setOverwiteEnabled(true);
                }
            }
            PIPSIIkasanModel pipsiIkasanModel = Context.getPipsiIkasanModel(projectKey);
            pipsiIkasanModel.generateJsonFromModelInstance();
            pipsiIkasanModel.generateSourceFromModelInstance();
            Context.getDesignerCanvas(projectKey).setInitialiseAllDimensions(true);
            Context.getDesignerCanvas(projectKey).repaint();
            disablePermissionToOverwriteBespokeClass();
        } else {
            LOG.info("Data hasn't changed, ignoring OK action");
        }
    }

    private void controlFieldsThatAffectBespokeClass(boolean enable) {
        for (ComponentPropertyEditBox componentPropertyEditBox : componentPropertyEditBoxList) {
            componentPropertyEditBox.controlFieldsAffectingBespokeClass(enable);
        }
    }

    private void disablePermissionToOverwriteBespokeClass() {
        controlFieldsThatAffectBespokeClass(false);
        if (beskpokeComponentOverwriteCheckBox != null) {
            beskpokeComponentOverwriteCheckBox.setSelected(false);
        }
    }


    /**
     * When updateTargetComponent is called, it will set the component to be exposed / edited, it will then
     * delegate update of the editor pane to this component so that we can specialise for different components.
     * For the given component, get all the editable properties and add them the to properties edit panel.
     */
    protected void populatePropertiesEditorPanel() {
        if (!componentInitialisation) {
            okButton.setEnabled(false);
        }

        if (getSelectedComponent() != null) {
            propertiesEditorScrollingContainer.removeAll();

            propertiesEditorPanel = new JPanel(new GridBagLayout());
            propertiesEditorPanel.setBackground(JBColor.WHITE);

            JPanel mandatoryPropertiesEditorPanel = new JPanel(new GridBagLayout());
            JPanel optionalPropertiesEditorPanel = new JPanel(new GridBagLayout());
            JPanel regeneratingPropertiesEditorPanel = new JPanel(new GridBagLayout());

            componentPropertyEditBoxList = new ArrayList<>();

            GridBagConstraints gc = new GridBagConstraints();
            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.insets = JBUI.insets(3, 4);

            int mandatoryTabley = 0;
            int regenerateTabley = 0;
            int optionalTabley = 0;
            if (getSelectedComponent().getProperty(ComponentPropertyMeta.NAME) != null) {
                componentPropertyEditBoxList.add(addNameValueToPropertiesEditPanel(mandatoryPropertiesEditorPanel,
                        getSelectedComponent().getProperty(ComponentPropertyMeta.NAME), gc, mandatoryTabley++));
            }
            if (!getSelectedComponent().getComponentMeta().getProperties().isEmpty()) {
                for (Map.Entry<String, ComponentPropertyMeta> entry : getSelectedComponent().getComponentMeta().getProperties().entrySet()) {
                    String key = entry.getKey();
                    if (!key.equals(ComponentPropertyMeta.NAME)) {
                        ComponentProperty property = getSelectedComponent().getProperty(key);
                        if (property == null) {
                            // This property has not yet been set for the component
                            property = new ComponentProperty((getSelectedComponent()).getComponentMeta().getMetadata(key));
                        }
                        if (property.getMeta().isUserImplementedClass() || property.getMeta().isAffectsBespokeClass()) {
                            componentPropertyEditBoxList.add(addNameValueToPropertiesEditPanel(
                                    regeneratingPropertiesEditorPanel,
                                    property, gc, regenerateTabley++));
                        } else if (property.getMeta().isMandatory()) {
                            componentPropertyEditBoxList.add(addNameValueToPropertiesEditPanel(
                                    mandatoryPropertiesEditorPanel,
                                    property, gc, mandatoryTabley++));
                        } else {
                            componentPropertyEditBoxList.add(addNameValueToPropertiesEditPanel(
                                    optionalPropertiesEditorPanel,
                                    property, gc, optionalTabley++));
                        }
                    }
                }
                if (!componentInitialisation && getSelectedComponent().getComponentMeta().isGeneratesBespokeClass()) {
                    addOverrideCheckBoxToPropertiesEditPanel(regeneratingPropertiesEditorPanel, gc, regenerateTabley++);
                }
            }

            GridBagConstraints gc1 = new GridBagConstraints();
            gc1.fill = GridBagConstraints.HORIZONTAL;
            gc1.insets = JBUI.insets(3, 4);
            gc1.gridx = 0;
            gc1.weightx = 1;
            gc1.gridy = 0;

            if (mandatoryTabley > 0) {
                setSubPanel(propertiesEditorPanel, mandatoryPropertiesEditorPanel, "Mandatory Properties", JBColor.RED, gc1);
            }

            if (regenerateTabley > 0) {
                setSubPanel(propertiesEditorPanel, regeneratingPropertiesEditorPanel, "Code Regenerating Properties", JBColor.ORANGE, gc1);
            }

            if (optionalTabley > 0) {
                setSubPanel(propertiesEditorPanel, optionalPropertiesEditorPanel, "Optional Properties", JBColor.LIGHT_GRAY, gc1);
            }
            propertiesEditorScrollingContainer.add(propertiesEditorPanel);

            if (!componentInitialisation && !getSelectedComponent().getComponentMeta().isGeneratesBespokeClass() && ! getComponentPropertyEditBoxList().isEmpty()) {
                okButton.setEnabled(true);
            }
        }
    }

    /**
     * Get the field that should be given the focus in popup or inscreen form
     * @return the component that should be given focus or null
     */
    public JComponent getFirstFocusField() {
        JComponent firstComponent = null;
        if (componentPropertyEditBoxList != null && !componentPropertyEditBoxList.isEmpty()) {
            firstComponent = componentPropertyEditBoxList.get(0).getInputField().getFirstFocusComponent();
        }
        return firstComponent;
    }

    /**
     * The properties panel has a series of subsections for mandatory, options and code regenerating components
     * @param allPropertiesEditorPanel is the parent
     * @param subPanel is the subsection (e.g. mandatory, optional, code regenerating)
     * @param title to place on the subsection
     * @param borderColor of the subsection
     * @param gc1 is used to dictate layout and relay layout to the next subsection.
     */
    private void setSubPanel(JPanel allPropertiesEditorPanel, JPanel subPanel, String title, Color borderColor, GridBagConstraints gc1) {
        subPanel.setBackground(JBColor.WHITE);
        subPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor), title,
                TitledBorder.LEFT,
                TitledBorder.TOP));
        allPropertiesEditorPanel.add(subPanel, gc1);
        gc1.gridy += 1;
    }

    /**
     * The override checkbox safeguards autogenerated code that the user might have subsequently customised
     * @param propertiesEditorPanel is the parent
     * @param gc is used to dictate layout and relay layout to the next subsection.
     * @param tabley is used to convey the row number
     */
    private void addOverrideCheckBoxToPropertiesEditPanel(JPanel propertiesEditorPanel, GridBagConstraints gc, int tabley) {
        JLabel overrideLabel = new JLabel("Regenerate Code");
        overrideLabel.setToolTipText("Check the box if you wish to rewrite / overwrite the existing code for this beskpoke implementation");
        beskpokeComponentOverwriteCheckBox = new JCheckBox();
        beskpokeComponentOverwriteCheckBox.addItemListener(ie -> {
                controlFieldsThatAffectBespokeClass(ie.getStateChange() == ItemEvent.SELECTED);
            }
        );
        beskpokeComponentOverwriteCheckBox.setBackground(JBColor.WHITE);
        addLabelAndSimpleInput(propertiesEditorPanel, gc, tabley, overrideLabel, beskpokeComponentOverwriteCheckBox);
    }

    /**
     *
     * @param propertiesEditorPanel to hold the name/value pair
     * @param componentProperty being added
     * @param gc is used to dictate layout and relay layout to the next subsection.
     * @param tabley is used to convey the row number
     * @return a populated 'row' i.e. a container that supports the edit of the supplied name / value pair.
     */
    private ComponentPropertyEditBox addNameValueToPropertiesEditPanel(JPanel propertiesEditorPanel, ComponentProperty componentProperty, GridBagConstraints gc, int tabley) {
        ComponentPropertyEditBox componentPropertyEditBox = new ComponentPropertyEditBox(componentProperty, componentInitialisation, this);
        addLabelAndParamInput(propertiesEditorPanel, gc, tabley, componentPropertyEditBox.getPropertyTitleField(), componentPropertyEditBox.getInputField());
        return componentPropertyEditBox;
    }

    private void addLabelAndSimpleInput(JPanel propertiesEditorPanel, GridBagConstraints gc, int tabley, JLabel propertyLabel, JComponent propertyInputField) {
        gc.weightx = 0.0;
        gc.gridx = 0;
        gc.gridy = tabley;
        propertiesEditorPanel.add(propertyLabel, gc);
        gc.weightx = 1.0;
        gc.gridx = 1;
        propertiesEditorPanel.add(propertyInputField, gc);
    }

    private void addLabelAndParamInput(JPanel propertiesEditorPanel, GridBagConstraints gc, int tabley, JLabel propertyLabel, ComponentInput componentInput) {
        gc.weightx = 0.0;
        gc.gridx = 0;
        gc.gridy = tabley;
        propertiesEditorPanel.add(propertyLabel, gc);
        ++gc.gridx;
        if (!componentInput.isBooleanInput()) {
            gc.weightx = 1.0;
            propertiesEditorPanel.add(componentInput.getFirstFocusComponent(), gc);
        } else {
            JPanel booleanPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            booleanPanel.setBackground(JBColor.WHITE);
            booleanPanel.add(new JLabel("true"));
            booleanPanel.add(componentInput.getTrueBox());
            booleanPanel.add(new JLabel("false"));
            booleanPanel.add(componentInput.getFalseBox());
            propertiesEditorPanel.add(booleanPanel, gc);
        }
    }

    private void addLabelInputEditorAndGenerateSource(JPanel propertiesEditorPanel, GridBagConstraints gc, int tabley,
                                                      JLabel propertyLabel, ComponentInput componentInput,
                                                      JLabel generateSourceLabel, JCheckBox generateSourceCheckbox) {
        gc.weightx = 0.0;
        gc.gridx = 0;
        gc.gridy = tabley;
        propertiesEditorPanel.add(propertyLabel, gc);
        gc.weightx = 1.0;
        gc.gridx = 1;
        if (!componentInput.isBooleanInput()) {
            gc.weightx = 1.0;
            propertiesEditorPanel.add(componentInput.getFirstFocusComponent(), gc);
        } else {
            JPanel booleanPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            booleanPanel.setBackground(JBColor.WHITE);
            booleanPanel.add(new JLabel("true"));
            booleanPanel.add(componentInput.getTrueBox());
            booleanPanel.add(new JLabel("false"));
            booleanPanel.add(componentInput.getFalseBox());
            propertiesEditorPanel.add(booleanPanel, gc);
        }
//        propertiesEditorPanel.add(propertyInputField, gc);

        gc.weightx = 0.0;
        gc.gridx = 2;
        propertiesEditorPanel.add(generateSourceLabel, gc);
        gc.gridx = 3;
        propertiesEditorPanel.add(generateSourceCheckbox, gc);
    }

    @Override
    protected BasicElement getSelectedComponent() {
        return (BasicElement)super.getSelectedComponent();
    }

    /**
     * Check the before and after images of the fields, if they differ then data has changed.
     * If the fields affect the bespoke user implemented class, ignore any chanes unless beskpokeComponentOverrideCheckBox is selected.
     * @return true if the above conditions hold, otherwise false.
     */
    @Override
    public boolean dataHasChanged() {
        boolean modelUpdated = false;
        if (componentPropertyEditBoxList != null) {
            for (final ComponentPropertyEditBox componentPropertyEditBox : componentPropertyEditBoxList) {
                if (componentPropertyEditBox.propertyValueHasChanged()) {
                    if (!componentPropertyEditBox.isAffectsBespokeClass() || beskpokeComponentOverwriteCheckBox.isSelected()) {
                        LOG.info("Component " + componentPropertyEditBox.getComponentProperty().getMeta().getPropertyName() + " new value is " + componentPropertyEditBox.getValue());
                        modelUpdated = true;
                        break;
                    }
                }
            }
        }
        return modelUpdated;
    }

    /**
     * Check to see if any new values have been entered, update the model and return true if that is the case.
     */
    public void processEditedFlowComponents() {
        if (componentPropertyEditBoxList != null) {
            for (final ComponentPropertyEditBox componentPropertyEditBox: componentPropertyEditBoxList) {
                if (componentPropertyEditBox.propertyValueHasChanged()) {
                    if (getSelectedComponent() instanceof FlowBeskpokeElement) {
                        ((FlowBeskpokeElement)getSelectedComponent()).setOverwiteEnabled(true);
                    }
                    // Property has been unset e.g. a boolean
                    if (!componentPropertyEditBox.editBoxHasValue()) {
                        getSelectedComponent().removeProperty(componentPropertyEditBox.getPropertyKey());
                    } else { // update existing
                        ComponentProperty componentProperty = componentPropertyEditBox.updateValueObjectWithEnteredValues();
                        // If its new this will insert, existing will just overwrite.
                        getSelectedComponent().addComponentProperty(componentPropertyEditBox.getPropertyKey(), componentProperty);
                    }
                }
            }
        }
    }

    public List<ComponentPropertyEditBox> getComponentPropertyEditBoxList() {
        return componentPropertyEditBoxList;
    }

    /**
     * Validates the values populated
     * @return a populated ValidationInfo array if htere are any validation issues.
     */
    protected java.util.List<ValidationInfo> doValidateAll() {
        List<ValidationInfo> result = new ArrayList<>();
        for (final ComponentPropertyEditBox editPair: getComponentPropertyEditBoxList()) {
            result.addAll(editPair.doValidateAll());
        }
        return result;
    }
}
