package org.ikasan.studio.ui.component.properties;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import org.ikasan.studio.core.model.ikasan.instance.BasicElement;
import org.ikasan.studio.core.model.ikasan.instance.ComponentProperty;
import org.ikasan.studio.core.model.ikasan.instance.FlowUserImplementedElement;
import org.ikasan.studio.core.model.ikasan.meta.ComponentPropertyMeta;
import org.ikasan.studio.core.model.ikasan.meta.IkasanComponentLibrary;
import org.ikasan.studio.ui.StudioUIUtils;
import org.ikasan.studio.ui.UiContext;
import org.ikasan.studio.ui.model.psi.PIPSIIkasanModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.ikasan.studio.core.model.ikasan.meta.ComponentPropertyMeta.VERSION;
import static org.ikasan.studio.ui.UiContext.PALETTE_TAB_INDEX;

/**
 * Encapsulate the properties entry from a UI and validity perspective.
 */
public class ComponentPropertiesPanel extends PropertiesPanel implements EditBoxContainer {
    public static final Logger LOG = Logger.getInstance("ComponentPropertiesPanel");
    private transient List<ComponentPropertyEditBox> componentPropertyEditBoxList;
    private JCheckBox userImplementedComponentOverwriteCheckBox;
    private ComponentDescription componentDescription;
    private boolean isExpanded;
    private JPanel optionalPropertiesEditorPanel;
    private JPanel optionalPropertiesExpandPanel;
    private JButton toggleOptionalPropertiesButton;
    private JButton setDefaultsButton;
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

    /**
     *
     */
    @Override
    public void editBoxChangeListener() {
        if (updateCodeButton != null) {
            updateCodeButton.setEnabled(dataHasChanged());
        }
     }
    /**
     * This method is invoked when we have checked it's OK to process the panel i.e. all items are valid
     */
    protected void doOKAction() {
        // Some items may affect the user implemented (autogenerated stubs classes), in which case we only take action if the regenerateCode checkbox is selected.
        // If the user has selected the checkbox, that indicates they wish to force a re-write

        if (userImplementedComponentOverwriteCheckBox != null && !userImplementedComponentOverwriteCheckBox.isSelected() ) {
            StudioUIUtils.displayIdeaWarnMessage(projectKey, "Update is not allowed unless overwrite box is ticked.");
        } else if (dataHasChanged()) {
            StudioUIUtils.displayIdeaInfoMessage(projectKey, "Code generation in progress, please wait.");
            // If the meta version has changed, we need to rerender the screen
            boolean metaPackChanged = getSelectedComponent().getComponentMeta().isModule() && propertyHasChanged(VERSION);
            updateComponentsWithNewValues();
            // This will force a regeneration of the component
            if (userImplementedComponentOverwriteCheckBox == null || userImplementedComponentOverwriteCheckBox.isSelected()) {
                if (getSelectedComponent() instanceof FlowUserImplementedElement) {
                    ((FlowUserImplementedElement)getSelectedComponent()).setOverwriteEnabled(true);
                }
            }
            PIPSIIkasanModel pipsiIkasanModel = UiContext.getPipsiIkasanModel(projectKey);
            pipsiIkasanModel.generateJsonFromModelInstance();
            pipsiIkasanModel.generateSourceFromModelInstance3();
            // Intellij startup is multi-threaded so caution is required.
            if (metaPackChanged && UiContext.getPalettePanel(projectKey) != null) {
                UiContext.getPalettePanel(projectKey).resetPallette();
            }

            UiContext.getDesignerCanvas(projectKey).setInitialiseAllDimensions(true);
            UiContext.getDesignerCanvas(projectKey).repaint();
            UiContext.getPalettePanel(projectKey).repaint();
            disablePermissionToOverwriteUserImplementedClass();
            UiContext.setRightTabbedPaneFocus(projectKey, PALETTE_TAB_INDEX);
        } else {
            StudioUIUtils.displayIdeaWarnMessage(projectKey, "Data hasn't changed, ignoring OK action.");
        }
    }

