package org.ikasan.studio.ui.component.properties;

import com.intellij.openapi.ui.ValidationInfo;
import org.ikasan.studio.model.ikasan.IkasanComponentProperty;
import org.ikasan.studio.model.ikasan.IkasanComponentPropertyMeta;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Encapsulates the UI component functionality e.g. Label and appropriate editor box for a property,
 * including validation and subsequent value access.
 */
public class ComponentPropertyEditBox {
//    private String propertyTitle;
    private JLabel propertyTitleField;
    private JFormattedTextField propertyValueField;
    private JCheckBox propertyBooleanFieldTrue;
    private JCheckBox propertyBooleanFieldFalse;
    private boolean userMaintainedClass = false;
    private JCheckBox regenerateSourceCheckBox;
    private JLabel regenerateLabel;
    private IkasanComponentPropertyMeta meta;
    private IkasanComponentProperty componentProperty;

    public ComponentPropertyEditBox(IkasanComponentProperty componentProperty, boolean popupMode) {
        this.componentProperty = componentProperty;
//        this.propertyTitle = componentProperty.getMeta().getPropertyName();
        this.propertyTitleField = new JLabel(componentProperty.getMeta().getPropertyName());
        this.meta = componentProperty.getMeta();
        Object value = componentProperty.getValue();

        // @todo we can have all types of components with rich pattern matching validation
        if (meta.getPropertyDataType() == java.lang.Integer.class || meta.getPropertyDataType() == java.lang.Long.class) {
            // NUMERIC INPUT
            NumberFormat amountFormat = NumberFormat.getNumberInstance();
            this.propertyValueField = new JFormattedTextField(amountFormat);
            if (value != null) {
                // Coming from a property this may not be the correct type yet
                if (value instanceof String) {
                    value = Integer.valueOf((String) value);
                }
                propertyValueField.setValue(value);
            }
        } else if (meta.getPropertyDataType() == java.lang.Boolean.class) {
            // BOOLEAN INPUT
            propertyBooleanFieldTrue = new JCheckBox();
            propertyBooleanFieldFalse = new JCheckBox();
            propertyBooleanFieldTrue.setBackground(Color.WHITE);
            propertyBooleanFieldFalse.setBackground(Color.WHITE);
            if (value != null) {
                // Defensive, just in case not set correctly
                if (value instanceof String) {
                    value = Boolean.valueOf((String) value);
                }

                if (value instanceof Boolean) {
                    if ((Boolean)value) {
                        propertyBooleanFieldTrue.setSelected(true);
                    } else {
                        propertyBooleanFieldFalse.setSelected(true);
                    }
                }
            }
        }
        else {
            // STRING INPUT
            this.propertyValueField = new JFormattedTextField();

            if (componentProperty.getValue() != null) {
                propertyValueField.setText(componentProperty.getValue().toString());
            }
        }
        propertyTitleField.setToolTipText(componentProperty.getMeta().getHelpText());

        if (componentProperty.isUserImplementedClass() && !popupMode) {
            userMaintainedClass = true;
            regenerateLabel = new JLabel("Regenerate");
            regenerateSourceCheckBox = new JCheckBox();
            regenerateSourceCheckBox.addItemListener( ie -> {
                    propertyValueField.setEditable(ie.getStateChange() == 1);
                    propertyValueField.setEnabled(ie.getStateChange() == 1);
            });
            regenerateSourceCheckBox.setBackground(Color.WHITE);

            // Cant edit unless the regenerateSource is selected
            disableRegeneratingFeilds();
        }
    }

    /**
     * User can only select override box if a valid value has been supplied.
     */
    public void disableRegeneratingFeilds() {
        if (userMaintainedClass) {
            regenerateSourceCheckBox.setSelected(false);
            propertyValueField.setEditable(false);
            propertyValueField.setEnabled(false);
        }
    }

    /**
     * For a simple property, the key IS the property name.
     * @return the key for this property.
     */
    public String getPropertyKey() { return componentProperty.getMeta().getPropertyName(); }

    public boolean isBooleanProperty() {
        return propertyBooleanFieldTrue != null;
    }

    public ComponentInput getInputField() {
        ComponentInput componentInput = null;
        if (isBooleanProperty()) {
            componentInput = new ComponentInput(propertyBooleanFieldTrue, propertyBooleanFieldFalse);
        } else {
            componentInput = new ComponentInput(propertyValueField);
        }
        return componentInput;
    }

