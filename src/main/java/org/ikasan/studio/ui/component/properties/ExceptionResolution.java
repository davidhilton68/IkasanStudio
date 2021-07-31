package org.ikasan.studio.ui.component.properties;

import com.intellij.openapi.ui.ValidationInfo;
import org.ikasan.studio.model.ikasan.IkasanComponentProperty;
import org.ikasan.studio.model.ikasan.IkasanExceptionResolution;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Encapsulates the UI component functionality e.g. Label and appropriate editor box for a property,
 * including validation and subsequent value access.
 */
public class ExceptionResolution {
    ExceptionResolverEditBox parent;
    private JLabel exceptionField = new JLabel();
    private JLabel actionField = new JLabel();
    private List<ComponentPropertyEditBox> actionParamEditBoxList = new ArrayList<>();
    private boolean popupMode;
    private IkasanExceptionResolution ikasanExceptionResolution;
    private JButton deleteButton = new JButton("DEL");

    public ExceptionResolution(ExceptionResolverEditBox parent, IkasanExceptionResolution ikasanExceptionResolution, boolean popupMode) {
        this.parent = parent;
        this.ikasanExceptionResolution = ikasanExceptionResolution ;
        this.popupMode = popupMode;

        String theException = ikasanExceptionResolution.getTheException();
        String theAction = ikasanExceptionResolution.getTheAction();

        if (theException != null && !theException.isEmpty()) {
            exceptionField.setText(ikasanExceptionResolution.getTheException());
            deleteButton.addActionListener(e ->
                parent.doDelete(ikasanExceptionResolution)
            );
        }
        if (theAction != null && !theAction.isEmpty()) {
            actionField.setText(ikasanExceptionResolution.getTheAction());
        }
        if (ikasanExceptionResolution.getTheAction() != null &&
                ikasanExceptionResolution.getParams() != null &&
                !ikasanExceptionResolution.getParams().isEmpty()) {
            actionParamEditBoxList = new ArrayList<>();
            for (IkasanComponentProperty property : ikasanExceptionResolution.getParams()) {
                ComponentPropertyEditBox actionParam = new ComponentPropertyEditBox(property, this.popupMode);
                actionParamEditBoxList.add(actionParam);
            }
        }
    }

    /**
     * get the key for the exception resolution
     * @return the key for this exception resolution
     */
    public String getPropertyKey() {
        return ikasanExceptionResolution.getTheException();
    }

    /**
     * actionParams will only have elements if an action has been chosen the requires params.
     * @return
     */
    public boolean actionHasParams() {
        return !actionParamEditBoxList.isEmpty();
    }

    public List<ComponentPropertyEditBox> getActionParamsEditBoxList() {
        return actionParamEditBoxList;
    }

    /**
     * Validate the selected values
     * @return a non-empty ValidationInfo list if there are validation errors
     */
    protected List<ValidationInfo> doValidateAll() {
        return  Collections.emptyList();
    }

    public IkasanExceptionResolution getIkasanExceptionResolution() {
        return ikasanExceptionResolution;
    }

    public JLabel getExceptionField() {
        return exceptionField;
    }

    public JLabel getActionField() {
        return actionField;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }
}