    private void controlFieldsThatAffectUserImplementedClass(boolean enable) {
        for (ComponentPropertyEditBox componentPropertyEditBox : componentPropertyEditBoxList) {
            if (!componentPropertyEditBox.getMeta().isReadOnlyProperty()) {
                componentPropertyEditBox.controlFieldsAffectingUserImplementedClass(enable);
            }
        }
    }

    private void disablePermissionToOverwriteUserImplementedClass() {
        controlFieldsThatAffectUserImplementedClass(false);
        if (userImplementedComponentOverwriteCheckBox != null) {
            userImplementedComponentOverwriteCheckBox.setSelected(false);
        }
    }


    /**
     * When updateTargetComponent is called, it will set the component to be exposed / edited, it will then
     * delegate update of the editor pane to this component so that we can specialise for different components.
     * For the given component, get all the editable properties and add them the to properties edit panel.
     */
    protected void populatePropertiesEditorPanel() {
        if (!componentInitialisation) {
            updateCodeButton.setEnabled(false);
        }

        if (getSelectedComponent() != null) {
            propertiesEditorScrollingContainer.removeAll();

            propertiesEditorPanel = new JPanel(new GridBagLayout());
            propertiesEditorPanel.setBackground(JBColor.WHITE);

            JPanel mandatoryPropertiesEditorPanel = new JPanel(new GridBagLayout());
            optionalPropertiesEditorPanel = new JPanel(new GridBagLayout());
            if (optionalPropertiesExpandPanel == null) {
                optionalPropertiesExpandPanel = getOptionalPropertiesExpandPanel();
            }
            JPanel regeneratingPropertiesEditorPanel = new JPanel(new GridBagLayout());

            componentPropertyEditBoxList = new ArrayList<>();

            GridBagConstraints gc = new GridBagConstraints();
            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.insets = JBUI.insets(3, 4);

            int mandatoryTabley = 0;
            int regenerateTabley = 0;
            int optionalTabley = 0;
            if (getSelectedComponent().getComponentMeta().isModule()) {
                // Always refresh the list of choosable metapacks
                List<String> installedMetapacks = IkasanComponentLibrary.getMetapackList();
                if (installedMetapacks != null && ! installedMetapacks.isEmpty()) {
                    getSelectedComponent().getComponentMeta().getProperties().get(VERSION).setChoices(installedMetapacks);
                }
            }

            if (getSelectedComponent().getProperty(ComponentPropertyMeta.NAME) != null) {
                componentPropertyEditBoxList.add(addNameValueToPropertiesEditPanel(mandatoryPropertiesEditorPanel,
                        getSelectedComponent().getProperty(ComponentPropertyMeta.NAME), gc, mandatoryTabley++));
            }
            if (!getSelectedComponent().getComponentMeta().getProperties().isEmpty()) {
                if (!componentInitialisation && getSelectedComponent().getComponentMeta().isGeneratesUserImplementedClass()) {
                    addOverrideCheckBoxToPropertiesEditPanel(regeneratingPropertiesEditorPanel, gc, regenerateTabley++);
                }
                for (Map.Entry<String, ComponentPropertyMeta> entry : getSelectedComponent().getComponentMeta().getProperties().entrySet()) {
                    String key = entry.getKey();
                    if (!key.equals(ComponentPropertyMeta.NAME) && !entry.getValue().isHiddenProperty() && !entry.getValue().isIgnoreProperty()) {
                        ComponentProperty property = getSelectedComponent().getProperty(key);
                        if (property == null) {
                            // This property has not yet been set for the component
                            property = new ComponentProperty((getSelectedComponent()).getComponentMeta().getMetadata(key));
                        }
                        if (property.getMeta().isUserSuppliedClass() || property.getMeta().isAffectsUserImplementedClass()) {
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
                setSubPanel(propertiesEditorPanel, regeneratingPropertiesEditorPanel, "User Code Regenerating Properties", JBColor.ORANGE, gc1);
            }

            if (optionalTabley > 0) {
                optionalPropertiesExpandPanel.setVisible(true);
                setToggleOptionalPropertiesButton(false);
                optionalPropertiesEditorPanel.setVisible(false);
                setSubPanel(propertiesEditorPanel, optionalPropertiesExpandPanel, null, null, gc1);
                setSubPanel(propertiesEditorPanel, optionalPropertiesEditorPanel, "Optional Properties", JBColor.LIGHT_GRAY, gc1);
            } else if (optionalPropertiesExpandPanel != null) {
                optionalPropertiesExpandPanel.setVisible(false);
            }
            propertiesEditorScrollingContainer.add(propertiesEditorPanel);

            if (!componentInitialisation && !getSelectedComponent().getComponentMeta().isGeneratesUserImplementedClass() && ! getComponentPropertyEditBoxList().isEmpty()) {
                updateCodeButton.setEnabled(true);
            }
            UiContext.setRightTabbedPaneFocus(projectKey, UiContext.PROPERTIES_TAB_INDEX);

            if (componentDescription != null) {
                componentDescription.setText(getSelectedComponent().getComponentMeta().getHelpText());
            }
        }
    }

    private void toggleOptionalSection() {
        isExpanded = !isExpanded;
        setToggleOptionalPropertiesButton(isExpanded);
        if (!isExpanded) {
            for (ComponentPropertyEditBox componentPropertyEditBox : componentPropertyEditBoxList) {
                if (componentPropertyEditBox.getMeta().isOptional()) {
                    componentPropertyEditBox.resetDataEntryComponentsWithNoValue();
                }
            }
        }
        if (getPropertiesDialogue() != null) {
            getPropertiesDialogue().pack();
        }
    }

    private void setToggleOptionalPropertiesButton(boolean enable) {
        optionalPropertiesEditorPanel.setVisible(enable);
        setDefaultsButton.setEnabled(enable);
        toggleOptionalPropertiesButton.setText(enable ? "Ignore" : "Expand");
    }

    protected JPanel getOptionalPropertiesExpandPanel() {
        JPanel optionalPropertiesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel optionalPropertiesLabel = new JLabel("Optional Properties");
        // Create the buttons
        toggleOptionalPropertiesButton = new JButton("Expand");
        toggleOptionalPropertiesButton.addActionListener(e -> toggleOptionalSection());
        setDefaultsButton = new JButton("Set Defaults");
        setDefaultsButton.addActionListener(e -> setOptionalPropertiesToDefaultVales());
        // Add buttons to the panel
        optionalPropertiesPanel.add(optionalPropertiesLabel);
        optionalPropertiesPanel.add(toggleOptionalPropertiesButton);
        optionalPropertiesPanel.add(setDefaultsButton);

        return optionalPropertiesPanel;
    }

    protected void setOptionalPropertiesToDefaultVales() {
//        this.selectedComponent
        for (ComponentPropertyEditBox componentPropertyEditBox : componentPropertyEditBoxList) {
            if (componentPropertyEditBox.getMeta().isOptional()) {
                componentPropertyEditBox.setDefaultValue();
            }
        }
        redrawPanel();
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
        if (title != null) {
        subPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor), title,
                TitledBorder.LEFT,
                TitledBorder.TOP));
        }
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
        JLabel overrideLabel = new JLabel("Allow Update");
        overrideLabel.setFont(new Font(overrideLabel.getFont().getName(), Font.BOLD, overrideLabel.getFont().getSize()));
        overrideLabel.setToolTipText("Check the box if you wish to rewrite / overwrite the existing code for this user implemented class");
        userImplementedComponentOverwriteCheckBox = new JCheckBox();
        userImplementedComponentOverwriteCheckBox.addItemListener(
            ie -> controlFieldsThatAffectUserImplementedClass(ie.getStateChange() == ItemEvent.SELECTED));
        userImplementedComponentOverwriteCheckBox.setBackground(JBColor.WHITE);
        addLabelAndSimpleInput(propertiesEditorPanel, gc, tabley, overrideLabel, userImplementedComponentOverwriteCheckBox);
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
        ComponentPropertyEditBox componentPropertyEditBox = new ComponentPropertyEditBox(projectKey, componentProperty, componentInitialisation, this);
        addLabelAndParamInput(propertiesEditorPanel, gc, tabley, componentPropertyEditBox.getPropertyTitleField(), componentPropertyEditBox.getDataValidationHelper(),  componentPropertyEditBox.getInputField());
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

    private void addLabelAndParamInput(JPanel propertiesEditorPanel, GridBagConstraints gc, int tabley, JLabel propertyLabel, JButton helpButton, ComponentInput componentInput) {
        gc.weightx = 0.0;
        gc.gridx = 0;
        gc.gridy = tabley;
        propertiesEditorPanel.add(propertyLabel, gc);
        ++gc.gridx;
        if (helpButton != null) {
            propertiesEditorPanel.add(helpButton, gc);
        }
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

    @Override
    protected BasicElement getSelectedComponent() {
        return (BasicElement)super.getSelectedComponent();
    }

    /**
     * Check the before and after images of the fields, if they differ then data has changed.
     * If the fields affect the user implemented class, ignore any changes unless userImplementedComponentOverwriteCheckBox is selected.
     * @return true if the above conditions hold, otherwise false.
     */
    @Override
    public boolean dataHasChanged() {
        boolean modelUpdated = false;
        if (componentPropertyEditBoxList != null) {
            for (final ComponentPropertyEditBox componentPropertyEditBox : componentPropertyEditBoxList) {
                if (componentPropertyEditBox.propertyValueHasChanged()) {
                    if (!componentPropertyEditBox.isAffectsUserImplementedClass() || userImplementedComponentOverwriteCheckBox.isSelected()) {
                        LOG.info("STUDIO: Component " + componentPropertyEditBox.getComponentProperty().getMeta().getPropertyName() + " new value is " + componentPropertyEditBox.getValue());

                        modelUpdated = true;
                        break;
                    }
                }
            }
        }
        return modelUpdated;
    }

    /**
     * Determine if the named property has changed
     * @return true if the named property has changed
     */
    public boolean propertyHasChanged(String propertyNameToSearchFor) {
        boolean propertyHasChanged = false;
        if (componentPropertyEditBoxList != null) {
            for (final ComponentPropertyEditBox componentPropertyEditBox: componentPropertyEditBoxList) {
                if (componentPropertyEditBox.getMeta().getPropertyName().equals(propertyNameToSearchFor)) {
                    if (componentPropertyEditBox.propertyValueHasChanged()) {
                        propertyHasChanged = true;
                        break;
                    }
                }
            }
        }
        return propertyHasChanged;
    }


    /**
     * Check to see if any new values have been entered, update the model and return true if that is the case.
     */
    public void updateComponentsWithNewValues() {
        if (componentPropertyEditBoxList != null) {
            for (final ComponentPropertyEditBox componentPropertyEditBox: componentPropertyEditBoxList) {
                if (componentPropertyEditBox.propertyValueHasChanged()) {
                    if (getSelectedComponent() instanceof FlowUserImplementedElement) {
                        ((FlowUserImplementedElement)getSelectedComponent()).setOverwriteEnabled(true);
                    }
                    // Property has been unset e.g. a boolean
                    if (!componentPropertyEditBox.editBoxHasValue()) {
                        getSelectedComponent().removeProperty(componentPropertyEditBox.getPropertyKey());
                    } else { // update existing
                        ComponentProperty componentProperty = componentPropertyEditBox.updateValueObjectWithEnteredValues();
                        // If its new this will insert, existing will just overwrite.
                        getSelectedComponent().addComponentProperty(componentPropertyEditBox.getPropertyKey(), componentProperty);
                    }
                    UiContext.setRightTabbedPaneFocus(projectKey, PALETTE_TAB_INDEX);
                }
            }
        }
    }

    public List<ComponentPropertyEditBox> getComponentPropertyEditBoxList() {
        return componentPropertyEditBoxList;
    }

    /**
     * Validates the values populated
     * @return a populated ValidationInfo array if there are any validation issues.
     */
    protected java.util.List<ValidationInfo> doValidateAll() {
        List<ValidationInfo> result = new ArrayList<>();
        for (final ComponentPropertyEditBox editPair: getComponentPropertyEditBoxList()) {
            result.addAll(editPair.doValidateAll());
        }
        return result;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    public void setComponentDescription(ComponentDescription componentDescription) {
        this.componentDescription = componentDescription;
    }
}