    /**
     * Given the class of the property, return a value of the appropriate type.
     * @return the value of the property updated by the user.
     */
    public Object getValue() {
        Object returnValue = null;
        if (meta.getPropertyDataType() == java.lang.Boolean.class) {
            // It is possible neither are current selected i.e. the property is unset
            if (isBooleanProperty() && propertyBooleanFieldTrue.isSelected()) {
                returnValue = true;
            } else if (propertyBooleanFieldFalse != null && propertyBooleanFieldFalse.isSelected()) {
                returnValue = false;
            }
        } else if (meta.getPropertyDataType() == java.lang.String.class) {
            // The formatter would be null if this was a standard text field.
            returnValue = propertyValueField.getText();
        } else {
            returnValue = propertyValueField.getValue();
        }
        return returnValue;
    }

    /**
     * For the given field type, determine if a valid value has been set.
     * @return true if the field is empty or unset
     */
    public boolean inputfieldIsUnset() {
        // For boolean we don't current support unset @todo support unset if we need to
        if (meta.getPropertyDataType() == java.lang.String.class) {
            return propertyValueField.getText() == null || propertyValueField.getText().isEmpty();
        } else if (meta.getPropertyDataType() == java.lang.Integer.class || meta.getPropertyDataType() == java.lang.Long.class) {
            // NumberFormat.getNumberInstance() will always return long
            return propertyValueField.getValue() == null || ((Long) propertyValueField.getValue() == 0);
        }
        return false;
    }

    /**
     * Validates the values populated
     * @return a populated ValidationInfo array if htere are any validation issues.
     */
    protected java.util.List<ValidationInfo> doValidateAll() {
        List<ValidationInfo> result = null;
        if (meta.isMandatory() &&
            !isBooleanProperty() &&
            inputfieldIsUnset()) {
            result = new ArrayList<>();
            result.add(new ValidationInfo(componentProperty.getMeta().getPropertyName() + " must be set to a valid value", getOverridingInputField()));
        }
        if (result == null) {
            return Collections.emptyList();
        } else {
            return result;
        }
    }

    /**
     * Determine if the edit box has a valid value
     * @return true if the editbox has a non-whitespace / real value.
     */
    public boolean editBoxHasValue() {
        boolean hasValue = false;

        Object value = getValue();
        if (value instanceof String) {
            if (((String) value).length() > 0) {
                hasValue = true;
            }
        } else {
            hasValue = (value != null);
        }

        return hasValue;
    }

    /**
     * Usually the final step of edit, update the original value object with the entered data
     */
    public IkasanComponentProperty updateValueObjectWithEnteredValues() {
        componentProperty.setValue(getValue());
        if (isUserMaintainedClassWithPermissionToRegenerate()) {
            componentProperty.setRegenerateAllowed(true);
        }
        return componentProperty;
    }

    /**
     * Determine if the data entered differs from the value object (componentProperty)
     * @return true if the property has been altered
     */
    public boolean propertyValueHasChanged() {
        Object propertyValue = componentProperty.getValue();
        return ((propertyValue == null && editBoxHasValue()) ||
                (propertyValue != null && !componentProperty.getValue().equals(getValue())) ||
                (isUserMaintainedClassWithPermissionToRegenerate() && editBoxHasValue()) );
    }

    public boolean isUserMaintainedClassWithPermissionToRegenerate() {
        return isUserMaintainedClass() && regenerateSourceCheckBox != null && regenerateSourceCheckBox.isSelected();
    }

    public boolean isUserMaintainedClass() {
        return userMaintainedClass;
    }


    public JLabel getPropertyTitleField() {
        return propertyTitleField;
    }
    public JFormattedTextField getOverridingInputField() {
        return propertyValueField;
    }
    public JCheckBox getRegenerateSourceCheckBox() { return regenerateSourceCheckBox; }
    public JLabel getRegenerateLabel() { return regenerateLabel; }

    public IkasanComponentPropertyMeta getMeta() {
        return meta;
    }
    public boolean isMandatory() {
        return meta.isMandatory();
    }
    public IkasanComponentProperty getComponentProperty() {
        return componentProperty;
    }
}
